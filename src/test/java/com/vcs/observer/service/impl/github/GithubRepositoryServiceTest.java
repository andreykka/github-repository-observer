package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsOwner;
import com.vcs.observer.client.dto.VcsRepository;
import com.vcs.observer.client.exception.VcsClientException;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.dto.OwnerDto;
import com.vcs.observer.dto.RepositoryDto;
import com.vcs.observer.dto.UserDto;
import com.vcs.observer.exception.UserNotFoundException;
import com.vcs.observer.mapper.RepositoryMapper;
import com.vcs.observer.service.BranchService;
import com.vcs.observer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {

    private static final String EXISTING_USERNAME = "user-1";

    @Mock
    private VcsClient vcsClient;

    @Mock
    private BranchService branchService;

    @Mock
    private UserService userService;

    @Spy
    private RepositoryMapper mapper = Mappers.getMapper(RepositoryMapper.class);

    @InjectMocks
    private GithubRepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(repositoryService, "batchSize", 100);
    }

    @Test
    void givenVcsClientErrorWhenFindPublicNonForkRepositoriesThenExceptionPropagated() {
        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME))
                .thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(eq(EXISTING_USERNAME), anyInt(), anyInt()))
                .thenThrow(VcsClientException.class);
        assertThatThrownBy(() -> repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME))
                .isInstanceOfAny(VcsClientException.class);
    }

    @Test
    void givenNotExistingUserWhenFindPublicNonForkRepositoriesThenUserNotFoundExceptionThrown() {
        String username = "invalidUsername";
        when(userService.getUser(username))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> repositoryService.findPublicNonForkRepositories(username))
                .isInstanceOfAny(UserNotFoundException.class);
    }

    @Test
    void givenUserWithoutRepositoriesWhenFindPublicNonForkRepositoriesThenEmptyList() {
        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME))
                .thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(eq(EXISTING_USERNAME), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        assertThat(repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME)).asList()
                .isEmpty();
    }

    @Test
    void givenUserWithForkRepositoriesWhenFindPublicNonForkRepositoriesThenEmptyList() {
        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME)).thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(eq(EXISTING_USERNAME), anyInt(), anyInt()))
                .thenReturn(asList(
                        VcsRepository.builder().name("repository-1").fork(true).build(),
                        VcsRepository.builder().name("repository-2").fork(true).build()
                ));
        assertThat(repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME)).asList()
                .isEmpty();
    }

    @Test
    void givenUserWithNonForkRepositoriesWhenFindPublicNonForkRepositoriesThenRepositoriesReturned() {
        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME)).thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(eq(EXISTING_USERNAME), anyInt(), anyInt()))
                .thenReturn(asList(
                        VcsRepository.builder()
                                .name("repository-1")
                                .owner(VcsOwner.builder().login(EXISTING_USERNAME).build())
                                .fork(false)
                                .build(),
                        VcsRepository.builder()
                                .name("repository-2")
                                .owner(VcsOwner.builder().login(EXISTING_USERNAME).build())
                                .fork(false)
                                .build()
                ));
        assertThat(repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME)).asList()
                .hasSize(2)
                .contains(
                        RepositoryDto.builder()
                                .name("repository-1")
                                .owner(OwnerDto.builder().login(EXISTING_USERNAME).build())
                                .build(),
                        RepositoryDto.builder()
                                .name("repository-2")
                                .owner(OwnerDto.builder().login(EXISTING_USERNAME).build())
                                .build()
                );
    }

    @Test
    void givenUserWithMultipleRepositoriesWhenFindPublicNonForkRepositoriesThenRepositoriesFetchedInBatches() {
        // assume batch size = 2
        final int batchSize = 2;
        ReflectionTestUtils.setField(repositoryService, "batchSize", batchSize);

        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME)).thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(anyString(), anyInt(), anyInt()))
                // first page with 2 elements
                .thenReturn(asList(
                        VcsRepository.builder().name("repository-1").build(),
                        VcsRepository.builder().name("repository-2").build()
                ))
                // second page with 1 element
                .thenReturn(singletonList(VcsRepository.builder().name("repository-3").build()));
        // do not fetch branches for this test
        when(branchService.findBranches(anyString(), anyString())).thenReturn(Collections.emptyList());

        repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME);

        // verify repositories fetched in batches by 2 per request
        verify(vcsClient).findRepositories(eq(EXISTING_USERNAME), eq(1), eq(batchSize));
        verify(vcsClient).findRepositories(eq(EXISTING_USERNAME), eq(2), eq(batchSize));
        // no more redundant calls to VCS
        verifyNoMoreInteractions(vcsClient);
    }

    @Test
    void givenUserWithRepositoryWhenFindPublicNonForkRepositoriesThenBranchesFetchedForRepository() {
        final UserDto existingUser = UserDto.builder().login(EXISTING_USERNAME).build();
        when(userService.getUser(EXISTING_USERNAME)).thenReturn(Optional.of(existingUser));
        when(vcsClient.findRepositories(eq(EXISTING_USERNAME), anyInt(), anyInt()))
                .thenReturn(singletonList(VcsRepository.builder()
                        .name("repository-1")
                        .owner(VcsOwner.builder().login(EXISTING_USERNAME).build())
                        .fork(false)
                        .build()
                ));
        when(branchService.findBranches(eq(EXISTING_USERNAME), eq("repository-1")))
                .thenReturn(asList(
                        BranchDto.builder().name("master").sha("111").build(),
                        BranchDto.builder().name("feature").sha("222").build()
                ));
        assertThat(repositoryService.findPublicNonForkRepositories(EXISTING_USERNAME)).asList()
                .hasSize(1)
                .contains(RepositoryDto.builder()
                        .name("repository-1")
                        .owner(OwnerDto.builder().login(EXISTING_USERNAME).build())
                        .branch(BranchDto.builder().name("master").sha("111").build())
                        .branch(BranchDto.builder().name("feature").sha("222").build())
                        .build());
    }

}