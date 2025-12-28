package com.devsong.server.ranking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GithubEventResponseDto {

    private String type;           // PushEvent, PullRequestEvent 등
    private String created_at;     // 이벤트 발생 시각
    private Payload payload;

    @Getter
    @NoArgsConstructor
    public static class Payload {
        private List<Commit> commits;
    }

    @Getter
    @NoArgsConstructor
    public static class Commit {
        private String sha;
    }
}

