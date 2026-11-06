package dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter;

import dev.guilhermesebastiao.authenticationapi.application.port.out.IpGeolocationPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.IpLocationOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IpGeolocationAdapter implements IpGeolocationPort {
    private final RestClient ipGeolocationRestClient;

    @Override
    public Optional<IpLocationOutput> findLocation(String ipAddress) {
        try {
            LocationResponse response = findLocationRestClient(ipAddress);

            if (response == null || !response.success()) {
                return Optional.empty();
            }

            return Optional.of(
                    new IpLocationOutput(response.country(), response.city())
            );

        } catch (RestClientException exception) {
            return Optional.empty();
        }
    }

    private LocationResponse findLocationRestClient(String ipAddress) {
        return ipGeolocationRestClient
                .get()
                .uri("/{ip}", ipAddress)
                .retrieve()
                .body(LocationResponse.class);
    }

    public record LocationResponse(boolean success, String country, String city) { }
}
