package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

}
