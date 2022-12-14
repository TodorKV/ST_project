package ccom.stproject.st_be.controllers.impl;

import com.ft.controllers.base.BaseAbstrController;
import com.ft.dto.CommentDto;
import com.ft.service.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController extends BaseAbstrController<CommentDto> {

    public CommentController(BaseService<CommentDto> service) {
        super(service);
    }
}
