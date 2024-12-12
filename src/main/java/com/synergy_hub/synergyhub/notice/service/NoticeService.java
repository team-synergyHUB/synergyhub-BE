package com.synergy_hub.synergyhub.notice.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.notice.dto.NoticeCreateRequestDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeUpdateRequestDTO;
import com.synergy_hub.synergyhub.notice.entity.Notice;
import com.synergy_hub.synergyhub.notice.repository.NoticeRepository;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;

import java.time.LocalDateTime;

import com.synergy_hub.synergyhub.team.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamService teamService;

    public NoticeService(
            NoticeRepository noticeRepository,
            TeamRepository teamRepository,
            MemberRepository memberRepository,
            TeamService teamService) {
        this.noticeRepository = noticeRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.teamService = teamService;
    }

    // 공지사항 생성
    public NoticeResponseDTO createNotice(NoticeCreateRequestDTO createRequestDTO, Member currentUser, Long teamId) {
        if (!teamService.teamAccessValidator(teamId, currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 공지사항 생성 로직
        Notice notice = Notice.builder()
                .title(createRequestDTO.getTitle())
                .content(createRequestDTO.getContent())
                .imageUrl(createRequestDTO.getImageUrl())
                .team(team)
                .member(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 공지사항 수정
    public NoticeResponseDTO updateNotice(Long id, NoticeUpdateRequestDTO updateRequestDTO, Member currentUser) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!teamService.teamAccessValidator(notice.getTeam().getId(), currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        if (!notice.getMember().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        notice.setTitle(updateRequestDTO.getTitle());
        notice.setContent(updateRequestDTO.getContent());
        notice.setImageUrl(updateRequestDTO.getImageUrl());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 공지사항 삭제
    public void deleteNotice(Long id, Member currentUser) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!teamService.teamAccessValidator(notice.getTeam().getId(), currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        if (!notice.getMember().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        notice.setDeletedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    // 팀별 공지사항 조회
    public Page<NoticeResponseDTO> getNoticesByTeam(Member currentUser, Long teamId, int page, int size, String sortField, String sortDirection) {
        if (!teamService.teamAccessValidator(teamId, currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return noticeRepository.findByTeamIdAndDeletedAtIsNull(teamId, pageable)
                .map(NoticeResponseDTO::fromEntity);
    }

    // 특정 공지사항 조회
    public NoticeResponseDTO getNotice(Long id, Member currentUser) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!teamService.teamAccessValidator(notice.getTeam().getId(), currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        return NoticeResponseDTO.fromEntity(notice);
    }
}
