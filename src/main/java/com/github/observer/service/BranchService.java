package com.github.observer.service;

import com.github.observer.dto.BranchDto;

import java.util.List;

public interface BranchService {
    List<BranchDto> findBranches(String username, String repository);
}
