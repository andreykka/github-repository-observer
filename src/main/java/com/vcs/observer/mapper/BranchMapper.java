package com.vcs.observer.mapper;

import com.vcs.observer.client.dto.VcsBranch;
import com.vcs.observer.dto.BranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BranchMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "sha", source = "commit.sha")
    BranchDto mapToBranchDto(VcsBranch vcsBranch);

}
