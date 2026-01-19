package ru.ddd.delivery.core.domain.model.order;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.libs.ddd.Aggregate;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.Result;
import ru.ddd.libs.errs.UnitResult;

@Entity
@Table(name = "orders")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public final class Order extends Aggregate<UUID> {

    @Embedded
    @Getter
    private final Location location;

    @Embedded
    @Getter
    private final Volume volume;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    @Getter
    private OrderStatus status;

    @Column(name = "courier_id")
    @Getter
    private UUID courierId;

    private Order(UUID orderId, Location location, Volume volume) {
        super(orderId);
        this.location = location;
        this.volume = volume;
        status = OrderStatus.CREATED;
    }

    public static Result<Order, Error> create(UUID orderId, Location location, Volume volume) {
        Except.againstNull(orderId, "orderId");
        Except.againstNull(location, "location");
        Except.againstNull(volume, "volume");

        var order = new Order(orderId, location, volume);
        return Result.success(order);
    }

    public UnitResult<Error> assign(Courier courier) {
        Except.againstNull(courier, "courier");
        
        if (status != OrderStatus.CREATED) {
            return UnitResult.failure(Errors.orderNotInCreatedStatus(status));
        }
        
        status = OrderStatus.ASSIGNED;

        courierId = courier.getId();
        return UnitResult.success();
    }

    public UnitResult<Error> complete() {
        if (status != OrderStatus.ASSIGNED) {
            return UnitResult.failure(Errors.orderWasNotAssigned());
        }
        
        status = OrderStatus.COMPLETED;
        return UnitResult.success();
    }

    public static class Errors {
        public static Error orderWasNotAssigned() {
            return Error.of("order.was.not.assigned",
                            "Заказ не был назначен");
        }

        public static Error orderNotInCreatedStatus(OrderStatus status) {
            return Error.of("order.not.in.created.status",
                            "Заказ не в статусе CREATED. Статус: " + status.toValue());
        }
    }
}
