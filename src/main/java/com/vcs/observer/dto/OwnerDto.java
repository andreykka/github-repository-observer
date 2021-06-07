package com.vcs.observer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class OwnerDto implements Serializable {

    private static final long serialVersionUID = -2520597186274292248L;

    private String login;
}

