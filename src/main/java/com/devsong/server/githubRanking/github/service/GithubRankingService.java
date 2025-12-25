package com.devsong.server.githubRanking.github.service;

import com.devsong.server.githubRanking.github.dto.GithubRankingResponseDto;
import com.devsong.server.githubRanking.github.dto.GithubSearchResponseDto;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GithubRankingService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String githubToken;

    public List<GithubRankingResponseDto> getGithubRanking() {

        // DB에서 유저 가져오기
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getGithubId() != null && !u.getGithubId().isBlank())
                .toList();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        List<GithubRankingResponseDto> rankingList = new ArrayList<>();

        // 유저 정보와 API 정보 합치기
        for (User user : users) {
            int commitCount = fetchCommitCount(user.getGithubId().trim());

            rankingList.add(
                    GithubRankingResponseDto.builder()
                            .rank(0)
                            .username(user.getUsername())
                            .githubId(user.getGithubId())
                            .commitCount(commitCount)
                            .build()
            );
        }

        // 정렬 (커밋 수 내림차순)
        rankingList.sort(
                Comparator.comparingInt(GithubRankingResponseDto::getCommitCount).reversed());

        // 등수 매기기
        for (int i = 0; i < rankingList.size(); i++) {
            rankingList.get(i).setRank(i + 1);
        }

        return rankingList;
    }

    // GitHub Search API 호출
    private int fetchCommitCount(String githubId) {
        String url = "https://api.github.com/search/commits?q=author:" + githubId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.set(
                "Accept",
                "application/vnd.github.cloak-preview+json"
        );

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubSearchResponseDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        GithubSearchResponseDto.class
                );

        if (response.getBody() == null) {
            return 0;
        }

        return response.getBody().getTotal_count();
    }
}
