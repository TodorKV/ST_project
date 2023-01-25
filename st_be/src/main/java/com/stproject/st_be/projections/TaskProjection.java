package com.stproject.st_be.projections;

import java.sql.Date;

public interface TaskProjection {
    String getDescription();
    Date getWhenToBeDone();
    Date getFinishedOnDate();
    String getUsername();
}
