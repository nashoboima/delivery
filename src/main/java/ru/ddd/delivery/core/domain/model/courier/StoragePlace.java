package ru.ddd.delivery.core.domain.model.courier;

import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.libs.ddd.BaseEntity;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.Result;
import ru.ddd.libs.errs.UnitResult;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public final class StoragePlace extends BaseEntity<UUID> {

    @Getter
    private final String name;

    @Getter
    private Volume totalVolume;

    private UUID orderId;

    private StoragePlace(String name, Volume volume) {
        super(UUID.randomUUID());
        this.name = name;
        this.totalVolume = volume;
    }

    public static Result<StoragePlace, Error> create(String name, Volume volume) {
        Except.againstNull(name, "name");
        Except.againstNull(volume, "volume");

        var storagePlace = new StoragePlace(name, volume);
        return Result.success(storagePlace);
    }

    public Result<Boolean, Error> canStore(Volume volume) {
        Except.againstNull(volume, "volume");

        boolean canStore = (null == orderId) && (volume.getValue() <= totalVolume.getValue());
        return Result.success(canStore);
    }

    public UnitResult<Error> store(UUID orderId, Volume volume) {
        Except.againstNull(orderId, "orderId");
        Except.againstNull(volume, "volume");
        
        if (this.orderId != null) return UnitResult.failure(Errors.storagePlaceIsOccupied());
        if (volume.getValue() > totalVolume.getValue()) return UnitResult.failure(Errors.storagePlaceVolumeIsExceeded(totalVolume.getValue()));

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
