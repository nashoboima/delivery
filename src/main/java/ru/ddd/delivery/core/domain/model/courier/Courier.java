package ru.ddd.delivery.core.domain.model.courier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.libs.ddd.Aggregate;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.GeneralErrors;
import ru.ddd.libs.errs.Result;
import ru.ddd.libs.errs.UnitResult;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public final class Courier extends Aggregate<UUID> {

    @Getter
    private final String name;

    @Getter
    private final Speed speed;

    @Getter
    private Location location;

    private List<StoragePlace> storagePlaces;

    private Courier(String name, Speed speed, Location location, StoragePlace storagePlace) {
        super(UUID.randomUUID());
        this.name = name;
        this.speed = speed;
        this.location = location;
        storagePlaces = new ArrayList<>();
        storagePlaces.add(storagePlace);
    }

    public static Result<Courier, Error> create(String name, Speed speed, Location location) {
        Except.againstNull(name, "name");
        Except.againstNull(speed, "speed");
        Except.againstNull(location, "location");

        var addDefaultStoragePlaceResult = addDefaultStoragePlace();
        if (addDefaultStoragePlaceResult.isFailure()) {
            return Result.failure(addDefaultStoragePlaceResult.getError());
        }
        var courier = new Courier(name, speed, location, addDefaultStoragePlaceResult.getValue());
        return Result.success(courier);
    }

    private static Result<StoragePlace, Error> addDefaultStoragePlace() {
        var createVolumeResult = Volume.create(10);
        if (createVolumeResult.isFailure()) {
            return Result.failure(createVolumeResult.getError());
        }
        return StoragePlace.create("Рюкзак", createVolumeResult.getValue());
    }

    public UnitResult<Error> addStoragePlace(String name, Volume volume) {
        Except.againstNull(name, "name");
        Except.againstNull(volume, "volume");
        
        var createStoragePlaceResult = StoragePlace.create(name, volume);
        if (createStoragePlaceResult.isFailure()) {
            return UnitResult.failure(createStoragePlaceResult.getError());
        }

        storagePlaces.add(createStoragePlaceResult.getValue());
        return UnitResult.success();
    }

    public Result<Boolean, Error> canTakeOrder(Order order) {
        Except.againstNull(order, "order");

        boolean canTakeOrder = storagePlaces.stream().anyMatch(s -> s.canStore(order.getVolume()).getValue());
        if (!canTakeOrder) {
            return Result.failure(Errors.noSuitableStoragePlaces());
        }

        return Result.success(canTakeOrder);
    }

    public UnitResult<Error> takeOrder(Order order) {
        Except.againstNull(order, "order");
        
        Optional<StoragePlace> suitableStoragePlace = storagePlaces.stream().filter(s -> s.canStore(order.getVolume()).getValue()).findFirst();

        if (suitableStoragePlace.isEmpty()) {
            return UnitResult.failure(Errors.noSuitableStoragePlaces());
        }

        UnitResult<Error> storeResult = suitableStoragePlace.get().store(order.getId(), order.getVolume());
        if (storeResult.isFailure()) {
            return UnitResult.failure(storeResult.getError());
        }

        return UnitResult.success();
    }

    public UnitResult<Error> completeOrder(Order order) {
        Except.againstNull(order, "order");
        
        Optional<StoragePlace> orderStoragePlace = storagePlaces
                                                    .stream()
                                                    .filter(s -> s.getOrderId()
                                                                    .map(oId -> oId.equals(order.getId()))
                                                                    .orElse(false))
                                                    .findFirst();

        if (orderStoragePlace.isEmpty()) {
            List<UUID> uuids = storagePlaces.stream().map(s -> s.getOrderId().orElse(null)).filter(oId -> oId != null).toList();
            return UnitResult.failure(Errors.noSuchOrderInStoragePlaces(uuids));
        }

        UnitResult<Error> clearResult = orderStoragePlace.get().clear(order.getId());
        if (clearResult.isFailure()) {
            return UnitResult.failure(clearResult.getError());
        }
        
        return UnitResult.success();
    }

    public Result<Double, Error> calculateTimeToLocation(Location location) {
        Except.againstNull(location, "location");

        var distanceResult = this.location.distanceTo(location);
        if (distanceResult.isFailure()) {
            return Result.failure(distanceResult.getError());
        }

        double timeToLocation = Math.ceil(distanceResult.getValue()/speed.getValue());
        return Result.success(timeToLocation);
    }

    public UnitResult<Error> move(Location target) {
        if (target == null) {
            return UnitResult.failure(GeneralErrors.valueIsRequired("target"));
        }

        int difX = target.getX() - location.getX();
        int difY = target.getY() - location.getY();
        int cruisingRange = speed.getValue();

        int moveX = Math.max(-cruisingRange, Math.min(difX, cruisingRange));
        cruisingRange -= Math.abs(moveX);

        int moveY = Math.max(-cruisingRange, Math.min(difY, cruisingRange));

        Result<Location, Error> locationCreateResult = Location.create(
                location.getX() + moveX,
                location.getY() + moveY
        );

        if (locationCreateResult.isFailure()) {
            return UnitResult.failure(locationCreateResult.getError());
        }

        this.location = locationCreateResult.getValue();
        return UnitResult.success();
    }
    
    public static class Errors {
        public static Error noSuitableStoragePlaces() {
            return Error.of("no.suitable.storagePlaces",
                            "Нет подходящих мест хранения");
        }

        public static Error noSuchOrderInStoragePlaces(List<UUID> uuids) {
            String stringOfOrders = "Нет заказов";
            if (!uuids.isEmpty()) {
                stringOfOrders = "Хранятся заказы: " + String.join(", ", uuids.stream().map(u -> u.toString()).toList());
            }
            return Error.of("no.such.order.in.storagePlaces",
                            "Нет такого заказа в местах хранения. " + stringOfOrders);
        }
    }
}
