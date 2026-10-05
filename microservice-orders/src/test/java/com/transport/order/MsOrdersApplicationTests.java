package com.transport.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.mock;

class MsOrdersApplicationTests {

    @Test
    void testMain1() {
        MsOrdersApplication app = new MsOrdersApplication();
        Assertions.assertNotNull(app);
    }

    @Test
    void testMain2() {
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);
            mockedStatic.when(() -> SpringApplication.run(WavefrontProperties.Application.class, new String[]{}))
                    .thenReturn(mockApplicationContext);
            MsOrdersApplication.main(null);
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }
}
