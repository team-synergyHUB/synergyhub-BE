package com.synergy_hub.synergyhub.notice.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.image.S3ImageService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;

    public NoticeService(
        NoticeRepository noticeRepository, TeamRepository teamRepository,
        MemberRepository memberRepository, S3ImageService s3ImageService) {

        this.noticeRepository = noticeRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.s3ImageService = s3ImageService;
    }

    // 공지사항 생성
    @Transactional
    public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Team team = teamRepository.findById(requestDTO.getTeamId())
            .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // S3에 이미지 업로드
        String imageUrl = null;
        if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
            imageUrl = s3ImageService.upload(requestDTO.getImage());
        }

        Notice notice = Notice.createNotice(
            requestDTO.getTitle(),
            requestDTO.getContent(),
            member,
            team,
            imageUrl
        );
        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 공지사항 수정
    @Transactional
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO) {
        String newImageUrl = null;
        if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
            newImageUrl = s3ImageService.upload(requestDTO.getImage());
        }

        int updatedCount = noticeRepository.updateNotice(
            id, requestDTO.getTitle(), requestDTO.getContent(), newImageUrl
        );

        if (updatedCount == 0) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        return new NoticeResponseDTO(
            id,
            requestDTO.getTitle(),
            requestDTO.getContent(),
            requestDTO.getMemberId(),
            requestDTO.getMemberNickname(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (notice.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(notice.getImageUrl());
        }

        notice.softDelete();
        noticeRepository.save(notice);
    }

    // 모든 공지사항 조회
    public List<NoticeResponseDTO> getAllNotices() {
        return noticeRepository.findAllByDeletedAtIsNull().stream()
            .map(NoticeResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    // 특정 공지사항 조회
    public NoticeResponseDTO getNotice(Long id) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return NoticeResponseDTO.fromEntity(notice);
    }

    // 특정 팀의 공지사항 조회
    public List<NoticeResponseDTO> getNoticesByTeamId(Long teamId) {
        List<Notice> notices = noticeRepository.findByTeamIdAndDeletedAtIsNull(teamId);
        return notices.stream()
            .map(NoticeResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
