package com.github.observer.mapper;

import com.github.observer.client.github.dto.GithubBranch;
import com.github.observer.dto.BranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BranchMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "sha", source = "commit.sha")
    BranchDto mapToBranchDto(GithubBranch githubBranch);

}
