package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="deliver_id")
    private Long id;

    @OneToOne(mappedBy="delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)//Ordinal 사용시 중간에 새로운 컬럼 추가되더라도 괜찮음 아니면 DB값 다 변경
    private DeliveryStatus status;//READY,COMP
}
