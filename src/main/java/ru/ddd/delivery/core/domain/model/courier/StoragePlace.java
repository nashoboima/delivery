package ru.ddd.delivery.core.domain.model.courier;

import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.libs.ddd.BaseEntity;
import ru.ddd.libs.errs.Err;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.Result;
import ru.ddd.libs.errs.UnitResult;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public final class StoragePlace extends BaseEntity<UUID> {

    @Getter
    private final String name;

    @Getter
    private int totalVolume;

    private UUID orderId;

    private StoragePlace(String name, int volume) {
        super(UUID.randomUUID());
        this.name = name;
        this.totalVolume = volume;
    }

    public static Result<StoragePlace, Error> create(String name, int volume) {
        Except.againstNull(name, "name");
        var err = Err.againstZeroOrNegative(volume, "volume");
        if (err != null) return Result.failure(err);

        var storagePlace = new StoragePlace(name, volume);
        return Result.success(storagePlace);
    }

    public Result<Boolean, Error> canStore(int volume) {
        var err = Err.againstZeroOrNegative(volume, "volume");
        if (err != null) return Result.failure(err);

        boolean canStore = (null == orderId) && (volume <= totalVolume);
        return Result.success(canStore);
    }

    public UnitResult<Error> store(UUID orderId, int volume) {
        Except.againstNull(orderId, "orderId");
        var err = Err.againstZeroOrNegative(volume, "volume");        
        if (err != null) return UnitResult.failure(err);
        
        if (this.orderId != null) return UnitResult.failure(Errors.storagePlaceIsOccupied());
        if (volume > totalVolume) return UnitResult.failure(Errors.storagePlaceVolumeIsExceeded(totalVolume));

        this.orderId = orderId;
        return UnitResult.success();
    }

    public UnitResult<Error> clear(UUID orderid) {
        Except.againstNull(orderId, "orderId");
        if (this.orderId != orderid) return UnitResult.failure(Errors.orderIdIsWrong(this.orderId));
        
        this.orderId = null;
        return UnitResult.success();
    }

    public boolean isOccupied() {
        return orderId != null;
    }

    public Optional<UUID> getOrderId() {
        return Optional.ofNullable(orderId);
    }

    public static class Errors {
        public static Error storagePlaceIsOccupied() {
            return Error.of("storagePlace.is.occupied",
                            "Место хранения занято");
        }

        public static Error storagePlaceVolumeIsExceeded(int totalVolume) {
            return Error.of("storagePlace.volume.is.exceeded",
                            "Объем места хранения " + totalVolume + " превышен");
        }

        public static Error orderIdIsWrong(UUID orderId) {
            return Error.of("orderId.is.wrong",
                            "Неверный orderId. Хранимый orderId: " + orderId);
        }
    }
}
