package com.stproject.st_be.controllers.impl;

import com.ft.controllers.base.BaseAbstrController;
import com.ft.dto.ProductDto;
import com.ft.service.base.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
public class ProductController extends BaseAbstrController<ProductDto> {

    public ProductController(ProductService service) {
        super(service);
    }

}
