package com.github.observer.service;

import com.github.observer.dto.RepositoryDto;

import java.util.List;

public interface RepositoryService {

    public List<RepositoryDto> findUserRepositories(String username);

}
