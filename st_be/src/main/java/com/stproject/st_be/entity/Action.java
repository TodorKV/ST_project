package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
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
@Table(name = "actions") // , indexes = @Index(name = "step_index", columnList = "step", unique = false))
@NoArgsConstructor
@AllArgsConstructor
public class Action extends BaseEntity {

    @Column
    private String name;

    @Column(name = "expected_minutes")
    private int expectedMinutes;

    @Column(name = "step")
    private int step;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(targetEntity = ActionStatus.class, mappedBy = "action", fetch = FetchType.EAGER)
    private Set<ActionStatus> actionStatuses = new HashSet<>();

    // @ManyToMany
    // @JoinTable(name = "actions_tenants", joinColumns = @JoinColumn(name =
    // "action_id", referencedColumnName = "id"), inverseJoinColumns =
    // @JoinColumn(name = "tenant_id", referencedColumnName = "id"))
    // private Set<Tenant> tenants = new HashSet<Tenant>();

}
