package com.devsong.server.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GithubGraphqlRequestDto {
    private String query;
    private Map<String, Object> variables;
}

