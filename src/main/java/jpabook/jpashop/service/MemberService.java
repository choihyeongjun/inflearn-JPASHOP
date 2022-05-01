package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)

public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    //최신 스프링은 서비스안에 객체 생성자하나만 있으면 autowired 없어도 알아서 주입해줌
    /*public MemberService(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }*/ //requiredArgConstructor 해주면 알아서 생성자 만들어줌
    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers=memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미가입된 회원입니다");
        }
    }

    //회원전체조회

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
    @Transactional
    public void update(Long id,String name){
        Member member=memberRepository.findOne(id);
        member.setName(name);
    }
}
