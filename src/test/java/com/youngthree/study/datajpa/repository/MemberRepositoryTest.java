package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.dto.MemberDto;
import com.youngthree.study.datajpa.entity.Member;
import com.youngthree.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;

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

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        int age = 10;
        int offset = 0;
        int limit = 3;
        PageRequest request =  PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));


        //when
        Page<Member> byAge = memberRepository.findByAge(age, request);

        //then
        List<Member> members = byAge.getContent();
        Long totalCount = byAge.getTotalElements();
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
        assertThat(byAge.getNumber()).isEqualTo(0);
        assertThat(byAge.isFirst()).isTrue();
        assertThat(byAge.hasNext()).isTrue();

    }

    @Test
    public void bulkAgePlus(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));
        memberRepository.save(new Member("member3",30));
        memberRepository.save(new Member("member4",40));
        Long id = memberRepository.save(new Member("member5",50)).getId();

        //when
        int count = memberRepository.bulkAgePlus(30);

        //then
        Member member = memberRepository.findById(id).get();
        assertThat(count).isEqualTo(3);
        assertThat(member.getAge()).isEqualTo(51);

    }

    //Lazy loading test
    @Test
    public void findMemberLazy(){
        //given
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("member1",10, team1);
        Member member2 = new Member("member2", 10, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when N+1 문제 발생!!
        List<Member> members = memberRepository.findAll();

        for(Member member : members){
            System.out.println("member : "+ member);
            System.out.println("member.team.class : "+ member.getTeam().getClass());
            System.out.println("member.team : "+ member.getTeam().getName());
        }
    }

    @Test
    public void findMemberFetchJoinLazy(){
        //given
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("member1",10, team1);
        Member member2 = new Member("member2", 10, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when 위에 테스트와 비교해 보자 fetch join을 적용해서 N+1 문제를 해결했다.
        List<Member> members = memberRepository.findMemberFetchJoin();

        for(Member member : members){
            System.out.println("member : "+ member);
            System.out.println("member.team.class : "+ member.getTeam().getClass());
            System.out.println("member.team : "+ member.getTeam().getName());
        }
    }

    @Test
    public void findMemberEntityGraphLazy(){
        //given
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("member1",10, team1);
        Member member2 = new Member("member2", 10, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when 위에 테스트와 비교해 보자 entity graph을 적용해서 N+1 문제를 해결했다.
        List<Member> members = memberRepository.findMemberEntityGraph();

        for(Member member : members){
            System.out.println("member : "+ member);
            System.out.println("member.team.class : "+ member.getTeam().getClass());
            System.out.println("member.team : "+ member.getTeam().getName());
        }
    }

    @Test
    public void customRepository(){
        List<Member> allCustom = memberRepository.findAllCustom();
    }

}