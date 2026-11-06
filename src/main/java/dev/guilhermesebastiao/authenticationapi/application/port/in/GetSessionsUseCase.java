package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.result.PageResult;
import dev.guilhermesebastiao.authenticationapi.application.command.PageQueryCommand;
import dev.guilhermesebastiao.authenticationapi.application.result.SessionResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface GetSessionsUseCase {
    PageResult<SessionResult> execute(Account account, String currentSession, PageQueryCommand paging);
}
