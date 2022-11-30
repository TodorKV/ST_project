package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "category")
    private String category;
}
