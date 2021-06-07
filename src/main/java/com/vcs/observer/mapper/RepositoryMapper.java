package com.vcs.observer.mapper;

import com.vcs.observer.client.dto.VcsRepository;
import com.vcs.observer.dto.BranchDto;
import com.vcs.observer.dto.RepositoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = OwnerMapper.class)
public interface RepositoryMapper {

    @Mapping(target = "name", source = "repository.name")
    @Mapping(target = "owner", source = "repository.owner")
    @Mapping(target = "branches", source = "branches")
    RepositoryDto mapToRepositoryDto(VcsRepository repository, List<BranchDto> branches);

}
