package com.stproject.st_be.utils;

import java.util.Comparator;

import com.stproject.st_be.dto.ActionStatusDto;

public class ActionStatusDtoComparator implements Comparator<ActionStatusDto> {

    @Override
    public int compare(ActionStatusDto arg0, ActionStatusDto arg1) {
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