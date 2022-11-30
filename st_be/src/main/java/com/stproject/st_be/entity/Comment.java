package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Column(name = "time_sent")
    private String timeSent;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(targetEntity = ActionStatus.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "action_status_id", referencedColumnName = "id")
    private ActionStatus actionStatus;

    @ManyToOne(targetEntity = Tenant.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;
}