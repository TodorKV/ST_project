package com.stproject.st_be.controllers.impl;

import com.ft.dto.OrderDto;
import com.ft.dto.PhotoDto;
import com.ft.service.base.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/order")
public class UserOrderController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserOrderController.class);

    private final OrderService service;

    public UserOrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getByTenantId(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<OrderDto> list = this.service.getAllWhereTenantId(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<OrderDto>> searchByDescriptionPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String description) {
        Page<OrderDto> list = this.service.searchAllWhereTenantIdAndDescription(description, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping(value = "photos/{id}")
    public ResponseEntity addCompletedPhotos(@PathVariable("id") Integer id,
            @RequestBody List<PhotoDto> photos) {
        OrderDto savedOrder = this.service.findById(id);

        Set<PhotoDto> currentPhotos = savedOrder.getPhotos();
        currentPhotos.addAll(photos);
        savedOrder.setPhotos(currentPhotos);
        this.service.save(savedOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}