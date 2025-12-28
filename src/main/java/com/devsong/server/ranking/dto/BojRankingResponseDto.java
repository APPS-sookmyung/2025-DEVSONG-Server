package com.devsong.server.ranking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BojRankingResponseDto {
    private int rank;            // 등수
    private String username;     // 이름
    private String bojId;        // 백준 아이디
    private int rating;          // 레이팅
    private int solvedCount;     // 푼 문제 수
}