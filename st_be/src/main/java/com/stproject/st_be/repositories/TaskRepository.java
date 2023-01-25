package com.stproject.st_be.repositories;

import com.stproject.st_be.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "WHERE o.description LIKE %:description% AND o.completed = :isCompleted AND o.whenToBeDone < :dateBefore "
            +
            "ORDER BY o.id DESC")
    Page<Task> findByDescriptionIgnoreCaseContaining(@Param("description") String description, Pageable pageable,
                                                     @Param("isCompleted") boolean isCompleted, @Param("dateBefore") Date dateBefore);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "WHERE o.completed = :isCompleted AND o.whenToBeDone < :dateBefore " +
            "ORDER BY o.id DESC")
    Page<Task> findAll(Pageable pageable, @Param("isCompleted") boolean isCompleted,
                       @Param("dateBefore") Date dateBefore);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "JOIN o.tenants t " +
            "WHERE o.completed = :isCompleted AND t.id = :tenantid AND o.whenToBeDone < :dateBefore " +
            "GROUP BY o.id " +
            "ORDER BY o.id DESC")
    Page<Task> findAllWhereTenantId(
            @Param("tenantid") String tenantid, Pageable pageable,
            @Param("dateBefore") Date dateBefore, @Param("isCompleted") boolean isCompleted);

    @Query(value = "SELECT o " +
            "FROM Task o " +
            "JOIN o.tenants t " +
            "WHERE o.completed = :isCompleted AND t.id = :tenantid AND o.description LIKE %:description% AND o.whenToBeDone < :dateBefore "
            +
            "GROUP BY o.id " +
            "ORDER BY o.id DESC")
    Page<Task> findAllWhereTenantIdAndDescription(
            @Param("description") String description,
            @Param("tenantid") String tenantid, Pageable pageable,
            @Param("dateBefore") Date dateBefore, @Param("isCompleted") boolean isCompleted);

    @Query(value = "SELECT task " +
            "FROM Task task " +
            "JOIN task.tenants tenants " +
            "WHERE tenants.id = :tenantId AND task.whenToBeDone < task.finishedOnDate ")
    List<Task> findAllOverdueTasksWhereTenantId(@Param("tenantId") String tenantId);
}