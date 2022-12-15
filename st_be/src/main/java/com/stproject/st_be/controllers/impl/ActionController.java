package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.services.impl.ActionServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/action")
public class ActionController {

    private final ActionServiceImpl service;

    public ActionController(ActionServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ActionDto> save(@RequestBody ActionDto dto, @PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(service.save(dto, productId), HttpStatus.CREATED);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<ActionDto> edit(@PathVariable("id") Integer id, @RequestBody ActionDto dto) {
        return new ResponseEntity<>(service.edit(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
