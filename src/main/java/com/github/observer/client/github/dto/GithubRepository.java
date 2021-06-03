package com.github.observer.client.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepository implements Serializable {
    private String name;
    private GithubOwner owner;
}
