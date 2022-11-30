package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "photos")
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends BaseEntity {

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "secure_url")
    private String secureUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_for_completed_product")
    private boolean isForCompletedProduct;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
}
