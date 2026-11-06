package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.PaginationParams;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.response.SessionResponse;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageQuery;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionResponse toResponse(SessionOutput output);

    PageQuery toPaging(PaginationParams request);
}
