package com.devsong.server.ranking.BOJ.controller;

import com.devsong.server.ranking.BOJ.dto.BojRankingResponseDto;
import com.devsong.server.ranking.BOJ.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/boj")
    public List<BojRankingResponseDto> getBojRanking() {
        return rankingService.getBojRanking();
    }
}