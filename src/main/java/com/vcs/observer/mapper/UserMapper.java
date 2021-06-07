package com.vcs.observer.mapper;

import com.vcs.observer.client.dto.VcsUser;
import com.vcs.observer.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "login", source = "login")
    UserDto mapToUserDto(VcsUser vcsUser);
}
