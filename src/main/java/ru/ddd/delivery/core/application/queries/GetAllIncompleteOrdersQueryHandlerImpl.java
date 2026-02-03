package ru.ddd.delivery.core.application.queries;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.ddd.delivery.core.application.queries.dto.LocationDto;
import ru.ddd.delivery.core.application.queries.dto.OrderDto;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Service
public class GetAllIncompleteOrdersQueryHandlerImpl implements GetAllIncompleteOrdersQueryHandler {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Result<List<OrderDto>, Error> handle() {
        String sql = """
                SELECT id, location_x, location_y
                FROM orders
                WHERE order_status != 'COMPLETED'
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();

        List<OrderDto> orders = rows.stream().map(row -> new OrderDto(
                (UUID) row[0],
                new LocationDto(((Number) row[1]).intValue(), ((Number) row[2]).intValue())))
                .toList();
        return Result.success(orders);

    }

}