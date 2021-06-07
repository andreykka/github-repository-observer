package com.vcs.observer.service;

import com.vcs.observer.dto.BranchDto;

import java.util.List;

public interface BranchService {
    /**
     * Find All branches of a user's repository
     * @param username system username or login
     * @param repository repository name
     * @return List of found branches or Empty list if there is none branches
     */
    List<BranchDto> findBranches(String username, String repository);
}
