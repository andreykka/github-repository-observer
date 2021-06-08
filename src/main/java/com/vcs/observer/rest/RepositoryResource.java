package com.vcs.observer.rest;

import com.vcs.observer.dto.RepositoryDto;
import com.vcs.observer.exception.model.ErrorResponse;
import com.vcs.observer.service.RepositoryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class RepositoryResource {

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation(value = "Find all public, not fork repositories for specified user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved value"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 406, message = "Invalid Media type used")
    })
    @GetMapping(value = "/repositories/owner/{username}", produces = APPLICATION_JSON_VALUE)
    public List<RepositoryDto> findRepositories(
            @ApiParam(name = "username", value = "login or username of VCS User") @PathVariable String username) {
        return repositoryService.findPublicNonForkRepositories(username);
    }
}
