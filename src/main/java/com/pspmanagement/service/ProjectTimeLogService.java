package com.pspmanagement.service;


public interface ProjectTimeLogService {
    Boolean startProject(Long projectId);
    Boolean endProject(Long projectId);
    String getProjectDuration(Long projectId);
}
