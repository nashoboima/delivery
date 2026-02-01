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
public class CreateOrderCommand {
    private final UUID orderId;
    private final String street;
    private final int volume;

    public static Result<CreateOrderCommand, Error> create(UUID orderId, String street, int volume) {
        var err = Err.combine(
            Err.againstNullOrEmpty(orderId, "orderId"),
            Err.againstNullOrEmpty(street, "street"),
            Err.againstNegative(volume, "volume")
        );
        if (err != null) return Result.failure(err);

        return Result.success(new CreateOrderCommand(orderId, street, volume));
    }
}
