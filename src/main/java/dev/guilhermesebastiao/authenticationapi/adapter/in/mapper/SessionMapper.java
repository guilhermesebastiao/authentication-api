package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.PaginationRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.application.command.PageQueryCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.SessionResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionResponse toResponse(SessionResult result);

    PageQueryCommand toPaging(PaginationRequest request);
}
