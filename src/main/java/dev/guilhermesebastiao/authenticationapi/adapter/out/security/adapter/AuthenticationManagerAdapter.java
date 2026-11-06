package dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter;

import dev.guilhermesebastiao.authenticationapi.application.port.out.AuthenticationPort;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAuthenticationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountDisabledException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerAdapter implements AuthenticationPort {
    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            authenticationManager.authenticate(authenticationToken);

        } catch (BadCredentialsException | UsernameNotFoundException exception) {
            throw new AccountInvalidCredentialsException();

        } catch (DisabledException exception) {
            throw new AccountDisabledException();

        } catch (LockedException exception) {
            throw new AccountNotActivatedException();

        } catch (AccountExpiredException | CredentialsExpiredException exception) {
            throw new AccountAuthenticationExpiredException();
        }
    }
}
