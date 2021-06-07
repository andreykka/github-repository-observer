package com.vcs.observer.service;

import com.vcs.observer.dto.UserDto;

import java.util.Optional;

public interface UserService {
    /**
     * Get User information by username
     *
     * @param username system username or login
     * @return Found user wrapped with {@link Optional} or {@link Optional#empty()}
     */
    Optional<UserDto> getUser(String username);

}
