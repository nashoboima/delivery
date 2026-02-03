package ru.ddd.delivery.adapters.in.http.openapi.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import ru.ddd.delivery.adapters.in.http.openapi.mappers.CourierMapper;
import ru.ddd.delivery.adapters.in.http.openapi.mappers.OrderMapper;
import ru.ddd.delivery.adapters.in.http.openapi.model.Courier;
import ru.ddd.delivery.adapters.in.http.openapi.model.CreateCourierResponse;
import ru.ddd.delivery.adapters.in.http.openapi.model.CreateOrderResponse;
import ru.ddd.delivery.adapters.in.http.openapi.model.NewCourier;
import ru.ddd.delivery.adapters.in.http.openapi.model.Order;
import ru.ddd.delivery.core.application.commands.CreateCourierCommand;
import ru.ddd.delivery.core.application.commands.CreateCourierCommandHandler;
import ru.ddd.delivery.core.application.commands.CreateOrderCommand;
import ru.ddd.delivery.core.application.commands.CreateOrderCommandHandler;
import ru.ddd.delivery.core.application.queries.GetAllCouriersQueryHandler;
import ru.ddd.delivery.core.application.queries.GetAllIncompleteOrdersQueryHandler;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-09T21:18:13.892080390+03:00[Europe/Moscow]", comments = "Generator version: 7.19.0")
@Controller
@CrossOrigin(origins = "*") // Добавьте это
@RequestMapping("${openapi.swaggerDelivery.base-path:}")
public class ApiApiController implements ApiApi {

    private final NativeWebRequest request;

    @Autowired
    private CreateCourierCommandHandler createCourierCommandHandler;

    @Autowired
    private CreateOrderCommandHandler createOrderCommandHandler;

    @Autowired
    private GetAllCouriersQueryHandler getAllCouriersQueryHandler;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private GetAllIncompleteOrdersQueryHandler getAllIncompleteOrdersQueryHandler;
    
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    public ApiApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<CreateCourierResponse> createCourier(@Nullable @Valid NewCourier newCourier) {
        var CreateCourierCommandResult = CreateCourierCommand.create(newCourier.getName(), newCourier.getSpeed());
        if (CreateCourierCommandResult.isFailure()) {
            return ResponseEntity.badRequest().build();
        }
        var command = CreateCourierCommandResult.getValue();

        var handleResult = createCourierCommandHandler.handle(command);
        if (handleResult.isFailure())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        
        var response = new CreateCourierResponse();
        response.setCourierId(handleResult.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder() {
        // Формируем команду
        var createCommandResult = CreateOrderCommand.create(
                UUID.randomUUID(),
                "Несуществующая",
                5
        );
        if (createCommandResult.isFailure()) {
            return ResponseEntity.badRequest().build();
        }
        var command = createCommandResult.getValue();

        // Обрабатываем команду
        var handleResult = this.createOrderCommandHandler.handle(command);
        if (handleResult.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var response = new CreateOrderResponse();
        response.setOrderId(handleResult.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {
        var handleResult = getAllCouriersQueryHandler.handle();
        if (handleResult.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        var couriers = handleResult.getValue().stream().map(courierDto -> courierMapper.toHttp(courierDto)).toList();
        return ResponseEntity.ok(couriers);

    }

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        var handleResult = getAllIncompleteOrdersQueryHandler.handle();
        if (handleResult.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        var orders = handleResult.getValue().stream().map(orderDto -> orderMapper.toHttp(orderDto)).toList();
        return ResponseEntity.ok(orders);
    }

    

}
