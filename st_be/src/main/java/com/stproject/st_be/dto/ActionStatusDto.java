package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;
import com.stproject.st_be.entity.enums.Progress;
import com.stproject.st_be.entity.enums.TimeTakenState;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActionStatusDto extends BaseDto implements Comparable<ActionStatusDto> {

    private TimeTakenState timeTakenState;
    private Progress progress;
    private Set<SimpleCommentDto> comments;
    private Date timeBegin;
    private Date timeEnd;
    private Set<TenantDto> tenants = new HashSet<TenantDto>();
    private ActionDto action;
    private Boolean isReadyForWork = false;
    private boolean isLastAction = false;

    @Override
    public int compareTo(ActionStatusDto o) {
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
         * Check if o is an instance of ActionStatusDto or not
         * "null instanceof [type]" also returns false
         */
        if (!(o instanceof ActionStatusDto)) {
            return false;
        }

        // typecast o to ActionStatusDto so that we can compare data members
        ActionStatusDto asd = (ActionStatusDto) o;

        // Compare the data members and return accordingly
        return this.getAction().getId().equals(asd.getAction().getId());
    }
}
