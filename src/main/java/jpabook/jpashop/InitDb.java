package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
/*
    UserA
        JPA1 BOOK
        JPA2 BOOK
    UserB
        Spring2 BOOK
        Spring2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initServcie;
    @PostConstruct
    public void init(){
        initServcie.dbInit1();
        initServcie.dbInit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit1(){
            Member member=CreateMember("UserA","서울","1","1111");
            em.persist(member);

            Book book = CreateBook("JPA1 BOOK", 10000,100);
            em.persist(book);

            Book book2 = CreateBook("JPA2 BOOK", 20000,100);
            em.persist(book2);

            OrderItem orderItem1=OrderItem.createOrderItem(book,10000,1);
            OrderItem orderItem2=OrderItem.createOrderItem(book2,20000,2);
            Delivery delivery = CreateDelivery(member);
            Order order=Order.createOrder(member,delivery,orderItem1,orderItem2);
            em.persist(order);
        }

        private Delivery CreateDelivery(Member member) {
            Delivery delivery=new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book CreateBook(String name, int price,int Quantity) {
            Book book=new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(100);
            return book;
        }

        public void dbInit2(){
            Member member = CreateMember("UserB","진주","1234","1111");
            em.persist(member);

            Book book = CreateBook("Spring1 BOOK", 20000,200);
            em.persist(book);

            Book book2 = CreateBook("Spring2 BOOK", 40000,300);
            em.persist(book2);

            OrderItem orderItem1=OrderItem.createOrderItem(book,20000,3);
            OrderItem orderItem2=OrderItem.createOrderItem(book2,40000,4);
            Delivery delivery = CreateDelivery(member);
            Order order=Order.createOrder(member,delivery,orderItem1,orderItem2);
            em.persist(order);
        }

        private Member CreateMember(String name,String city,String street,String zipcode) {
            Member member=new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }
    }
}

