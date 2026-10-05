package com.transport.order;

import com.transport.gateway.ApiGatewayApplication;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ApiGatewayApplicationTests {

	@Test
	void testMain1() {
		ApiGatewayApplication app = new ApiGatewayApplication();
		assertNotNull(app);
	}

	@Test
	void testSetApplicationContext2() {
		try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
			ConfigurableApplicationContext mockApplicationContext = mock(ConfigurableApplicationContext.class);
			mockedStatic.when(() -> SpringApplication.run(WavefrontProperties.Application.class, new String[]{}))
					.thenReturn(mockApplicationContext);
			ApiGatewayApplication.main(null);
		} catch (Exception e) {
			assertNotNull(e.getMessage());
		}
	}
}
