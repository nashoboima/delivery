package ru.ddd.delivery.core.application.commands;

import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.UnitResult;

public interface AddStoragePlaceCommandHandler {
    UnitResult<Error> handle(AddStoragePlaceCommand command);
}
