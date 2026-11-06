package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.IpLocationOutput;

import java.util.Optional;

public interface IpGeolocationPort {
    Optional<IpLocationOutput> findLocation(String ipAddress);
}
