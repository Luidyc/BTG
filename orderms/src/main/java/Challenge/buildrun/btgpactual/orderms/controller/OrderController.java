package Challenge.buildrun.btgpactual.orderms.controller;


import Challenge.buildrun.btgpactual.orderms.controller.dto.ApiResponse;
import Challenge.buildrun.btgpactual.orderms.controller.dto.OrderResponse;
import Challenge.buildrun.btgpactual.orderms.controller.dto.PaginationResponse;
import Challenge.buildrun.btgpactual.orderms.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{costumerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable(name = "costumerId") Long costumerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
            var pageResponse = orderService.findAllByCostumerId(costumerId, PageRequest.of(page,pageSize));
            var totalOnOrders = orderService.findAmountOnOrdersByCostumerId(costumerId);

           return ResponseEntity.ok(new ApiResponse<>(
                   Map.of("totalOnOrders",totalOnOrders),
                   pageResponse.getContent(),
                  PaginationResponse.fromPage(pageResponse)
           ));
    }


}
