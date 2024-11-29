package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.LabelDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import jakarta.transaction.Transactional;
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
    public LabelDTO createLabel(LabelDTO request) {
        Label label = new Label(request.getName(), request.getColor());
        Label savedLabel = labelRepository.save(label);
        return new LabelDTO(savedLabel);
    }

    // 라벨 수정
    public LabelDTO updateLabel(Long labelId, LabelDTO request) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));

        label.update(request.getName(), request.getColor());
        return new LabelDTO(labelRepository.save(label));
    }

    // 라벨 삭제
    public void deleteLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));

        labelRepository.delete(label);
    }

    public List<LabelDTO> getLabelsByTeamId(Long teamId) {
        List<Label> labels = labelRepository.findLabelsByTeamId(teamId);
        return labels.stream()
                .map(LabelDTO::new)
                .collect(Collectors.toList());
    }

    // 라벨 조회
    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(LabelDTO::new)
                .collect(Collectors.toList());
    }
}
