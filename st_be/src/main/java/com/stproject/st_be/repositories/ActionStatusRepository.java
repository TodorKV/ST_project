package com.stproject.st_be.repositories;

import com.stproject.st_be.entity.ActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionStatusRepository extends JpaRepository<ActionStatus, Integer> {

}
