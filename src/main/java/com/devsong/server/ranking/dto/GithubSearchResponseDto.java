package com.devsong.server.ranking.dto;

import lombok.Data;

@Data
public class GithubSearchResponseDto {
    private int total_count; //커밋 수
    private boolean incomplete_results; //전체 결과를 불러왔는지 여부
}
