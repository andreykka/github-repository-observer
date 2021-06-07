package com.vcs.observer.client.impl.github;

import com.vcs.observer.client.dto.VcsBranch;
import com.vcs.observer.client.dto.VcsRepository;
import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsUser;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Implementation of Github version control system
 */

public interface GithubClient extends VcsClient {

    @Override
    @RequestLine("GET /users/{username}")
    VcsUser getUser(@Param("username") String username);

    @Override
    @RequestLine("GET /users/{username}/repos?page={page}&page_size={pageSize}")
    List<VcsRepository> findRepositories(@Param("username") String username,
                                         @Param("page") Integer page,
                                         @Param("pageSize") Integer pageSize );

    @Override
    @RequestLine("GET /repos/{owner}/{repo}/branches")
    List<VcsBranch> findBranches(@Param("owner") String owner, @Param("repo") String repositoryName);
}
