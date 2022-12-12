package com.stproject.st_be.repositories;

import com.stproject.st_be.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "WHERE o.description LIKE %:description% AND o.completed = :isCompleted AND o.whenToBeDone < :dateBefore " +
            "ORDER BY o.id DESC")
    Page<Task> findByDescriptionIgnoreCaseContaining(String description, Pageable pageable, boolean isCompleted, Date dateBefore);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "WHERE o.completed = :isCompleted AND o.whenToBeDone < :dateBefore " +
            "ORDER BY o.id DESC")
    Page<Task> findAll(Pageable pageable, boolean isCompleted, Date dateBefore);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "JOIN o.tenants t " +
            "WHERE o.completed = :isCompleted AND t.id = :tenantid AND o.whenToBeDone < :dateBefore " +
            "GROUP BY o.id " +
            "ORDER BY o.id DESC")
    Page<Task> findAllWhereTenantId(String tenantid, Pageable pageable, Date dateBefore, boolean isCompleted);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "JOIN o.tenants t " +
            "WHERE o.completed = :isCompleted AND t.id = :tenantid AND o.description LIKE %:description% AND o.whenToBeDone < :dateBefore " +
            "GROUP BY o.id " +
            "ORDER BY o.id DESC")
    Page<Task> findAllWhereTenantIdAndDescription(String description, String tenantid, Pageable pageable, Date dateBefore, boolean isCompleted);
}
