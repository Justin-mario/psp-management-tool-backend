package com.pspmanagement.dto.requestdto;

import com.pspmanagement.model.constant.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRegistrationRequestDto {
    private Long projectId;

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project description is required")
    private String projectDescription;


    @NotBlank(message = "Project developer name is required")
    private String projectDeveloper;

    @NotBlank(message = "Programming language is required")
    private String programmingLanguage;

    private ProjectStatus projectStatus;



}
