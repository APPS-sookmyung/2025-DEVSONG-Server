package com.devsong.server.githubRanking.github.controller;

import com.devsong.server.githubRanking.github.dto.GithubRankingResponseDto;
import com.devsong.server.githubRanking.github.service.GithubRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class GithubRankingController {

    private final GithubRankingService rankingService;

    @GetMapping("/github")
    public List<GithubRankingResponseDto> getGithubRanking() {
        return rankingService.getGithubRanking();
    }
}