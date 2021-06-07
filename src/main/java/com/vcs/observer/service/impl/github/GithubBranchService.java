package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.mapper.BranchMapper;
import com.vcs.observer.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GithubBranchService implements BranchService {

    @Autowired
    private VcsClient vcsClient;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    public List<BranchDto> findBranches(String username, String repository) {
        log.debug("Searching for branches for repository:{} of user:{}", repository, username);
        return vcsClient.findBranches(username, repository)
                .stream()
                .map(branchMapper::mapToBranchDto)
                .collect(Collectors.toList());
    }
}
