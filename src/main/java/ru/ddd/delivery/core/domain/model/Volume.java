package ru.ddd.delivery.core.domain.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.libs.ddd.ValueObject;
import ru.ddd.libs.errs.Err;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Volume extends ValueObject<Volume> {

    private final int value;

    public static Result<Volume, Error> create(int value) {
        var err = Err.againstZeroOrNegative(value, "value");
        if (err != null) return Result.failure(err);

        return Result.success(new Volume(value));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.value);
    }

}
