package dev.guilhermesebastiao.authenticationapi.adapter.in.controller;

import dev.guilhermesebastiao.authenticationapi.adapter.in.contract.SessionApi;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiListBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.PaginationRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.RevokeSessionsRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.adapter.in.mapper.SessionMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.security.SecurityAccount;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetCurrentSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GetSessionsUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SessionSignOutUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SignOutAllUseCase;
import dev.guilhermesebastiao.authenticationapi.application.result.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.result.SessionResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController implements SessionApi {
    private final GetCurrentSessionUseCase getCurrentSessionUseCase;
    private final SessionSignOutUseCase sessionSignOutUseCase;
    private final GetSessionsUseCase  getSessionsUseCase;
    private final SignOutAllUseCase signOutAllUseCase;
    private final SessionMapper mapper;


    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<ApiListBody<SessionResponse>> findAllSessions(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestHeader("X-Session") String sessionToken,
            @Valid PaginationRequest pagination
    ) {
        PageResult<SessionResult> result = getSessionsUseCase.execute(
                securityAccount.account(),
                sessionToken,
                mapper.toPaging(pagination)
        );

        List<SessionResponse> sessions = result.content().stream()
                .map(mapper::toResponse)
                .toList();

        ApiListBody<SessionResponse> data = new ApiListBody<>(
                sessions,
                result.page(),
                result.size(),
                result.totalItems(),
                result.totalPages()
        );

        return ApiBody.of(data);
    }

    @Override
    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<SessionResponse> findCurrentSession(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestHeader("X-Session") String sessionToken
    ) {
        SessionResult data = getCurrentSessionUseCase.execute(securityAccount.account(), sessionToken);

        return ApiBody.of(mapper.toResponse(data));
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<Void> revokeSession(
            @AuthenticationPrincipal SecurityAccount securityAccount,
            @RequestBody @Valid RevokeSessionsRequest dto
    ) {
        sessionSignOutUseCase.execute(securityAccount.account(), dto.sessionsIds());

        return ApiBody.of();
    }

    @Override
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiBody<Void> revokeAllSessions(
            @AuthenticationPrincipal SecurityAccount securityAccount
    ) {
        signOutAllUseCase.execute(securityAccount.account());

        return ApiBody.of();
    }
}
