package com.vcs.observer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class BranchDto implements Serializable {

    private static final long serialVersionUID = -3059167898842273205L;

    private String name;
    private String sha;
}
