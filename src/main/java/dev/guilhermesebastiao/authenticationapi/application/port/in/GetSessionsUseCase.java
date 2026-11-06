package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageQuery;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetSessionsUseCase {
    PageResult<SessionOutput> execute(Account account, String currentSession, PageQuery paging);
}
