package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.team.dto.LabelRequestDTO;
import com.synergy_hub.synergyhub.team.dto.LabelResponseDTO;
import com.synergy_hub.synergyhub.team.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @CommonApiDocs(summary = "라벨 생성", description = "새로운 라벨을 생성합니다.")
    @PostMapping
    public ResponseEntity<LabelResponseDTO> createLabel(@Valid @RequestBody LabelRequestDTO labelDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.createLabel(labelDTO));
    }

    // 라벨 수정
    @CommonApiDocs(summary = "라벨 수정", description = "기존 라벨의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<LabelResponseDTO> updateLabel(
            @PathVariable Long id,
            @Valid @RequestBody LabelRequestDTO labelDTO) {
        return ResponseEntity.ok(labelService.updateLabel(id, labelDTO));
    }

    // 라벨 삭제
    @CommonApiDocs(summary = "라벨 삭제", description = "특정 라벨을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }

    // 팀 ID로 라벨 조회
    @CommonApiDocs(summary = "팀별 라벨 조회", description = "특정 팀에 속한 라벨을 조회합니다.")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<LabelResponseDTO>> getLabelsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(labelService.getLabelsByTeamId(teamId));
    }

    // 모든 라벨 조회
    @CommonApiDocs(summary = "라벨 조회", description = "모든 라벨을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LabelResponseDTO>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }
}