package ru.ddd.delivery.core.application.commands;

import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.UnitResult;

public interface AssignOrderCommandHandler {
    UnitResult<Error> handle();
}
