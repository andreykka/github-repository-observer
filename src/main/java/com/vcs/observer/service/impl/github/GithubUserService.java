package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsUser;
import com.vcs.observer.client.exception.VcsClientResourceNotFoundException;
import com.vcs.observer.dto.UserDto;
import com.vcs.observer.mapper.UserMapper;
import com.vcs.observer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GithubUserService implements UserService {

    @Autowired
    private VcsClient vcsClient;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Optional<UserDto> getUser(String username) {
        log.debug("Getting user:{}", username);
        return getVcsUser(username).map(userMapper::mapToUserDto);
    }

    private Optional<VcsUser> getVcsUser(String username) {
        return Optional.ofNullable(username)
                .map(_username -> {
                    try {
                        return vcsClient.getUser(_username);
                    } catch (VcsClientResourceNotFoundException e) {
                        log.debug("User:{} not found", _username, e);
                        return null;
                    }
                });
    }

}
