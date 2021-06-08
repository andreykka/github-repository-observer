package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.mapper.BranchMapper;
import com.vcs.observer.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GithubBranchService implements BranchService {

    private final VcsClient vcsClient;
    private final BranchMapper branchMapper;

    public GithubBranchService(VcsClient vcsClient, BranchMapper branchMapper) {
        this.vcsClient = vcsClient;
        this.branchMapper = branchMapper;
    }

    @Override
    public List<BranchDto> findBranches(String username, String repository) {
        log.debug("Searching for branches for repository:{} of user:{}", repository, username);
        return vcsClient.findBranches(username, repository)
                .stream()
                .map(branchMapper::mapToBranchDto)
                .collect(Collectors.toList());
    }
}
