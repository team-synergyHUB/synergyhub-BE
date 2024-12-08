package com.synergy_hub.synergyhub.member.repository.query;

import com.synergy_hub.synergyhub.member.dto.TeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    //ID로 회원 조회
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    //이메일로 회원 조회
    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    //탈퇴하지 않은 모든 회원 조회
    List<Member> findAllByDeletedAtIsNull();

    //특정 팀에 속한 회원 목록 조회
    Page<TeamMemberResponseDto> findMembersByTeam(Long teamId, Pageable pageable);

    //닉네임과 이메일로 회원 조회
    Optional<Member> findByNicknameAndEmailDeletedAtIsNull(String nickname, String email);

}
