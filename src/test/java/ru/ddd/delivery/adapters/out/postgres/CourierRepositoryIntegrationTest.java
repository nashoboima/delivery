package ru.ddd.delivery.adapters.out.postgres;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.ports.CourierRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CourierRepositoryIntegrationTest extends BasePostgresContainerTest {

    @Autowired
    CourierRepository repository;

    @BeforeEach
    @SuppressWarnings("unused")
    void clean() {
        repository.clean();
    }
    @Test
    void save_and_find_courier_by_id() {
        // Arrange
        var courier = Courier.create("k1", Speed.create(2).getValue(), Location.create(1, 1).getValue()).getValue();

        // Act
        repository.save(courier);
        var loaded = repository.findById(courier.getId());

        // Assert
        assertAll(
            () -> assertThat(loaded).isPresent(),
            () -> assertThat(loaded.get().getName()).isEqualTo(courier.getName()),
            () -> assertThat(loaded.get().getSpeed()).isEqualTo(courier.getSpeed()),
            () -> assertThat(loaded.get().getLocation()).isEqualTo(courier.getLocation())
        );
    }

    @Test
    void find_all_assignable_couriers() {
        // Arrange
        var courier1 = Courier.create("k1", Speed.create(1).getValue(), Location.create(1, 1).getValue()).getValue();
        courier1.addStoragePlace( "rack", Volume.create(20).getValue());
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(5).getValue()).getValue();
        courier1.takeOrder(order1);
        var courier2 = Courier.create("k2", Speed.create(2).getValue(), Location.create(2, 2).getValue()).getValue();
        var courier3 = Courier.create("k3", Speed.create(3).getValue(), Location.create(3, 3).getValue()).getValue();
        var order3 = Order.create(UUID.randomUUID(), Location.create(6, 6).getValue(), Volume.create(6).getValue()).getValue();
        courier3.takeOrder(order3);

        repository.save(courier1);
        repository.save(courier2);
        repository.save(courier3);

        // Act
        var loaded = repository.findAllAssignableCouriers();

        // Assert
        assertAll(
            () -> assertThat(loaded.size()).isEqualTo(1),
            () -> assertThat(loaded.get(0).getName()).isEqualTo(courier2.getName()),
            () -> assertThat(loaded.get(0).getSpeed()).isEqualTo(courier2.getSpeed()),
            () -> assertThat(loaded.get(0).getLocation()).isEqualTo(courier2.getLocation())
        );
    }
}
