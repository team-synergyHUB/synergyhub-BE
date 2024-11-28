package com.synergy_hub.synergyhub.team.entity;

import com.synergy_hub.synergyhub.calendar.repository.CalendarRepository;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.persistence.*;
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

//    @Column(nullable = false, length = 30)
//    private String inviteSecret; // 초대 비밀번호

    @Column(nullable = false)
    private Boolean isDeleted = false; // 삭제 여부

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @ManyToOne(optional = true) // 연관 관계에서 null 허용
    @JoinColumn(name = "label_id", nullable = true) // DB에서 nullable 허용
    private Label label;

    @PrePersist //엔티티가 처음으로 데이터베이스에 저장되기 전에 호출
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
//        } while (isInviteCodeDuplicate(code)); // 중복 확인 로직 추가
        } while (false); // 중복 확인 임시
        return code;
    }

//    private boolean isInviteCodeDuplicate(String code) {
//        return teamRepository.existsByInviteCode(code);
//    }


    public void markAsDeleted() {
        this.isDeleted = false;
    }

    public void updateTeam(String name, Label label) {
        this.name = name;
//        this.inviteCode = inviteCode;
//        this.inviteSecret = inviteSecret;
        this.label = label;
        this.isDeleted = false; // 명시적으로 기본값 설정
    }
}