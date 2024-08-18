package com.pspmanagement.dto.requestdto;

import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.constant.DefectType;
import com.pspmanagement.model.constant.ProjectPhase;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
public class DefectRequestDto {
    private Long defectId;

    @Column(nullable = false)
    private String description;

    private DefectStatus defectStatus;

    private DefectType defectType;

    private ProjectPhase injectedPhase;

    private ProjectPhase removedPhase;

    private LocalDateTime fixTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectRequestDto that = (DefectRequestDto) o;
        return Objects.equals(defectId, that.defectId) && Objects.equals(description, that.description) && defectStatus == that.defectStatus && defectType == that.defectType && injectedPhase == that.injectedPhase && removedPhase == that.removedPhase && Objects.equals(fixTime, that.fixTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defectId, description, defectStatus, defectType, injectedPhase, removedPhase, fixTime);
    }
}
