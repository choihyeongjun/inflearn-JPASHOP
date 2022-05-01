package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findmembers=memberService.findMembers();
        List<MemberDto>collect=findmembers.stream()
                .map(m->new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{//result라는 껍데기 씌워줘야함 리스트를 컬렉션으로 바로 씌워주면 json배열 타입으로 바로날아가기때문에
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }
    @PostMapping("/api/v1/members") //responseBody Json형태로 데이터 받기위해 사용
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){//v1같은경우 member객체안에 이름 바뀌면 찾기힘들다 그거를 방지하기 위해 v2처럼 하나의 DTO만들어서 적용
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);
    }
    //Entity를 외부에 노출하거나 직접가져와서 사용해서는 안된다!!
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member=new Member();
        member.setName(request.getName());
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());
        Member findMember=memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }
    @Data
    static class CreateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;
        CreateMemberResponse(Long id){
            this.id=id;
        }
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
