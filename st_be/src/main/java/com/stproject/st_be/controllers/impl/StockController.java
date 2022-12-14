package com.stproject.st_be.controllers.impl;

import com.ft.controllers.base.BaseAbstrController;
import com.ft.dto.StockDto;
import com.ft.service.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stock")
public class StockController extends BaseAbstrController<StockDto> {
    public StockController(BaseService<StockDto> service) {
        super(service);
    }
}
