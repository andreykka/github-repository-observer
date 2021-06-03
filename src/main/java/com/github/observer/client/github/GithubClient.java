package com.github.observer.client.github;

import com.github.observer.client.github.dto.GithubBranch;
import com.github.observer.client.github.dto.GithubRepository;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface GithubClient {

    @RequestLine("GET /users/{username}/repos")
    List<GithubRepository> findRepositories(@Param("username") String username);

    @RequestLine("GET /repos/{owner}/{repo}/branches")
    List<GithubBranch> findBranches(@Param("owner") String owner, @Param("repo") String repositoryName);
}
