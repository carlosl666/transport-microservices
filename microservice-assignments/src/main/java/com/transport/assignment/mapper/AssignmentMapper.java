package com.transport.assignment.mapper;

import com.transport.assignment.dto.AssignmentResponse;
import com.transport.assignment.entity.Assignment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    AssignmentResponse toResponse(Assignment assignment);

    List<AssignmentResponse> toResponseList(List<Assignment> assignments);
}
