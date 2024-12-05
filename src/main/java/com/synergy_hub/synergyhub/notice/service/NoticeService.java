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
            NoticeRepository noticeRepository,
            TeamRepository teamRepository,
            MemberRepository memberRepository,
            S3ImageService s3ImageService) {
        this.noticeRepository = noticeRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.s3ImageService = s3ImageService;
    }

    @Transactional
    public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Team team = teamRepository.findById(requestDTO.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        String imageUrl = null;
        if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
            imageUrl = s3ImageService.upload(requestDTO.getImage());
        }

        Notice notice = Notice.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .member(member)
                .team(team)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    @Transactional
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        String newImageUrl = null;
        if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
            if (notice.getImageUrl() != null) {
                s3ImageService.deleteImageFromS3(notice.getImageUrl());
            }
            newImageUrl = s3ImageService.upload(requestDTO.getImage());
        }

        notice.setTitle(requestDTO.getTitle());
        notice.setContent(requestDTO.getContent());
        notice.setImageUrl(newImageUrl != null ? newImageUrl : notice.getImageUrl());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (notice.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(notice.getImageUrl());
        }

        notice.setDeletedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    public List<NoticeResponseDTO> getAllNotices() {
        return noticeRepository.findAllByDeletedAtIsNull().stream()
                .map(NoticeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public NoticeResponseDTO getNotice(Long id) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return NoticeResponseDTO.fromEntity(notice);
    }
}
