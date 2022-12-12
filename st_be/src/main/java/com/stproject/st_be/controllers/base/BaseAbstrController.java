package com.stproject.st_be.controllers.base;

import com.stproject.st_be.services.base.BaseService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class BaseAbstrController<D> {

    protected final BaseService<D> service;

    public BaseAbstrController(BaseService<D> service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<D> save(@RequestBody D dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<D>> getAllItemsPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<D> list = service.getAllPaginated(pageNo, pageSize, sortBy);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<D>> searchByDescriptionPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String name) {
        Page<D> list = this.service.searchByNamePaginated(name, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<D> edit(@PathVariable("id") Integer id, @RequestBody D dto) {
        return new ResponseEntity<>(service.edit(id, dto), HttpStatus.OK);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<D> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

}
