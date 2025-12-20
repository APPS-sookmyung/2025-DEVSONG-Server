package com.devsong.server.ranking.BOJ.service;

import com.devsong.server.ranking.BOJ.dto.BojRankingResponseDto;
import com.devsong.server.ranking.BOJ.dto.SolvedAcResponseDto;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

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
                        dto -> dto.getHandle().toLowerCase(),
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
}