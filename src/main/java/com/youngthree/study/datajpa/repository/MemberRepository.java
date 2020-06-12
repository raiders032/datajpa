package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
