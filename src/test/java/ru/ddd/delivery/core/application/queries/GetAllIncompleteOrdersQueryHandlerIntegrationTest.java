package ru.ddd.delivery.core.application.queries;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ru.ddd.delivery.adapters.out.postgres.BasePostgresContainerTest;
import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.ports.OrderRepository;

@SpringBootTest
@ActiveProfiles("test")
public class GetAllIncompleteOrdersQueryHandlerIntegrationTest extends BasePostgresContainerTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    GetAllIncompleteOrdersQueryHandlerImpl handler;

    @Test
    void testGetAllIncompleteOrders() {
        // Arrange
        var order1 = Order.create(UUID.randomUUID(), Location.create(1, 1).getValue(), Volume.create(10).getValue()).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(2, 2).getValue(), Volume.create(10).getValue()).getValue();
        var courier = Courier.create("test", Speed.create(1).getValue(), Location.create(10, 10).getValue()).getValue();
        order2.assign(courier);
        var order3 = Order.create(UUID.randomUUID(), Location.create(2, 2).getValue(), Volume.create(10).getValue()).getValue();
        order3.assign(courier);
        order3.complete();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // Act
        var result = handler.handle();

        // Assert
        assertThat(result.isSuccess()).isTrue();

        var response = result.getValue();

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).id()).isEqualTo(order1.getId());
        assertThat(response.get(1).id()).isEqualTo(order2.getId());
    }

}
