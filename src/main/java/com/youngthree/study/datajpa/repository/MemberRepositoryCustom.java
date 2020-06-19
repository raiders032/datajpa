package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findAllCustom();
}
