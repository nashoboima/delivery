package ru.ddd.delivery.core.application.commands;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ddd.libs.errs.Err;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoragePlaceCommand {
    private final UUID courierId;
    private final String name;
    private final int totalVolume;

    public static Result<AddStoragePlaceCommand, Error> create(UUID courierId, String name, int totalVolume) {
        var err = Err.combine(
            Err.againstNullOrEmpty(courierId, "courierId"),
            Err.againstNullOrEmpty(name, "name"),
            Err.againstNegative(totalVolume, "totalVolume")
        );
        if (err != null) return Result.failure(err);

        return Result.success(new AddStoragePlaceCommand(courierId, name, totalVolume));
    }
}
