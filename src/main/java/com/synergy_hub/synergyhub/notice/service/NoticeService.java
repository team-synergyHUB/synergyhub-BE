package com.synergy_hub.synergyhub.notice.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.notice.dto.NoticeRequestDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
import com.synergy_hub.synergyhub.notice.entity.Notice;
import com.synergy_hub.synergyhub.notice.repository.NoticeRepository;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public NoticeService(
            NoticeRepository noticeRepository,
            TeamRepository teamRepository,
            MemberRepository memberRepository) {
        this.noticeRepository = noticeRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }

    // 공지사항 생성
    public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO, Member currentUser) {
        Team team = teamRepository.findById(requestDTO.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 공지사항 생성 로직
        Notice notice = Notice.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl())
                .team(team)
                .member(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 공지사항 수정
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO, Member currentUser) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 작성자 권한 확인
        if (!notice.getMember().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        // 수정 로직
        notice.setTitle(requestDTO.getTitle());
        notice.setContent(requestDTO.getContent());
        notice.setImageUrl(requestDTO.getImageUrl());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 공지사항 삭제
    public void deleteNotice(Long id, Member currentUser) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 작성자 권한 확인
        if (!notice.getMember().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        // 소프트 삭제 처리
        notice.setDeletedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    // 팀별 공지사항 조회 (페이지네이션 및 정렬)
    public Page<NoticeResponseDTO> getNoticesByTeam(Long teamId, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return noticeRepository.findByTeamIdAndDeletedAtIsNull(teamId, pageable)
                .map(NoticeResponseDTO::fromEntity);
    }

    // 특정 공지사항 조회
    public NoticeResponseDTO getNotice(Long id) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return NoticeResponseDTO.fromEntity(notice);
    }
}
