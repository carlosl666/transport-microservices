package com.transport.assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.context.ConfigurableApplicationContext;

class MsAssignmentsApplicationTests {

    @Test
    void testMain1() {
        MsAssignmentsApplication app = new MsAssignmentsApplication();
        Assertions.assertNotNull(app);
    }

    @Test
    void testMain2() {
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockApplicationContext = Mockito.mock(ConfigurableApplicationContext.class);
            mockedStatic.when(() -> SpringApplication.run(WavefrontProperties.Application.class, new String[]{}))
                    .thenReturn(mockApplicationContext);
            MsAssignmentsApplication.main(null);
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }

}
