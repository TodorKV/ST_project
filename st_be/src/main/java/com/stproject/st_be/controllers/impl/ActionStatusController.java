package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.controllers.base.BaseAbstrController;
import com.stproject.st_be.dto.ActionStatusDto;
import com.stproject.st_be.services.base.ActionStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/action/status")
public class ActionStatusController extends BaseAbstrController<ActionStatusDto> {

    public ActionStatusController(ActionStatusService service) {
        super(service);
    }
}
