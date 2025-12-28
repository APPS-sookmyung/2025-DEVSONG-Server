package com.devsong.server.ranking.service;

import com.devsong.server.ranking.dto.BojRankingResponseDto;
import com.devsong.server.ranking.dto.GithubEventResponseDto;
import com.devsong.server.ranking.dto.GithubRankingResponseDto;
import com.devsong.server.ranking.dto.SolvedAcResponseDto;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String githubToken;


    //백준
    // 스케줄러 메서드
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초
    @Transactional
    public void updateBojRankings() {

        // 백준 아이디 있는 유저 다 가져오기
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getBojId() != null && !u.getBojId().isBlank())
                .toList();

        if (users.isEmpty()) return;

        // API 호출을 위한 핸들 문자열 만들기
        String handles = users.stream()
                .map(User::getBojId)
                .map(String::trim)
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl("https://solved.ac/api/v3/user/lookup")
                .queryParam("handles", handles)
                .toUriString();

        SolvedAcResponseDto[] response = restTemplate.getForObject(url, SolvedAcResponseDto[].class);

        if (response == null) return;


        // 받아온 데이터를 Map으로 변환
        Map<String, SolvedAcResponseDto> infoMap = Arrays.stream(response)
                .collect(Collectors.toMap(
                        dto -> dto.getHandle().toLowerCase().trim(),
                        dto -> dto
                ));

        // DB 유저 정보 업데이트
        for (User user : users) {
            SolvedAcResponseDto info = infoMap.get(user.getBojId().toLowerCase().trim());
            if (info != null) {
                // User 엔티티에 새로 만든 메서드로 값 갱신
                user.updateBojInfo(info.getRating(), info.getSolvedCount());
            }
        }
    }

    // 조회 메서드
    @Transactional(readOnly = true)
    public List<BojRankingResponseDto> getBojRanking() {
        // DB에서 유저 다 가져오기
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getBojId() != null && !u.getBojId().isBlank())
                .toList();

        // DTO로 변환
        List<BojRankingResponseDto> rankingList = new ArrayList<>();

        for (User user : users) {
            rankingList.add(BojRankingResponseDto.builder()
                    .rank(0)
                    .username(user.getUsername())
                    .bojId(user.getBojId())
                    .rating(user.getBojRating())
                    .solvedCount(user.getBojSolvedCount())
                    .build());
        }

        // 정렬 (레이팅 내림차순)
        rankingList.sort(Comparator.comparingInt(BojRankingResponseDto::getRating).reversed());

        // 등수 매기기
        for (int i = 0; i < rankingList.size(); i++) {
            rankingList.get(i).setRank(i + 1);
        }

        return rankingList;
    }

    //깃허브
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초
    @Transactional
    public void updateGithubRankings() {

        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        //깃허브 아이디 있는 유저만 조회
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getGithubId() != null && !u.getGithubId().isBlank())
                .toList();

        if (users.isEmpty()) return;

        //각 유저별로 주간 커밋 수 계산
        for (User user : users) {
            int weeklyCommitCount =
                    fetchWeeklyCommitCount(user.getGithubId(), weekStart);
            user.updateGithubInfo(0, weeklyCommitCount);
        }
    }

    //주간 커밋 수 집계
    private int fetchWeeklyCommitCount(String githubId, LocalDate weekStart) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken.trim());
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        //Github Event API
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.github.com/users/{username}/events/public")
                .queryParam("per_page", 100)
                .buildAndExpand(githubId.trim())
                .toUriString();

        ResponseEntity<GithubEventResponseDto[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        GithubEventResponseDto[].class
                );

        GithubEventResponseDto[] events = response.getBody();
        if (events == null) return 0;

        int commitCount = 0;

        for (GithubEventResponseDto event : events) {

            if (!"PushEvent".equals(event.getType())) continue;

            ZonedDateTime eventTimeUtc =
                    ZonedDateTime.parse(event.getCreated_at());

            LocalDate eventDateKst =
                    eventTimeUtc
                            .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                            .toLocalDate();

            if (eventDateKst.isBefore(weekStart)) continue;

            commitCount += 1;
        }

        return commitCount;
    }

    // 조회 메서드
    @Transactional(readOnly = true)
    public List<GithubRankingResponseDto> getGithubRanking() {

        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getGithubId() != null && !u.getGithubId().isBlank())
                .toList();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        List<GithubRankingResponseDto> rankingList = new ArrayList<>();

        for (User user : users) {
            rankingList.add(
                    GithubRankingResponseDto.builder()
                            .rank(0)
                            .username(user.getUsername())
                            .githubId(user.getGithubId())
                            .commitCount(user.getCommitCount())
                            .build()
            );
        }

        rankingList.sort(
                Comparator.comparingInt(GithubRankingResponseDto::getCommitCount)
                        .reversed()
        );

        for (int i = 0; i < rankingList.size(); i++) {
            rankingList.get(i).setRank(i + 1);
        }

        return rankingList;
    }
}
