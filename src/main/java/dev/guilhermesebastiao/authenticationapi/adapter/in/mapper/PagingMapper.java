package dev.guilhermesebastiao.authenticationapi.adapter.in.mapper;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.query.PagingQuery;
import dev.guilhermesebastiao.authenticationapi.application.dto.query.PagingParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PagingMapper {
    PagingParam toPaging(PagingQuery paging);
}
