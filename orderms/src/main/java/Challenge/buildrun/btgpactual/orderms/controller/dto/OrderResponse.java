package Challenge.buildrun.btgpactual.orderms.controller.dto;

import Challenge.buildrun.btgpactual.orderms.entity.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long costumerId,
                            BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity) {
        return new OrderResponse(entity.getOrderId(),entity.getCostumerId(),entity.getTotal());
    }
}
