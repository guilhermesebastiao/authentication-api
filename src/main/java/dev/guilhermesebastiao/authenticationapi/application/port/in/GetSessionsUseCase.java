package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.common.PageBody;
import dev.guilhermesebastiao.authenticationapi.application.dto.query.PagingParam;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.SessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetSessionsUseCase {
    PageBody<SessionOutput> execute(Account account, String currentSession, PagingParam paging);
}
