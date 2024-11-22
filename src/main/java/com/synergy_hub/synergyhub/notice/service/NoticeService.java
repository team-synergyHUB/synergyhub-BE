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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public NoticeService(NoticeRepository noticeRepository, TeamRepository teamRepository, MemberRepository memberRepository) {
        this.noticeRepository = noticeRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;

    }

    //공지사항 생성
    public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Team team = teamRepository.findById(requestDTO.getTeamId())
            .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND)); // 예외 처리 변경

        Notice notice = Notice.createNotice(
            requestDTO.getTitle(),
            requestDTO.getContent(),
            member,
            team
            //requestDTO.getImage()
          );
        //공지사항 저장
        noticeRepository.save(notice);
        return NoticeResponseDTO.fromEntity(notice);
    }
    //공지사항 수정
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

    // 공지사항 업데이트
    notice.updateNotice(requestDTO.getTitle(), requestDTO.getContent());

    // 공지사항 저장
    noticeRepository.save(notice);
    return NoticeResponseDTO.fromEntity(notice);
    }
    //공지사항 삭제

}

