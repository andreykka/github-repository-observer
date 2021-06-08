package com.vcs.observer.service.impl.github;

import com.vcs.observer.client.VcsClient;
import com.vcs.observer.client.dto.VcsUser;
import com.vcs.observer.client.exception.VcsClientException;
import com.vcs.observer.client.exception.VcsClientResourceNotFoundException;
import com.vcs.observer.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubUserServiceTest {

    @Mock
    private VcsClient vcsClient;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private GithubUserService userService;

    @Test
    void givenVcsSystemThrownExceptionWhenGetUserThenExceptionPropagated() {
        final String username = "invalid_username";
        when(vcsClient.getUser(eq(username)))
                .thenThrow(VcsClientException.class);
        assertThatThrownBy(() -> userService.getUser(username)).isInstanceOf(VcsClientException.class);
    }

    @Test
    void givenNonExistingUserInVcsSystemWhenGetUserThenOptionalEmpty() {
        final String username = "invalid_username";
        when(vcsClient.getUser(eq(username)))
                .thenThrow(VcsClientResourceNotFoundException.class);
        assertThat(userService.getUser(username)).isEmpty();
    }

    @Test
    void givenExistingUserInVcsSystemWhenGetUserThenUserReturned() {
        final String username = "invalid_username";
        final VcsUser vcsUser = VcsUser.builder()
                .login(username)
                .email("user@domain.com")
                .build();
        when(vcsClient.getUser(eq(username)))
                .thenReturn(vcsUser);
        assertThat(userService.getUser(username))
                .hasValueSatisfying(userDto -> assertThat(userDto.getLogin()).isEqualTo(vcsUser.getLogin()));
    }
}