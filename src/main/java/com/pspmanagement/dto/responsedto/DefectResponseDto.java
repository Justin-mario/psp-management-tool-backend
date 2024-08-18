package com.pspmanagement.dto.responsedto;

import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.constant.DefectType;
import com.pspmanagement.model.constant.ProjectPhase;
import com.pspmanagement.model.entity.Defect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefectResponseDto {
    private Long defectId;

    private String description;

    private DefectStatus defectStatus;

    private DefectType defectType;

    private ProjectPhase injectedPhase;

    private ProjectPhase removedPhase;

    private LocalDateTime fixTime;

    public DefectResponseDto(Defect defect) {
        this.defectId = defect.getDefectId();
        this.description = defect.getDescription();
        this.defectStatus = defect.getDefectStatus();
        this.defectType = defect.getDefectType();
        this.injectedPhase = defect.getInjectedPhase();
        this.removedPhase = defect.getRemovedPhase();
        this.fixTime = defect.getFixTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefectResponseDto that = (DefectResponseDto) o;
        return Objects.equals(defectId, that.defectId) && Objects.equals(description, that.description) && defectStatus == that.defectStatus && defectType == that.defectType && injectedPhase == that.injectedPhase && removedPhase == that.removedPhase && Objects.equals(fixTime, that.fixTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defectId, description, defectStatus, defectType, injectedPhase, removedPhase, fixTime);
    }

    @Override
    public String toString() {
        return "DefectResponseDto{" +
                "defectId=" + defectId +
                ", description='" + description + '\'' +
                ", defectStatus=" + defectStatus +
                ", defectType=" + defectType +
                ", injectedPhase=" + injectedPhase +
                ", removedPhase=" + removedPhase +
                ", fixTime=" + fixTime +
                '}';
    }
}
