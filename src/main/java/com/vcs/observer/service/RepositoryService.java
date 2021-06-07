package com.vcs.observer.service;

import com.vcs.observer.dto.RepositoryDto;

import java.util.List;

public interface RepositoryService {

    /**
     * Find All public repositories of a user that is not a fork
     * @param username system username or login
     * @return List of repositories if such exists or Empty list
     */
    List<RepositoryDto> findPublicNonForkRepositories(String username);

}
