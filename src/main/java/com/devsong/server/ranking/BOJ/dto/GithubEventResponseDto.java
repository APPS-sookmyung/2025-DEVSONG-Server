package com.devsong.server.ranking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GithubEventResponseDto {

    private String type;
    private String created_at;
    private Payload payload;

    @Getter
    @NoArgsConstructor
    public static class Payload {

        @JsonProperty("commits")
        private List<Commit> commits;
    }

    @Getter
    @NoArgsConstructor
    public static class Commit {
        private String sha;
    }
}
