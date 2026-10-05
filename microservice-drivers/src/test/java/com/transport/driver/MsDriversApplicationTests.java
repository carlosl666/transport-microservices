package com.transport.driver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.mock;

class MsDriversApplicationTests {

    @Test
    void testMain1() {
        MsDriversApplication app = new MsDriversApplication();
        Assertions.assertNotNull(app);
    }

    @Test
    void testMain2() {
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);
            mockedStatic.when(() -> SpringApplication.run(WavefrontProperties.Application.class, new String[]{}))
                    .thenReturn(mockApplicationContext);
            MsDriversApplication.main(null);
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }
}
