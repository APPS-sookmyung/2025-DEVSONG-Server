package com.devsong.server.ranking.dto;

import lombok.Data;

@Data
public class SolvedAcResponseDto {

    private String handle;
    private int rating;
    private int solvedCount;
}
