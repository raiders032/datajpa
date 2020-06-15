package com.youngthree.study.datajpa.repository;

import com.youngthree.study.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
