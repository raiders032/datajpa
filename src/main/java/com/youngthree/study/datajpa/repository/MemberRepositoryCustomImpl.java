package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findAllCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
