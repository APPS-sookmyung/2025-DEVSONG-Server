package com.devsong.server.ranking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BojRankingResponseDto {

    private int rank;
    private String username;
    private String bojId;
    private int rating;
    private int solvedCount;
}
