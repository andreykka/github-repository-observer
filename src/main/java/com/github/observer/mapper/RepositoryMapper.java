package com.github.observer.mapper;

import com.github.observer.client.github.dto.GithubRepository;
import com.github.observer.dto.BranchDto;
import com.github.observer.dto.RepositoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {OwnerMapper.class, BranchMapper.class})
public interface RepositoryMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "branches", ignore = true)
    RepositoryDto mapToRepositoryDto(GithubRepository githubRepository);


    @Mapping(target = "name", source = "repository.name")
    @Mapping(target = "owner", source = "repository.owner")
    @Mapping(target = "branches", source = "branches")
    RepositoryDto mapToRepositoryWithBranchesDto(GithubRepository repository, List<BranchDto> branches);
}
