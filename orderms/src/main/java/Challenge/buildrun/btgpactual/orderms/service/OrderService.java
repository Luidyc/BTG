package Challenge.buildrun.btgpactual.orderms.service;


import Challenge.buildrun.btgpactual.orderms.controller.dto.OrderResponse;
import Challenge.buildrun.btgpactual.orderms.entity.OrderEntity;
import Challenge.buildrun.btgpactual.orderms.entity.OrderItem;
import Challenge.buildrun.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import Challenge.buildrun.btgpactual.orderms.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event){
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCostumerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));
        repository.save(entity);
    }

    public Page<OrderResponse> findAllByCostumerId(Long costumerId, PageRequest pageRequest){
         Page<OrderEntity> orders = repository.findAllByCostumerId(costumerId, pageRequest);
         return orders.map(OrderResponse::fromEntity);
    };

    public BigDecimal findAmountOnOrdersByCostumerId(Long costumerId){
        var aggregations = newAggregation(
              match(Criteria.where("costumerId").is(costumerId)),
                group().sum("total").as("total")
        );
       var response = mongoTemplate.aggregate(aggregations,"tb_orders", Document.class);
       return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(i->i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
