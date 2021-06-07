package com.vcs.observer.mapper;

import com.vcs.observer.client.dto.VcsOwner;
import com.vcs.observer.dto.OwnerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OwnerMapper {

    @Mapping(target = "login", source = "login")
    OwnerDto mapToOwnerDto(VcsOwner vcsOwner);
}
