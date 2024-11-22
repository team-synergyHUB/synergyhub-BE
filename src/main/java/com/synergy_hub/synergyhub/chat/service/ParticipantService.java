//package com.synergy_hub.synergyhub.chat.service;
//
//import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
//import com.synergy_hub.synergyhub.chat.entity.Participant;
//import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
//import com.synergy_hub.synergyhub.chat.repository.MemberRepository;
//import com.synergy_hub.synergyhub.chat.repository.ParticipantRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class ParticipantService {
//
//    private final ParticipantRepository participantRepository;
//    private final ChatRoomRepository chatRoomRepository;
//    private final MemberRepository memberRepository;
//
//    @Autowired
//    public ParticipantService(
//            ParticipantRepository participantRepository,
//            ChatRoomRepository chatRoomRepository,
//            MemberRepository memberRepository
//    ) {
//        this.participantRepository = participantRepository;
//        this.chatRoomRepository = chatRoomRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    public List<Map<String, Object>> getParticipants(Long chatRoomId) {
//        return participantRepository.findByChatRoom_ChatRoomId(chatRoomId)
//                .stream()
//                .map(participant -> Map.of(
//                        "memberId", participant.getMemberId(),
//                        "nickname", getNicknameByMemberId(participant.getMemberId()),
//                        "email", getEmailByMemberId(participant.getMemberId())
//                ))
//                .collect(Collectors.toList());
//    }
//
//    private String getNicknameByMemberId(Long memberId) {
//        return memberRepository.findById(memberId)
//                .map(member -> member.getNickname())
//                .orElse("Unknown");
//    }
//
//    private String getEmailByMemberId(Long memberId) {
//        return memberRepository.findById(memberId)
//                .map(member -> member.getEmail())
//                .orElse("Unknown");
//    }
//}
