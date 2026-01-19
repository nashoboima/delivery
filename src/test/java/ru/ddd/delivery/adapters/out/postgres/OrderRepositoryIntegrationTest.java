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
import ru.ddd.delivery.core.ports.OrderRepository;

@SpringBootTest
@ActiveProfiles("test") 
public class OrderRepositoryIntegrationTest extends BasePostgresContainerTest {

    @Autowired
    OrderRepository repository;

    @BeforeEach
    @SuppressWarnings("unused")
    void clean() {
        repository.clean();
    }
    @Test
    void save_and_find_order_by_id() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(10).getValue()).getValue();

        // Act
        repository.save(order);
        var loaded = repository.findById(order.getId());

        // Assert
        assertAll(
            () -> assertThat(loaded).isPresent(),
            () -> assertThat(loaded.get().getLocation()).isEqualTo(order.getLocation()),
            () -> assertThat(loaded.get().getVolume()).isEqualTo(order.getVolume())
        );
    }

    @Test
    void find_random_with_created_status() {
        // Arrange
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(5).getValue()).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(6, 6).getValue(), Volume.create(6).getValue()).getValue();
        var order3 = Order.create(UUID.randomUUID(), Location.create(6, 6).getValue(), Volume.create(6).getValue()).getValue();

        repository.save(order1);
        repository.save(order2);
        repository.save(order3);

        // Act
        var loaded = repository.findRandomWithCreatedStatus();

        // Assert
        assertThat(loaded).isPresent();
    }

    @Test
    void find_all_assigned_orders() {
        // Arrange
        var courier1 = Courier.create("k1", Speed.create(1).getValue(), Location.create(1, 1).getValue()).getValue();
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(5).getValue()).getValue();
        order1.assign(courier1);
        var order2 = Order.create(UUID.randomUUID(), Location.create(6, 6).getValue(), Volume.create(6).getValue()).getValue();
        var courier3 = Courier.create("k3", Speed.create(3).getValue(), Location.create(3, 3).getValue()).getValue();
        var order3 = Order.create(UUID.randomUUID(), Location.create(6, 6).getValue(), Volume.create(6).getValue()).getValue();
        order3.assign(courier3);

        repository.save(order1);
        repository.save(order2);
        repository.save(order3);

        // Act
        var loaded = repository.findAllAssigned();

        // Assert
        assertAll(
            () -> assertThat(loaded.size()).isEqualTo(2),
            () -> assertThat(loaded.get(0).getId()).isEqualTo(order1.getId()),
            () -> assertThat(loaded.get(1).getId()).isEqualTo(order3.getId())
        );
    }
}
