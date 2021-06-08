package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsBranch;
import com.vcs.observer.client.dto.VcsCommit;
import com.vcs.observer.client.exception.VcsClientException;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.mapper.BranchMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubBranchServiceTest {

    public static final String EXISTING_USER = "username";
    public static final String EXISTING_REPOSITORY = "repository-1";
    @Mock
    private VcsClient vcsClient;

    @Spy
    private BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);

    @InjectMocks
    private GithubBranchService branchService;

    @Test
    void givenVcsClientErrorWhenGetBranchesThenExceptionPropagated() {
        String username = "invalidUsername";
        String repository = "invalidRepository";
        when(vcsClient.findBranches(eq(username), eq(repository)))
                .thenThrow(VcsClientException.class);
        assertThatThrownBy(() -> branchService.findBranches(username, repository))
                .isInstanceOfAny(VcsClientException.class);
    }

    @Test
    void givenNoneBranchesExistsForRepositoryErrorWhenGetBranchesThenEmptyList() {
        String username = EXISTING_USER;
        String repository = EXISTING_REPOSITORY;
        when(vcsClient.findBranches(eq(username), eq(repository)))
                .thenReturn(Collections.emptyList());
        assertThat(branchService.findBranches(username, repository)).asList().isEmpty();
    }

    @Test
    void givenMultipleBranchReturnedFromClientWhenGetBranchesThenReturnBranches() {
        String username = EXISTING_USER;
        String repository = EXISTING_REPOSITORY;
        when(vcsClient.findBranches(eq(username), eq(repository)))
                .thenReturn(Arrays.asList(
                        VcsBranch.builder()
                                .name("branch-1")
                                .commit(VcsCommit.builder().sha("111").build())
                                .build(),
                        VcsBranch.builder()
                                .name("branch-2")
                                .commit(VcsCommit.builder().sha("222").build())
                                .build()
                ));
        assertThat(branchService.findBranches(username, repository)).asList().containsExactly(
                BranchDto.builder().name("branch-1").sha("111").build(),
                BranchDto.builder().name("branch-2").sha("222").build()
        );
    }
}