package Challenge.buildrun.btgpactual.orderms.repository;

import Challenge.buildrun.btgpactual.orderms.controller.dto.OrderResponse;
import Challenge.buildrun.btgpactual.orderms.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCostumerId(Long costumerId, PageRequest pageRequest);
}
