package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsRepository;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.dto.RepositoryDto;
import com.vcs.observer.exception.UserNotFoundException;
import com.vcs.observer.mapper.RepositoryMapper;
import com.vcs.observer.service.BranchService;
import com.vcs.observer.service.RepositoryService;
import com.vcs.observer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class GithubRepositoryService implements RepositoryService {

    @NotNull
    @Value("${github.batch-size}")
    private Integer batchSize;

    @Autowired
    private VcsClient vcsClient;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @Override
    public List<RepositoryDto> findPublicNonForkRepositories(String username) {
        log.debug("Searching for public non-fork repositories of user:{}", username);
        validateUserExists(username);
        return findPublicRepositoriesForUser(username)
                .parallel()
                .filter(repo -> !repo.isFork())
                .map(repo -> {
                    List<BranchDto> branches = branchService.findBranches(username, repo.getName());
                    return repositoryMapper.mapToRepositoryDto(repo, branches);
                })
                .collect(Collectors.toList());
    }

    private void validateUserExists(String username) {
        userService.getUser(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Stream<VcsRepository> findPublicRepositoriesForUser(String username) {
        Stream<VcsRepository> result = Stream.empty();
        int page = 1;
        List<VcsRepository> repositories;
        do {
            repositories = vcsClient.findRepositories(username, page++, batchSize);
            result = Stream.concat(result, repositories.stream());
        } while (repositories.size() == batchSize);
        return result;
    }
}
