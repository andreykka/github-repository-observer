package com.github.observer.service;

import com.github.observer.client.github.GithubClient;
import com.github.observer.dto.BranchDto;
import com.github.observer.dto.RepositoryDto;
import com.github.observer.mapper.RepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private BranchService branchService;

    @Override
    public List<RepositoryDto> findUserRepositories(String username) {
        return githubClient.findRepositories(username)
                .parallelStream()
                .map(repo -> {
                    final List<BranchDto> branches = branchService.findBranches(repo.getOwner().getLogin(), repo.getName());
                    return repositoryMapper.mapToRepositoryWithBranchesDto(repo, branches);
                })
                .collect(Collectors.toList());
    }
}
