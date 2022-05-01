package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
XToOne(ManyToOne,OneToOne)
Order
Order->member
Order->Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order>all=orderRepository.findAllByString(new OrderSearch());
        return all;
    }
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto>ordersV2(){

        //ORDER 2개
        //1+N문제 발생생
       List<Order>orders=orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto>ordersV3(){
        List<Order>orders=orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o->new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
    @GetMapping("/api/v4/simple-orders")//fetch join + 기존에 셀렉트절 많은거에서 내가 원하는것만 직접쿼리 작성
    public List<OrderSimpleQueryDto>ordersV4(){
        return orderRepository.findOrderDtos();
    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();//Lazy초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();//lazy초기화
        }
    }
}
