package com.stproject.st_be.utils;

import java.util.Comparator;

import com.stproject.st_be.entity.ActionStatus;

public class ActionStatusComparator implements Comparator<ActionStatus> {

    @Override
    public int compare(ActionStatus arg0, ActionStatus arg1) {
        int current = arg0.getAction().getStep();
        int other = arg1.getAction().getStep();
        if (current == other)
            return 0;
        else if (current < other)
            return -1;
        else
            return 1;
    }

}