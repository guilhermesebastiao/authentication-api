package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter.IpGeolocationAdapter;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.IpLocationOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class IpGeolocationAdapterTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private IpGeolocationAdapter adapter;

    @Test
    @DisplayName("Should map a successful geolocation response")
    void givenSuccessfulResponse_whenFindLocation_thenReturnMappedLocation() {
        String ipAddress = "203.0.113.10";
        IpGeolocationAdapter.LocationResponse response = new IpGeolocationAdapter.LocationResponse(true, "Brazil", "Sao Paulo");

        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/{ip}", ipAddress);
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(response).when(responseSpec).body(IpGeolocationAdapter.LocationResponse.class);

        var result = adapter.findLocation(ipAddress);

        assertTrue(result.isPresent());
        assertEquals(new IpLocationOutput("Brazil", "Sao Paulo"), result.get());
        then(requestHeadersUriSpec).should().uri("/{ip}", ipAddress);
    }

    @Test
    @DisplayName("Should return empty when the geolocation response is null")
    void givenNullResponse_whenFindLocation_thenReturnEmpty() {
        String ipAddress = "203.0.113.10";

        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/{ip}", ipAddress);
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(null).when(responseSpec).body(IpGeolocationAdapter.LocationResponse.class);

        var result = adapter.findLocation(ipAddress);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when the geolocation response is unsuccessful")
    void givenUnsuccessfulResponse_whenFindLocation_thenReturnEmpty() {
        String ipAddress = "203.0.113.10";

        IpGeolocationAdapter.LocationResponse response = new IpGeolocationAdapter.LocationResponse(false, null, null);

        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/{ip}", ipAddress);
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(response).when(responseSpec).body(IpGeolocationAdapter.LocationResponse.class);

        var result = adapter.findLocation(ipAddress);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when the geolocation request fails")
    void givenRestClientFailure_whenFindLocation_thenReturnEmpty() {
        String ipAddress = "203.0.113.10";

        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri("/{ip}", ipAddress);
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();

        doThrow(new RestClientException("Geolocation service unavailable"))
                .when(responseSpec)
                .body(IpGeolocationAdapter.LocationResponse.class);

        var result = adapter.findLocation(ipAddress);

        assertTrue(result.isEmpty());
    }
}
