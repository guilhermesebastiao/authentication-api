package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.out.RefreshRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureRandomGeneratorPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Refresh;
import dev.guilhermesebastiao.authenticationapi.domain.model.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CreateRefreshServiceTest {

    @Mock
    private SecureRandomGeneratorPort secureRandomGenerator;

    @Mock
    private RefreshRepositoryPort refreshRepository;

    @Mock
    private SecureHasherPort secureHasher;

    @InjectMocks
    private CreateRefreshService service;

    @Test
    @DisplayName("Should create and save a refresh token for the session")
    void givenSession_whenCreateRefresh_thenRefreshTokenIsGeneratedHashedSavedAndReturned() {
        Session session = new Session();
        String refreshToken = "refresh-token";
        String refreshTokenHash = "refresh-token-hash";

        given(secureRandomGenerator.generate(32))
                .willReturn(refreshToken);

        given(secureHasher.hash(refreshToken))
                .willReturn(refreshTokenHash);

        given(refreshRepository.save(any(Refresh.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        CreateRefreshOutput result = service.execute(session);

        assertEquals(refreshToken, result.refreshToken());
        assertSame(session, result.refresh().getSession());
        assertEquals(refreshTokenHash, result.refresh().getRefreshTokenHash());

        then(secureRandomGenerator).should().generate(32);
        then(secureHasher).should().hash(refreshToken);
        then(refreshRepository).should().save(result.refresh());
    }
}
