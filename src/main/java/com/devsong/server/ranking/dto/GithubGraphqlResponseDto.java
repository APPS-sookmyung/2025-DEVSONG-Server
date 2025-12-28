package com.devsong.server.ranking.dto;

import lombok.Getter;

@Getter
public class GithubGraphqlResponseDto {

    private Data data;

    @Getter
    public static class Data {
        private User user;
    }

    @Getter
    public static class User {
        private ContributionsCollection contributionsCollection;
    }

    @Getter
    public static class ContributionsCollection {
        private int totalCommitContributions;
    }
}

