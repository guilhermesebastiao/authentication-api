package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.SessionApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiPageBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.PaginationParams;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.SessionMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.context.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetCurrentSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeAllSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.RevokeSessionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController implements SessionApi {
    private final GetCurrentSessionUseCase getCurrentSessionUseCase;
    private final RevokeAllSessionsUseCase revokeAllSessionsUseCase;
    private final RevokeSessionUseCase revokeSessionUseCase;
    private final GetSessionsUseCase  getSessionsUseCase;
    private final SessionMapper mapper;


    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageBody<SessionResponse> findAllSessions(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestHeader("X-Session") String sessionToken,
            @Valid PaginationParams pagination
    ) {
        PageResult<SessionOutput> output = getSessionsUseCase.execute(
                securityAccount.account(),
                sessionToken,
                mapper.toPaging(pagination)
        );

        List<SessionResponse> sessions = output.content().stream()
                .map(mapper::toResponse)
                .toList();

        return ApiPageBody.of(
                sessions,
                output.page(),
                output.size(),
                output.totalItems(),
                output.totalPages()
        );
    }

    @Override
    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public SessionResponse findCurrentSession(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestHeader("X-Session") String sessionToken
    ) {
        SessionOutput output = getCurrentSessionUseCase.execute(securityAccount.account(), sessionToken);

        return mapper.toResponse(output);
    }

    @Override
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeSession(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @PathVariable("sessionId") String sessionId
    ) {
        revokeSessionUseCase.execute(securityAccount.account(), UUID.fromString(sessionId));
    }

    @Override
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAllSessions(
            @AuthenticationPrincipal SecurityAccount securityAccount
    ) {
        revokeAllSessionsUseCase.execute(securityAccount.account());
    }
}
