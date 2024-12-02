package com.synergy_hub.synergyhub.team.entity;

import com.synergy_hub.synergyhub.calendar.repository.CalendarRepository;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name; // 팀 이름

    @Column(nullable = false, unique = true, length = 12) // 초대 코드는 유니크 설정
    private String inviteCode; // 초대 코드

    @Column(nullable = false)
    private Boolean isDeleted = false; // 삭제 여부

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeams = new ArrayList<>();

    // 다대다 관계 설정
    @ManyToMany
    @JoinTable(
            name = "team_label", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels = new ArrayList<>(); // 라벨 목록

    @OneToOne(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatRoom chatRoom; // 채팅방 관계 설정

    @PrePersist // 엔티티가 처음으로 데이터베이스에 저장되기 전에 호출
    private void generateInviteCode() {
        if (this.inviteCode == null || this.inviteCode.isEmpty()) {
            this.inviteCode = generateUniqueInviteCode();
        }
    }

    private String generateUniqueInviteCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String code;
        do {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                int index = (int) (Math.random() * characters.length());
                builder.append(characters.charAt(index));
            }
            code = builder.toString();
        } while (false); // 중복 확인 로직 임시 비활성화
        return code;
    }

    // 팀 삭제 상태 설정
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    // 팀 이름 및 라벨 목록 업데이트
    public void updateTeam(String name, List<Label> newLabels) {
        this.name = name; // 팀 이름 업데이트
        this.labels.clear(); // 기존 라벨 삭제
        this.labels.addAll(newLabels); // 새 라벨 추가
    }

    // 라벨 추가 메서드
    public void addLabel(Label label) {
        if (!this.labels.contains(label)) {
            this.labels.add(label);
        }
    }

    public void setName(@NotBlank(message = "팀 이름은 필수 입력 항목입니다.") String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("팀 이름은 비어 있을 수 없습니다.");
        }
        this.name = name;
    }

    // ChatRoom 반환 메서드
    public ChatRoom getChatRoom() {
        return this.chatRoom;
    }

    // ChatRoom 설정 메서드 (Optional)
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
