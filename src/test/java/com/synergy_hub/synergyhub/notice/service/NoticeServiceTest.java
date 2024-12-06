//package com.synergy_hub.synergyhub.notice.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//import com.synergy_hub.synergyhub.member.entity.Member;
//import com.synergy_hub.synergyhub.member.entity.MemberRole;
//import com.synergy_hub.synergyhub.notice.dto.NoticeCreateRequestDTO;
//import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
//import com.synergy_hub.synergyhub.notice.entity.Notice;
//
//import com.synergy_hub.synergyhub.notice.repository.NoticeRepository;
//import com.synergy_hub.synergyhub.notice.service.NoticeService;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//class NoticeServiceTest {
//
//    @InjectMocks
//    private NoticeService noticeService; // 테스트 대상
//
//    @Mock
//    private NoticeRepository noticeRepository; // Mock 객체
//
//    NoticeServiceTest() {
//        MockitoAnnotations.openMocks(this); // Mock 초기화
//    }
//
//    @Test
//    void updateNotice_success() {
//        // Arrange
//        Long noticeId = 1L;
//        NoticeCreateRequestDTO requestDTO = new NoticeCreateRequestDTO();
//        requestDTO.setTitle("Updated Title");
//        requestDTO.setContent("Updated Content");
//        requestDTO.setImageUrl("http://example.com/updated-image.jpg");
//
//        // Mock Member 데이터 생성
//        Member mockMember = Member.createMember("MockUser", "mockuser@example.com", "password123");
//        mockMember.updateProfileImage("http://example.com/mock-profile.jpg");
//        mockMember.changeRole(MemberRole.USER);
//
//        // Mock Notice 데이터 생성
//        Notice existingNotice = new Notice();
//        existingNotice.setId(noticeId);
//        existingNotice.setTitle("Old Title");
//        existingNotice.setContent("Old Content");
//        existingNotice.setImageUrl("http://example.com/old-image.jpg");
//        existingNotice.setMember(mockMember);
//
//        Notice updatedNotice = new Notice();
//        updatedNotice.setId(noticeId);
//        updatedNotice.setTitle("Updated Title");
//        updatedNotice.setContent("Updated Content");
//        updatedNotice.setImageUrl("http://example.com/updated-image.jpg");
//        updatedNotice.setMember(mockMember);
//
//        // Mock 동작 정의
//        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
//        when(noticeRepository.save(existingNotice)).thenReturn(updatedNotice);
//
//        // Act
//        NoticeResponseDTO result = noticeService.updateNotice(noticeId, requestDTO);
//
//        // Assert
//        assertThat(result.getTitle()).isEqualTo("Updated Title");
//        assertThat(result.getContent()).isEqualTo("Updated Content");
//        assertThat(result.getImageUrl()).isEqualTo("http://example.com/updated-image.jpg");
//        assertThat(result.getMemberNickname()).isEqualTo("MockUser");
//
//        verify(noticeRepository).findById(noticeId);
//        verify(noticeRepository).save(existingNotice);
//    }
////    @Test
////    void updateNotice_notFound() {
////        // Arrange
////        Long noticeId = 1L;
////        NoticeCreateRequestDTO requestDTO = new NoticeCreateRequestDTO();
////        requestDTO.setTitle("Updated Title");
////        requestDTO.setContent("Updated Content");
////        requestDTO.setImageUrl("http://example.com/updated-image.jpg");
////
////        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());
////
////        // Act & Assert
////        CustomException exception = assertThrows(CustomException.class,
////                () -> noticeService.updateNotice(noticeId, requestDTO));
////
////        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
////
////        verify(noticeRepository).findById(noticeId);
////        verify(noticeRepository, never()).save(any());
////    }
//}
