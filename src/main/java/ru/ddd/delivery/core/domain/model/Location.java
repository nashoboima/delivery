package ru.ddd.delivery.core.domain.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.libs.ddd.ValueObject;
import ru.ddd.libs.errs.Err;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.GeneralErrors;
import ru.ddd.libs.errs.Result;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location extends ValueObject<Location> {
    private static final int COORDINATE_MIN_VALUE = 1;
    private static final int COORDINATE_MAX_VALUE = 10;

    /**
     * Горизонталь
     */
    private final int x;

    /**
     * Вертикаль
     */
    private final int y;

    public static final Location MIN_VALUE = new Location(COORDINATE_MIN_VALUE, COORDINATE_MIN_VALUE);
    public static final Location MAX_VALUE = new Location(COORDINATE_MAX_VALUE, COORDINATE_MAX_VALUE);

    public static Result<Location, Error> create(int x, int y) {
        var err = Err.combine(
                Err.againstOutOfRange(x, COORDINATE_MIN_VALUE, COORDINATE_MAX_VALUE, "x"),
                Err.againstOutOfRange(y, COORDINATE_MIN_VALUE, COORDINATE_MAX_VALUE, "y"));
        if (err != null) return Result.failure(err);

        return Result.success(new Location(x, y));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(this.x, this.y);
    }

    public Result<Integer, Error> distanceTo(Location target) {
        if (null == target) {
            var err = GeneralErrors.valueIsRequired("target");
            return Result.failure(err);
        }
        if (this.equals(target)) return Result.success(0);

        int xDistance = Math.abs(this.x - target.getX());
        int yDistance = Math.abs(this.y - target.getY());
        return Result.success(xDistance + yDistance);
    }

}
