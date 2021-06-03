package com.github.observer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RepositoryDto implements Serializable {

    private String name;
    private OwnerDto owner;
    private List<BranchDto> branches;
}
