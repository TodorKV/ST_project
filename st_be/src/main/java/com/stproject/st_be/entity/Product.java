package com.stproject.st_be.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.stproject.st_be.entity.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany(targetEntity = Action.class, mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("step ASC")
    private Set<Action> actions = new HashSet<>();

    @OneToMany(targetEntity = Order.class, mappedBy = "product", fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();
}
