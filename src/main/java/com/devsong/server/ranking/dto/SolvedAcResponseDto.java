package com.devsong.server.ranking.dto;

import lombok.Data;

@Data
public class SolvedAcResponseDto {
    private String handle;       // 백준 아이디
    private int rating;          // 레이팅 점수
    private int solvedCount;     // 푼 문제 수
}