//package com.synergy_hub.synergyhub.chat.controller;
//
//import com.synergy_hub.synergyhub.chat.service.ParticipantService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/chatrooms/{chatRoomId}/participants")
//public class ParticipantController {
//
//    private final ParticipantService participantService;
//
//    @Autowired
//    public ParticipantController(ParticipantService participantService) {
//        this.participantService = participantService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Map<String, Object>>> getParticipants(@PathVariable Long chatRoomId) {
//        List<Map<String, Object>> participants = participantService.getParticipants(chatRoomId);
//        return ResponseEntity.ok(participants);
//    }
//
//    @PostMapping
//    public ResponseEntity<String> addParticipant(
//            @PathVariable Long chatRoomId,
//            @RequestBody Map<String, Long> request) {
//        Long memberId = request.get("member_id");
//        participantService.addParticipant(chatRoomId, memberId);
//        return ResponseEntity.ok("User added to chatroom successfully");
//    }
//
//    @DeleteMapping("/{memberId}")
//    public ResponseEntity<String> removeParticipant(
//            @PathVariable Long chatRoomId,
//            @PathVariable Long memberId) {
//        participantService.removeParticipant(chatRoomId, memberId);
//        return ResponseEntity.ok("User removed from chatroom successfully");
//    }
//}
