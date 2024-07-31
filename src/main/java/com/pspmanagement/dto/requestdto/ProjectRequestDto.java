package com.pspmanagement.dto.requestdto;

import com.pspmanagement.model.constant.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequestDto {
    private Long projectId;

    private String projectName;

    private String jwtToken;

    private String projectDeveloper;

    private String projectStatus;
}
