package com.stproject.st_be.utils;

import com.stproject.st_be.entity.Task;

import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;

public class OverdueTaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        Period period1 = Period.between(o1.getWhenToBeDone().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                o1.getFinishedOnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        Period period2 = Period.between(o2.getWhenToBeDone().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                o2.getFinishedOnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        if (period2.getDays() > period1.getDays()) {
            return 1;
        } else if (period2.getDays() < period1.getDays()) {
            return -1;
        } else {
            return 0;
        }
    }
}
