package com.github.observer.rest;

import com.github.observer.dto.RepositoryDto;
import com.github.observer.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryResource {

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/repositories/owner/{username}")
    public List<RepositoryDto> findRepositories(@PathVariable String username) {
        return repositoryService.findUserRepositories(username);
    }
}
