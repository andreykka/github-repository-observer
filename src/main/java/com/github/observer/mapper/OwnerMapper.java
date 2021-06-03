package com.github.observer.mapper;

import com.github.observer.client.github.dto.GithubOwner;
import com.github.observer.dto.OwnerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OwnerMapper {

    @Mapping(target = "login", source = "login")
    OwnerDto mapToOwnerDto(GithubOwner githubOwner);
}
