package com.transport.assignment.service;

import com.transport.assignment.dto.AssignmentRequest;
import com.transport.assignment.dto.AssignmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {
    AssignmentResponse assignDriver(UUID orderId, AssignmentRequest request);

    AssignmentResponse addPdf(UUID assignmentId, MultipartFile file);

    AssignmentResponse addImage(UUID assignmentId, MultipartFile file);

    List<AssignmentResponse> findAll();
}
