package com.vcs.observer.client;

import com.vcs.observer.client.dto.VcsBranch;
import com.vcs.observer.client.dto.VcsRepository;
import com.vcs.observer.client.dto.VcsUser;
import com.vcs.observer.client.exception.VcsClientBadRequestException;
import com.vcs.observer.client.exception.VcsClientInternalServerException;

import java.util.List;

/**
 * Version Control System(VCS) client interface. Implement it for desired VCS
 */
public interface VcsClient {

    /**
     * Find user by username
     *
     * @param username login of the user
     * @return found user of {@code null} if not found
     * @throws VcsClientBadRequestException     in case of malformed request to vcs system
     * @throws VcsClientInternalServerException in case of vcs internal server error
     */
    VcsUser getUser(String username);

    /**
     * Find all repositories for a user
     *
     * @param username - login of the user
     * @return List of Repositories found for this user.
     * @throws VcsClientBadRequestException     in case of malformed request to vcs system
     * @throws VcsClientInternalServerException in case of vcs internal server error
     */
    List<VcsRepository> findRepositories(String username, Integer page, Integer pageSize);

    /**
     * Find all branches for user's repository
     *
     * @param owner          - user login that owns a repository
     * @param repositoryName name of the repository
     * @return List of branches of a repository(that belongs to a repository owner)
     * @throws VcsClientBadRequestException     in case of malformed request to vcs system
     * @throws VcsClientInternalServerException in case of vcs internal server error
     */
    List<VcsBranch> findBranches(String owner, String repositoryName);
}
