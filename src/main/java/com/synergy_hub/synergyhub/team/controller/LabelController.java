package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.team.dto.LabelDTO;
import com.synergy_hub.synergyhub.team.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
@Tag(name = "Label API", description = "라벨 관리 API") // Swagger 태그
public class LabelController {

    private final LabelService labelService;

    // 라벨 생성
    @Operation(summary = "라벨 생성", description = "새로운 라벨을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "라벨 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<LabelDTO> createLabel(
            @Valid @RequestBody LabelDTO labelDTO) {
        LabelDTO createdLabel = labelService.createLabel(labelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }

    // 라벨 수정
    @Operation(summary = "라벨 수정", description = "기존 라벨의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "라벨 수정 성공"),
            @ApiResponse(responseCode = "404", description = "라벨을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> updateLabel(
            @PathVariable Long id,
            @Valid @RequestBody LabelDTO labelDTO) {
        LabelDTO updatedLabel = labelService.updateLabel(id, labelDTO);
        return ResponseEntity.ok(updatedLabel);
    }

    // 라벨 삭제
    @Operation(summary = "라벨 삭제", description = "특정 라벨을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "라벨 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "라벨을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(
            @PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }

    // 모든 라벨 조회
    @Operation(summary = "라벨 조회", description = "모든 라벨을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "라벨 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        List<LabelDTO> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }
}
