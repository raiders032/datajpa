package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member findOne(Long memberId){
        return em.find(Member.class,memberId);
    }



}
