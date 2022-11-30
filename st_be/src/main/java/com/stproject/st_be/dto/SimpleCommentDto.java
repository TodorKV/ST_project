package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SimpleCommentDto extends BaseDto {
    private String timeSent;
    private String comment;
    private TenantDto tenant;
}
