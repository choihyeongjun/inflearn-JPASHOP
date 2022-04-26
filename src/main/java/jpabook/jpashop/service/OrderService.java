package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    //주문
    @Transactional
    public Long order(Long memberId,Long itemId,int count){
        Member member=memberRepository.findOne(memberId);
        Item item=itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery=new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem=OrderItem.createOrderItem(item,item.getPrice(),count);

        //주문 생성
        Order order=Order.createOrder(member,delivery,orderItem);

        //주문 저장
        orderRepository.save(order);//cascade 때문에 orderitem이랑 delivery가 자동적으로 persist가능

        return order.getId();


    }
    //취소

    @Transactional
    public void cancelOrder(Long orderId){
        Order order=orderRepository.findOne(orderId);
        //주문취소
        //여기서 또하나 JPA장점알수있음 주문취소 같이 만약에 수량 증가하고 취소하면 원래라면
        //DB값 증가시켜주기 위해 sql구문 일일히 다시 다해서 DB값 변경시켜줘야하는데 JPA는 알아서 해줘서 편리리
        order.cancel();
    }
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
