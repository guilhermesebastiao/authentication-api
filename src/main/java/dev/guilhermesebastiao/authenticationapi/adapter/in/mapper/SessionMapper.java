package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.query.PaginationParams;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.query.PagingParam;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionResponse toResponse(SessionOutput output);

    PagingParam toPaging(PaginationParams request);
}
