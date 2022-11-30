package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    @Column
    private Date whenToBeDone;

    @Column
    private String description;

    @Column
    private boolean completed;

    @ManyToMany
    @JoinTable(name = "tasks_tenants", joinColumns = @JoinColumn(name = "tasks_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"))
    private Set<Tenant> tenants = new HashSet<Tenant>();
}
