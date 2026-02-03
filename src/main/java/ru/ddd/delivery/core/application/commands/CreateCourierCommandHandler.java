package ru.ddd.delivery.core.application.commands;

import java.util.UUID;

import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

public interface CreateCourierCommandHandler {
    Result<UUID, Error> handle(CreateCourierCommand command);
}
