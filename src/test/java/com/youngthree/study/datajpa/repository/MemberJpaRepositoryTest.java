package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testMember(){
        //given
        Member member = new Member("nys");
        Member savedMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll(); assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("nys",10);
        Member m2 = new Member("nys",20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> findList = memberJpaRepository.findByUsernameAndAgeGreaterThan("nys",15);
        Member findMember = findList.get(0);

        Assertions.assertThat(findMember.getAge()).isEqualTo(20);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("nys");
        Assertions.assertThat(findList.size()).isEqualTo(1);
    }

    @Test
    public void paging() throws Exception {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        int age = 10;
        int offset = 0;
        int limit = 3;
        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ... // 최초 페이지 ..

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkAgePlus(){
        //given
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",20));
        memberJpaRepository.save(new Member("member3",30));
        memberJpaRepository.save(new Member("member4",40));
        Long id = memberJpaRepository.save(new Member("member5",50)).getId();

        //when
        int count = memberJpaRepository.bulkAgePlus(30);
        em.clear();

        //then
        Member member = memberJpaRepository.findById(id).get();
        assertThat(count).isEqualTo(3);
        assertThat(member.getAge()).isEqualTo(51);

    }
}