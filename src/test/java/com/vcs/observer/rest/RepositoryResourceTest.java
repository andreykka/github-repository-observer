package com.vcs.observer.rest;

import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.dto.OwnerDto;
import com.vcs.observer.dto.RepositoryDto;
import com.vcs.observer.exception.UserNotFoundException;
import com.vcs.observer.exception.handler.ApplicationExceptionHandler;
import com.vcs.observer.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RepositoryResource.class)
@ContextConfiguration(classes = {RepositoryResource.class, RepositoryService.class, ApplicationExceptionHandler.class})
public class RepositoryResourceTest {

    private static final String EXISTING_USERNAME = "user";

    @MockBean
    private RepositoryService repositoryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenInvalidAcceptTypeWhenGetRepositoriesThenIsNotAcceptable() throws Exception {
        mockMvc.perform(get("/repositories/owner/{username}", EXISTING_USERNAME)
                .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status", equalTo(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", equalTo("Media type not supported. Use:[application/json]")));
    }

    @Test
    public void givenNotExistingUsernameWhenGetRepositoriesThenNotFound() throws Exception {
        final String notExistingUsername = "not_existing";
        when(repositoryService.findPublicNonForkRepositories(eq(notExistingUsername)))
                .thenThrow(new UserNotFoundException(notExistingUsername));
        mockMvc.perform(get("/repositories/owner/{username}", notExistingUsername)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", equalTo("User:'not_existing' is not found")));
    }

    @Test
    public void givenExistingUsernameWhenGetRepositoriesThenListOfRepositoriesReturned() throws Exception {
        when(repositoryService.findPublicNonForkRepositories(eq(EXISTING_USERNAME)))
                .thenReturn(Arrays.asList(
                        RepositoryDto.builder()
                                .name("repository-1")
                                .owner(OwnerDto.builder().login(EXISTING_USERNAME).build())
                                .branch(BranchDto.builder().name("master").build())
                                .build(),
                        RepositoryDto.builder()
                                .name("repository-2")
                                .owner(OwnerDto.builder().login(EXISTING_USERNAME).build())
                                .branch(BranchDto.builder().name("master").build())
                                .branch(BranchDto.builder().name("feature").build())
                                .build())
                );

        mockMvc.perform(get("/repositories/owner/{username}", EXISTING_USERNAME)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", equalTo("repository-1")))
                .andExpect(jsonPath("$.[0].owner.login", equalTo(EXISTING_USERNAME)))
                .andExpect(jsonPath("$.[0].branches", hasSize(1)))
                .andExpect(jsonPath("$.[0].branches[0].name", equalTo("master")))
                .andExpect(jsonPath("$.[1].name", equalTo("repository-2")))
                .andExpect(jsonPath("$.[1].owner.login", equalTo(EXISTING_USERNAME)))
                .andExpect(jsonPath("$.[1].branches", hasSize(2)))
                .andExpect(jsonPath("$.[1].branches[0].name", equalTo("master")))
                .andExpect(jsonPath("$.[1].branches[1].name", equalTo("feature")));
    }


}