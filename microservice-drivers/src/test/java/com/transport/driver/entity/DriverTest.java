package com.transport.driver.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {

    @Test
    void testDriver() {
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setName("Driver");
        driver.setLicenseNumber("test");
        driver.setActive(true);
        assertNotNull(driver.getId());
        assertNotNull(driver.getName());
        assertNotNull(driver.getLicenseNumber());
        assertTrue(driver.isActive());
    }
}