package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.dto.MemberDto;
import com.youngthree.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age > :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m ")
    List<String> findMemberList();

    @Query("select new com.youngthree.study.datajpa.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByName(@Param("names")Collection<String> names);

    Page<Member> findByAge(int age, Pageable pageable);

    //countQuery를 분리할 수 있음
    @Query(value="select m from Member m left join m.team",
            countQuery="select m from Member m"
    )
    Page<Member> findAllCountBy(Pageable pagealbe);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age=m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();
}