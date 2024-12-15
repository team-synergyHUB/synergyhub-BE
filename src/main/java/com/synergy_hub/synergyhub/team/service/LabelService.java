package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.LabelRequestDTO;
import com.synergy_hub.synergyhub.team.dto.LabelResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import com.synergy_hub.synergyhub.team.repository.TeamLabelRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private final TeamLabelRepository teamLabelRepository;

    public LabelService(LabelRepository labelRepository, TeamLabelRepository teamLabelRepository) {
        this.labelRepository = labelRepository;
        this.teamLabelRepository = teamLabelRepository;
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
    @Transactional
    public void deleteLabel(Long labelId) {
        log.info("Deleting label {}",labelId);
        Label label = labelRepository.findById(labelId)

                .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));

        // 팀-라벨 매핑 데이터 삭제
        teamLabelRepository.deleteByLabelId(labelId);

        // 라벨 삭제
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
