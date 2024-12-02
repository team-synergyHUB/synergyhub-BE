package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.LabelRequestDTO;
import com.synergy_hub.synergyhub.team.dto.LabelResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    // 라벨 생성
    public LabelResponseDTO createLabel(LabelRequestDTO request) {
        Label label = new Label(request.getName(), request.getColor());
        Label savedLabel = labelRepository.save(label);
        return new LabelResponseDTO(savedLabel.getId(), savedLabel.getName(), savedLabel.getColor());
    }

    // 라벨 수정
    public LabelResponseDTO updateLabel(Long labelId, LabelRequestDTO request) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));

        label.update(request.getName(), request.getColor());
        Label updatedLabel = labelRepository.save(label);
        return new LabelResponseDTO(updatedLabel.getId(), updatedLabel.getName(), updatedLabel.getColor());
    }

    // 라벨 삭제
    public void deleteLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));

        labelRepository.delete(label);
    }

    // 팀 ID로 라벨 조회
    public List<LabelResponseDTO> getLabelsByTeamId(Long teamId) {
        List<Label> labels = labelRepository.findLabelsByTeamId(teamId);
        return labels.stream()
                .map(label -> new LabelResponseDTO(label.getId(), label.getName(), label.getColor()))
                .collect(Collectors.toList());
    }

    // 모든 라벨 조회
    public List<LabelResponseDTO> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(label -> new LabelResponseDTO(label.getId(), label.getName(), label.getColor()))
                .collect(Collectors.toList());
    }
}
