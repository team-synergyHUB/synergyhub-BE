package com.synergy_hub.synergyhub.notice.controller;

import com.synergy_hub.synergyhub.notice.dto.NoticeRequestDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
import com.synergy_hub.synergyhub.notice.service.NoticeService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지사항 생성
    @PostMapping
    public ResponseEntity<NoticeResponseDTO> createNotice(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("memberId") Long memberId,
        @RequestParam("teamId") Long teamId,
        @RequestParam(value = "image", required = false) MultipartFile image) {

        // Create DTO
        NoticeRequestDTO requestDTO = new NoticeRequestDTO();
        requestDTO.setTitle(title);
        requestDTO.setContent(content);
        requestDTO.setMemberId(memberId);
        requestDTO.setTeamId(teamId);
        requestDTO.setImage(image);

        NoticeResponseDTO response = noticeService.createNotice(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 공지사항 수정
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
        @PathVariable Long id,
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("memberId") Long memberId,
        @RequestParam("teamId") Long teamId,
        @RequestParam(value = "image", required = false) MultipartFile image) {

        // Create DTO
        NoticeRequestDTO requestDTO = new NoticeRequestDTO();
        requestDTO.setTitle(title);
        requestDTO.setContent(content);
        requestDTO.setMemberId(memberId);
        requestDTO.setTeamId(teamId);
        requestDTO.setImage(image);

        NoticeResponseDTO response = noticeService.updateNotice(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    // 모든 공지사항 조회
    @GetMapping
    public ResponseEntity<List<NoticeResponseDTO>> getAllNotices() {
        List<NoticeResponseDTO> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    // 특정 공지사항 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long id) {
        NoticeResponseDTO response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }
}
