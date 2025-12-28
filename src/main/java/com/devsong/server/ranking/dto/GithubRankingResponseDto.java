package com.devsong.server.ranking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubRankingResponseDto {

    private int rank;
    private String username;
    private String githubId;
    private int commitCount;
}
