package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.dto.MemberDto;
import com.youngthree.study.datajpa.entity.Member;
import com.youngthree.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testMember(){
        //given
        Member member = new Member("nys");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll(); assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("nys",10);
        Member m2 = new Member("nys",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findList = memberRepository.findByUsernameAndAgeGreaterThan("nys",15);
        Member findMember = findList.get(0);

        Assertions.assertThat(findMember.getAge()).isEqualTo(20);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("nys");
        Assertions.assertThat(findList.size()).isEqualTo(1);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan_query(){
        Member m1 = new Member("nys",10);
        Member m2 = new Member("nys",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findList = memberRepository.findUser("nys",15);
        Member findMember = findList.get(0);

        Assertions.assertThat(findMember.getAge()).isEqualTo(20);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("nys");
        Assertions.assertThat(findList.size()).isEqualTo(1);
    }

    @Test
    public void findMemberList(){
        Member m1 = new Member("nys",10);
        Member m2 = new Member("bhk",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> findList = memberRepository.findMemberList();
        for(String username :findList){
            System.out.println(username);
        }
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("nys",10,team);
        memberRepository.save(member);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        MemberDto memberDto = memberDtos.get(0);

        assertThat(memberDto.getId()).isEqualTo(member.getId());
        assertThat(memberDto.getUsername()).isEqualTo(member.getUsername());
        assertThat(memberDto.getTeamName()).isEqualTo(member.getTeam().getName());
    }


    @Test
    public void findByName(){
        Member m1 = new Member("nys",10);
        Member m2 = new Member("bhk",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findList = memberRepository.findByName(Arrays.asList("nys","bhk"));
        for(Member member :findList){
            System.out.println(member.getUsername());
        }
    }


}