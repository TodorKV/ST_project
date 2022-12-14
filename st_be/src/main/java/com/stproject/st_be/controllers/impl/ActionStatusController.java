package com.stproject.st_be.controllers.impl;

import com.ft.controllers.base.BaseAbstrController;
import com.ft.dto.ActionStatusDto;
import com.ft.service.base.ActionStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/action/status")
public class ActionStatusController extends BaseAbstrController<ActionStatusDto> {

    public ActionStatusController(ActionStatusService service) {
        super(service);
    }
}
