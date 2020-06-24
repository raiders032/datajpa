package com.youngthree.study.datajpa.controller;

import com.youngthree.study.datajpa.dto.MemberDto;
import com.youngthree.study.datajpa.entity.Member;
import com.youngthree.study.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    //도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id){
        return memberRepository.findById(id).get().getUsername();
    }

    //도메인 클래스 컨버터 사용
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    //entity 직접 노출
    ///members?page=0&size=3&sort=id,desc&sort=username,desc
    @GetMapping("/members")
    public Page<Member> members(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    //page dto로 변환
    @GetMapping("/members2")
    public Page<MemberDto> members2(Pageable pageable){
        Page<Member> all = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = all.map(MemberDto::new);
        return pageDto;
    }

    @PostConstruct
    public void createMembers(){
        for(int i=0;i<100;i++){
            memberRepository.save(new Member("user"+i,i));
        }
    }

}
