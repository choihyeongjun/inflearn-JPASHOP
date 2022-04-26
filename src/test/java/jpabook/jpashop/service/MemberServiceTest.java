package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberServcie;
    @Autowired
    MemberRepository memberRepositroy;
    @Autowired
    EntityManager em;

    @Test//회원가입
    public void 회원가입()throws Exception{
        //given
        Member member=new Member();
        member.setName("kim");
        //when
        Long saveId=memberServcie.join(member);

        assertEquals(member,memberRepositroy.findOne(saveId));
        //then
    }
    @Test(expected = IllegalStateException.class)
    public void 중복회원예외()throws Exception{
        //given
        Member member1=new Member();
        member1.setName("kim");
        Member member2=new Member();
        member2.setName("kim");

        //when
        memberServcie.join(member1);
        memberServcie.join(member2);


        //then
        fail("예외발생함");
    }

}