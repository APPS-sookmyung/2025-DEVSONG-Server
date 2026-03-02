package com.devsong.server.user.service;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.Resume;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    //이력서 조회
    @Transactional(readOnly = true)
    public ResumeResponseDto getResume(Long userId) {

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        User user = resume.getUser();

        return ResumeResponseDto.builder()
                //.profileImage(resume.getProfileImage())
                .profileImage(user.getProfileImageUrl())
                .username(user.getUsername())
                .studentId(user.getStudentId())
                .major(user.getMajor())
                .bojId(user.getBojId())
                .githubId(user.getGithubId())
                .techStack(user.getTechStack())
                .interests(resume.getInterests())
                .content(resume.getContent())
                .build();
    }

    //이력서 수정
    @Transactional
    public UpdateResumeResponseDto updateResume(Long userId, UpdateResumeRequestDto dto) {

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        resume.getUser().updateBojId(dto.getBojId());

        resume.getUser().updateGithubId(dto.getGithubId());

        resume.update(
                dto.getInterests(),
                dto.getContent(),
                dto.getProfileImage()
        );

        if (dto.getTechStack() != null) {
            resume.getUser().updateTechStack(dto.getTechStack());
        }

        return UpdateResumeResponseDto.builder()
                .message("Update Success")
                .build();
    }

    //특정 유저 이력서 조회
    @Transactional
    public ResumeResponseDto getUserResume(Long userId) {

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        User user = resume.getUser();

        return ResumeResponseDto.builder()
                .profileImage(resume.getProfileImage())
                .username(user.getUsername())
                .studentId(user.getStudentId())
                .major(user.getMajor())
                .bojId(user.getBojId())
                .githubId(user.getGithubId())
                .techStack(user.getTechStack())
                .interests(resume.getInterests())
                .content(resume.getContent())
                .build();
    }
}