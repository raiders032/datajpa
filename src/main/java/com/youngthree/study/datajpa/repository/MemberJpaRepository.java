package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class,id);
        return Optional.ofNullable(member);
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }

    public Long count(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long memberId){
        return em.find(Member.class,memberId);
    }

}
