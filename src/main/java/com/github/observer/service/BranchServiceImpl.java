package com.github.observer.service;

import com.github.observer.client.github.GithubClient;
import com.github.observer.dto.BranchDto;
import com.github.observer.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchServiceImpl implements BranchService {

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    public List<BranchDto> findBranches(String username, String repository) {
        return githubClient.findBranches(username, repository)
                .stream()
                .map(branchMapper::mapToBranchDto)
                .collect(Collectors.toList());
    }
}
