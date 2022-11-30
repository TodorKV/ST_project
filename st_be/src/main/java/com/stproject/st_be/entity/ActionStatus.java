package com.stproject.st_be.entity;

import com.stproject.st_be.entity.base.BaseEntity;
import com.stproject.st_be.entity.enums.Progress;
import com.stproject.st_be.entity.enums.TimeTakenState;
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
@Table(name = "actionstatuses")
@NoArgsConstructor
@AllArgsConstructor
public class ActionStatus extends BaseEntity implements Comparable<ActionStatus> {

    @Column(name = "time_begin")
    private Date timeBegin;

    @Column(name = "time_end")
    private Date timeEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_taken_state")
    private TimeTakenState timeTakenState;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress")
    private Progress progress = Progress.NOT_STARTED;

    @OneToMany(targetEntity = Comment.class, mappedBy = "actionStatus", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("timeSent DESC")
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(targetEntity = Action.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    private Action action;

    @ManyToMany
    @JoinTable(name = "actionstatuses_tenants", joinColumns = @JoinColumn(name = "actionstatuses_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"))
    private Set<Tenant> tenants = new HashSet<Tenant>();

    @Override
    public int compareTo(ActionStatus o) {
        int current = action.getStep();
        int other = o.getAction().getStep();
        if (current == other)
            return 0;
        else if (current < other)
            return -1;
        else
            return 1;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /*
         * Check if o is an instance of ActionStatus or not
         * "null instanceof [type]" also returns false
         */
        if (!(o instanceof ActionStatus)) {
            return false;
        }

        // typecast o to ActionStatus so that we can compare data members
        ActionStatus ase = (ActionStatus) o;

        // Compare the data members and return accordingly
        return this.getAction().getId().equals(ase.getAction().getId());
    }

}
