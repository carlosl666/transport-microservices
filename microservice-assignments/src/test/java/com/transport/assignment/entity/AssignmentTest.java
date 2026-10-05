package com.transport.assignment.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    @Test
    void testAssignment() {
        Assignment assignment = new Assignment();
        assignment.setId(UUID.randomUUID());
        assignment.setOrderId(UUID.randomUUID());
        assignment.setDriverId(UUID.randomUUID());
        assignment.setDriverName("TEST");
        assignment.setImageFilePath("test");
        assignment.setPdfFilePath("test");
        assertNotNull(assignment.getId());
        assertNotNull(assignment.getOrderId());
        assertNotNull(assignment.getDriverId());
        assertNotNull(assignment.getDriverName());
        assertNotNull(assignment.getImageFilePath());
        assertNotNull(assignment.getPdfFilePath());
    }
}