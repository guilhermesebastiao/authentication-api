package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.result.IpLocationResult;

import java.util.Optional;

public interface IpGeolocationPort {
    Optional<IpLocationResult> findLocation(String ipAddress);
}
