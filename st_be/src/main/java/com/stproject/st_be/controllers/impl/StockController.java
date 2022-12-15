package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.controllers.base.BaseAbstrController;
import com.stproject.st_be.dto.StockDto;
import com.stproject.st_be.services.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stock")
public class StockController extends BaseAbstrController<StockDto> {
    public StockController(BaseService<StockDto> service) {
        super(service);
    }
}
