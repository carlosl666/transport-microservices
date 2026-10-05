package com.transport.assignment.repository;

import com.transport.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    boolean existsByOrderId(UUID orderId);
}
