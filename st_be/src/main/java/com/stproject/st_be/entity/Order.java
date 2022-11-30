package com.stproject.st_be.entity;

import java.util.*;

import javax.persistence.*;

import com.stproject.st_be.entity.base.BaseEntity;
import com.stproject.st_be.entity.enums.Priority;
import com.stproject.st_be.utils.ActionStatusComparator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column
    private String orderNumber;

    @Column
    private Date whenToBeDone;

    @Column
    private String description;

    @Column
    private boolean completed;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Set<Photo> photos = new HashSet<>();

    @Fetch(value = FetchMode.JOIN)
    @OneToMany(targetEntity = ActionStatus.class, mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true)
    @SortComparator(ActionStatusComparator.class)
    private Set<ActionStatus> actionStatuses = new HashSet<>();

}
