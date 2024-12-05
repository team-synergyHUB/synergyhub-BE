package com.synergy_hub.synergyhub.notice.entity;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.entity.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;  //제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 내용

//    @Column(nullable = true)
//    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 작성자 (사용자 엔티티와 연결)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // 공지가 속한 팀 (팀 엔티티와 연결)

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 작성일

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    @Column(nullable = true)
    private LocalDateTime deletedAt;  // 삭제 ????



    //공지사항 생성 메서드
    public static Notice createNotice(String title, String content, Member member, Team team) {
        return Notice.builder()
            .title(title)
            .content(content)
            .member(member)
            .team(team)
//            .imageUrl(imageUrl)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
    //공지사항 수정 메서드
    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
//        this.imageUrl=imageUrl;
        this.updatedAt = LocalDateTime.now(); // 수정일 업데이트
    }

    // 소프트 딜리트 처리 메서드
    public void softDelete() {
        this.deletedAt = LocalDateTime.now(); // 삭제일 기록
    }
    //소프트 딜리트 여부 확인 메서드
    public boolean isDeleted(){
        return this.deletedAt !=null;
    }
}