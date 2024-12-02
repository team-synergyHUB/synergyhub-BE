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

//@RestController
//@RequestMapping("/api/labels")
//@RequiredArgsConstructor
//@Tag(name = "Label API", description = "라벨 관리 API") // Swagger 태그
//public class LabelController {
//
//    private final LabelService labelService;
//
//    // 라벨 생성
//    @Operation(summary = "라벨 생성", description = "새로운 라벨을 생성합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "라벨 생성 성공",
//                    content = @Content(schema = @Schema(implementation = LabelResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PostMapping
//    public ResponseEntity<LabelResponseDTO> createLabel(
//            @Valid @RequestBody LabelRequestDTO labelDTO) {
//        LabelResponseDTO createdLabel = labelService.createLabel(labelDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
//    }
//
//    // 라벨 수정
//    @Operation(summary = "라벨 수정", description = "기존 라벨의 정보를 수정합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "라벨 수정 성공",
//                    content = @Content(schema = @Schema(implementation = LabelResponseDTO.class))),
//            @ApiResponse(responseCode = "404", description = "라벨을 찾을 수 없음"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청")
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<LabelResponseDTO> updateLabel(
//            @PathVariable Long id,
//            @Valid @RequestBody LabelRequestDTO labelDTO) {
//        LabelResponseDTO updatedLabel = labelService.updateLabel(id, labelDTO);
//        return ResponseEntity.ok(updatedLabel);
//    }
//
//    // 라벨 삭제
//    @Operation(summary = "라벨 삭제", description = "특정 라벨을 삭제합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "라벨 삭제 성공"),
//            @ApiResponse(responseCode = "404", description = "라벨을 찾을 수 없음")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLabel(
//            @PathVariable Long id) {
//        labelService.deleteLabel(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // 팀 ID로 라벨 조회
//    @Operation(summary = "팀별 라벨 조회", description = "특정 팀에 속한 라벨을 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "라벨 조회 성공",
//                    content = @Content(schema = @Schema(implementation = LabelResponseDTO.class)))
//    })
//    @GetMapping("/team/{teamId}")
//    public ResponseEntity<List<LabelResponseDTO>> getLabelsByTeamId(@PathVariable Long teamId) {
//        List<LabelResponseDTO> labels = labelService.getLabelsByTeamId(teamId);
//        return ResponseEntity.ok(labels);
//    }
//
//    // 모든 라벨 조회
//    @Operation(summary = "라벨 조회", description = "모든 라벨을 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "라벨 조회 성공",
//                    content = @Content(schema = @Schema(implementation = LabelResponseDTO.class)))
//    })
//    @GetMapping
//    public ResponseEntity<List<LabelResponseDTO>> getAllLabels() {
//        List<LabelResponseDTO> labels = labelService.getAllLabels();
//        return ResponseEntity.ok(labels);
//    }
//}

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