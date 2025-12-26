package com.devsong.server.ranking.controller;

import com.devsong.server.ranking.dto.BojRankingResponseDto;
import com.devsong.server.ranking.service.RankingService;
import com.devsong.server.ranking.dto.GithubRankingResponseDto;
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

    @GetMapping("/github")
    public List<GithubRankingResponseDto> getGithubRanking() {
        return rankingService.getGithubRanking();
    }
}