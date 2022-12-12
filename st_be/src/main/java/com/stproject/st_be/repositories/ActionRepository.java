package com.stproject.st_be.repositories;

import com.stproject.st_be.entity.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {
    Page<Action> findByNameIgnoreCaseContaining(@RequestParam("name") String name, Pageable pageable);
}