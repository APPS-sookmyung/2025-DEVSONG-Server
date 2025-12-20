package com.devsong.server.ranking.config; // 패키지 위치에 맞게 수정

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class RankingConfig {

    // 랭킹(BOJ, GitHub) 조회에 사용할 RestTemplate 등록
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}