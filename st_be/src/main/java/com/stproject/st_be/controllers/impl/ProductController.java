package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.controllers.base.BaseAbstrController;
import com.stproject.st_be.dto.ProductDto;
import com.stproject.st_be.services.base.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
public class ProductController extends BaseAbstrController<ProductDto> {

    public ProductController(ProductService service) {
        super(service);
    }

}
