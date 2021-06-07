package com.vcs.observer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RepositoryDto implements Serializable {
    private static final long serialVersionUID = -4535551247990534301L;

    private String name;
    private OwnerDto owner;
    @Singular
    private List<BranchDto> branches;
}
