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
import java.util.ArrayList;
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

    @Transactional
    public void mapLabelsToTeam(Long teamId, List<Long> labelIds) {
        // 1. 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 라벨 조회
        List<Label> labels = labelRepository.findAllById(labelIds);
        if (labels.isEmpty()) {
            throw new CustomException(ErrorCode.LABEL_NOT_FOUND);
        }

        // 3. 팀과 라벨 매핑 (양방향 관계 설정)
        for (Label label : labels) {
            team.addLabel(label);
            label.addTeam(team); // 필요시 양방향 관계
        }

        // 4. 매핑 데이터 저장
        teamRepository.save(team);
    }


    // 팀 생성
    @Transactional
    public TeamCreateResponseDTO createTeam(TeamRequestDTO request) {
        // 1. 라벨 리스트 생성
        List<Label> labels = new ArrayList<>();
        if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
            labels = labelRepository.findAllById(request.getLabelIds());
            if (labels.isEmpty()) {
                throw new CustomException(ErrorCode.LABEL_NOT_FOUND); // 라벨이 없을 경우 예외 처리
            }
        }

        // 2. 팀 생성 및 저장
        Team team = Team.builder()
                .name(request.getName())
                .labels(labels) // 라벨 리스트 추가
                .isDeleted(false) // 명시적으로 기본값 설정
                .build();
        Team savedTeam = teamRepository.save(team);

        // 3. 캘린더 생성 및 저장
        Calendar calendar = Calendar.builder()
                .team(savedTeam) // 팀과 매핑
                .build();
        Calendar savedCalendar = calendarRepository.save(calendar);

        // 4. 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .team(savedTeam) // 팀과 매핑
                .createdAt(LocalDateTime.now()) // 명시적으로 값 설정
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 5. TeamCreateResponseDTO 반환
        return new TeamCreateResponseDTO(savedTeam, savedCalendar, savedChatRoom);
    }

    @Transactional
    public TeamResponseDTO updateTeam(Long teamId, TeamRequestDTO request) {
        // 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 라벨 리스트 조회
        List<Label> labels = new ArrayList<>();
        if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
            labels = labelRepository.findAllById(request.getLabelIds());
            if (labels.isEmpty()) {
                throw new CustomException(ErrorCode.LABEL_NOT_FOUND);
            }
        }

        // 팀 정보 업데이트
        team.setName(request.getName()); // 팀 이름 업데이트
        team.getLabels().clear(); // 기존 라벨 제거
        team.getLabels().addAll(labels); // 새 라벨 추가

        // 업데이트된 팀 저장
        Team updatedTeam = teamRepository.save(team);

        return new TeamResponseDTO(updatedTeam);
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

    /**
     * 특정 멤버가 속한 팀 목록 조회
     * @param memberId 멤버 ID
     * @return 멤버가 속한 팀 목록 (DTO 형태로 반환)
     */
    public List<TeamResponseDTO> getTeamsByMember(Long memberId) {
        // 1. 해당 멤버가 속한 MemberTeam 목록 조회
        List<MemberTeam> memberTeams = memberTeamRepository.findAllByMemberId(memberId);

        // 2. MemberTeam에서 팀 엔티티 추출 후 DTO로 변환
        return memberTeams.stream()
                .map(memberTeam -> new TeamResponseDTO(memberTeam.getTeam()))
                .collect(Collectors.toList());
    }
}
