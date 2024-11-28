package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.calendar.repository.CalendarRepository;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.TeamCreateResponseDTO;
import com.synergy_hub.synergyhub.team.dto.TeamRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final LabelRepository labelRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final CalendarRepository calendarRepository;
    private final ChatRoomRepository chatRoomRepository;

    public TeamService(TeamRepository teamRepository, LabelRepository labelRepository,
                       MemberTeamRepository memberTeamRepository, CalendarRepository calendarRepository,
                       ChatRoomRepository chatRoomRepository) {
        this.teamRepository = teamRepository;
        this.labelRepository = labelRepository;
        this.memberTeamRepository = memberTeamRepository;
        this.calendarRepository = calendarRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

//    public String generateUniqueInviteCode() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        String code;
//        do {
//            StringBuilder builder = new StringBuilder();
//            for (int i = 0; i < 12; i++) {
//                int index = (int) (Math.random() * characters.length());
//                builder.append(characters.charAt(index));
//            }
//            code = builder.toString();
//        } while (isInviteCodeDuplicate(code)); // 중복 확인 로직
//        return code;
//    }
//
//    private boolean isInviteCodeDuplicate(String code) {
//        return teamRepository.existsByInviteCode(code);
//    }

    // 팀 생성
    @Transactional
    public TeamCreateResponseDTO createTeam(TeamRequestDTO request) {
        Label label = null;

        // 깃랩 label처럼 구현 계획
        if (request.getLabelId() != null) {
            label = labelRepository.findById(request.getLabelId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));
        }

//        Team team = Team.builder()
//                .name(request.getName())
//                .inviteCode(request.getInviteCode())
////                .inviteSecret(request.getInviteSecret())
//                .label(label)
//                .isDeleted(false) // 명시적으로 기본값 설정
//                .build();
//
//        Team savedTeam = teamRepository.save(team);

        // 팀 생성 및 저장 (초대 코드는 Team 엔티티에서 자동 생성됨)
        Team team = Team.builder()
                .name(request.getName())
                .label(label)
                .isDeleted(false) // 명시적으로 기본값 설정
                .build();

        Team savedTeam = teamRepository.save(team);

        // 캘린더 생성 및 저장
        Calendar calendar = Calendar.builder()
                .team(savedTeam) // 팀과 매핑
                .build();
        Calendar savedCalendar = calendarRepository.save(calendar);

        // 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .team(savedTeam) // 팀과 매핑
                .roomName("Default Chat Room") // 필요 시 수정 가능
                .roomState("ACTIVE")          // 필요 시 수정 가능
                .createdAt(LocalDateTime.now()) // 명시적으로 값 설정
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // TeamResponseDTO 반환
        return new TeamCreateResponseDTO(savedTeam, savedCalendar, savedChatRoom);
    }


    // 팀 수정
    public TeamResponseDTO updateTeam(Long teamId, TeamRequestDTO request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Label label = null;
        if (request.getLabelId() != null) {
            label = labelRepository.findById(request.getLabelId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));
        }

        team.updateTeam(request.getName(), label);

        return new TeamResponseDTO(team);
    }

    // 팀 삭제 : 팀에서 모든 멤버가 나가면 팀 삭제
    public void leaveTeam(Long memberId, Long teamId) {
        // 해당 멤버와 팀의 연결 정보(MemberTeam) 찾기
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TEAM_NOT_FOUND));

        // 팀에 남아 있는 멤버가 있는지 확인
        boolean hasRemainingMembers = memberTeamRepository.existsByTeamId(teamId);

        // 팀원이 없으면 팀 삭제
        if (!hasRemainingMembers) {
            Team team = memberTeam.getTeam();
            team.markAsDeleted();
            teamRepository.save(team);
        }
    }

    // 팀 조회
//    public List<TeamResponseDTO> getAllTeams() {
//        return teamRepository.findAllByIsDeleted(false)
//                .stream()
//                .map(TeamResponseDTO::new)
//                .collect(Collectors.toList());
//    }

    // 팀 조회 with 페이지네이션
//    public Page<TeamResponseDTO> getAllTeams(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size); // 페이지네이션 정보 생성 (페이지 번호, 데이터 개수)
//
//        return teamRepository.findAllByIsDeleted(false, pageable)
//                .map(TeamResponseDTO::new); // Page 객체에 map 메서드를 사용해 DTO 변환
//    }

    // 팀 조회 with 페이지네이션
    public Page<TeamResponseDTO> getAllTeams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending()); // 페이지 정보 생성
        return teamRepository.findAllByIsDeleted(false, pageable)
                .map(TeamResponseDTO::new); // Page 객체를 DTO로 변환
    }
}
