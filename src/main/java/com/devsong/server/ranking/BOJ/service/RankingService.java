package com.devsong.server.ranking.BOJ.service;

import com.devsong.server.ranking.BOJ.dto.BojRankingResponseDto;
import com.devsong.server.ranking.BOJ.dto.SolvedAcResponseDto;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public List<BojRankingResponseDto> getBojRanking() {
        // DB에서 유저 가져오기
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getBojId() != null && !u.getBojId().isBlank())
                .toList();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        // solved.ac API 호출
        String handles = users.stream()
                .map(User::getBojId)
                .map(String::trim) // 공백 제거
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl("https://solved.ac/api/v3/user/lookup")
                .queryParam("handles", handles)
                .toUriString();

        SolvedAcResponseDto[] solvedAcUsers = restTemplate.getForObject(url, SolvedAcResponseDto[].class);

        if (solvedAcUsers == null) {
            return Collections.emptyList();
        }

        Map<String, SolvedAcResponseDto> infoMap = Arrays.stream(solvedAcUsers)
                .collect(Collectors.toMap(
                        dto -> dto.getHandle().toLowerCase(),
                        dto -> dto
                ));

        List<BojRankingResponseDto> rankingList = new ArrayList<>();

        // 유저 정보와 API 정보 합치기
        for (User user : users) {
            String dbBojId = user.getBojId().trim();
            SolvedAcResponseDto info = infoMap.get(dbBojId.toLowerCase());

            if (info != null) {
                rankingList.add(BojRankingResponseDto.builder()
                        .rank(0)
                        .username(user.getUsername())
                        .bojId(user.getBojId())
                        .rating(info.getRating())
                        .solvedCount(info.getSolvedCount())
                        .build());
            }
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