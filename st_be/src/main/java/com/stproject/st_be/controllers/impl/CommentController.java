package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.controllers.base.BaseAbstrController;
import com.stproject.st_be.dto.CommentDto;
import com.stproject.st_be.services.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController extends BaseAbstrController<CommentDto> {

    public CommentController(BaseService<CommentDto> service) {
        super(service);
    }
}
