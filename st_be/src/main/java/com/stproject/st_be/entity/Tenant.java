package com.stproject.st_be.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tenants")
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    private String id;

    @Column(name = "tenant_value")
    private String tenantValue;

    @OneToOne(mappedBy = "tenant", fetch = FetchType.EAGER)
    private User user;

    @OneToMany(targetEntity = Comment.class, mappedBy = "tenant", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(mappedBy = "tenants")
    private Set<ActionStatus> actions = new HashSet<>();

    @ManyToMany(mappedBy = "tenants")
    private Set<Task> tasks = new HashSet<>();
}
