package com.devsong.server.githubRanking.github.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubRankingResponseDto {
    private int rank;           //등수
    private String username;    //이름
    private String githubId;    //깃허브 아이디
    private int commitCount;    //커밋 수
}