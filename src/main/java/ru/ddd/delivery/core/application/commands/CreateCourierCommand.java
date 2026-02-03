package ru.ddd.delivery.core.application.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ddd.libs.errs.Err;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCourierCommand {
    private final String name;
    private final int speed;

    public static Result<CreateCourierCommand, Error> create(String name, int speed) {
        var err = Err.combine(
            Err.againstNullOrEmpty(name, "name"),
            Err.againstNegative(speed, "speed")
        );
        if (err != null) return Result.failure(err);

        return Result.success(new CreateCourierCommand(name, speed));
    }
}
