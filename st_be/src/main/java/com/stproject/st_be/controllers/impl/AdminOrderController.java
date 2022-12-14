package com.stproject.st_be.controllers.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ft.dto.*;
import com.ft.entity.enums.Progress;
import com.ft.exception.MyFileNotFoundException;
import com.ft.service.base.ActionStatusService;
import com.ft.service.base.OrderService;
import com.ft.service.base.ProductService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This is order controller for admin endpoints only
 */
@RestController
@RequestMapping("api/v3/order")
public class AdminOrderController {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminOrderController.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final OrderService orderService;
    private final ActionStatusService actionStatusService;
    private final ProductService productService;
    private final Cloudinary cloudinary;

    public AdminOrderController(OrderService orderService,
            ActionStatusService actionStatusService,
            ProductService productService) {
        this.productService = productService;
        this.actionStatusService = actionStatusService;
        this.orderService = orderService;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dg06tqxmp",
                "api_key", "648737837199464",
                "api_secret", "lsAz0bBPAgcaq2dTROrypyW-wLI",
                "secure", true));
    }

    /**
     * Create an order and assemble the action status entities.
     *
     * @return
     * @throws IOException
     */
    @SneakyThrows
    @PostMapping()
    public ResponseEntity<OrderDto> save(@RequestBody OrderDto dto) {

        ProductDto productDto = productService.findById(dto.getProduct().getId());
        dto.setProduct(productDto);

        OrderDto savedOrder = orderService.save(dto);

        int idx = 0;
        for (ActionDto a : savedOrder.getProduct().getActions()) {
            ActionStatusDto actionStatusDto = new ActionStatusDto();
            actionStatusDto.setProgress(Progress.NOT_STARTED);
            TenantDto tenantDto = new TenantDto();
            tenantDto.setId(dto.getTenantIds().get(idx++));
            Set<TenantDto> tenantset = new HashSet<>();
            tenantset.add(tenantDto);
            actionStatusDto.setTenants(tenantset);
            actionStatusService.save(actionStatusDto, a, savedOrder);
            // this.wsService.notifyUser(tenantDto.getId().replace("_id", ""), "Поръчка с
            // номер " + savedOrder.getId());
        }

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllItemsPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam boolean finished) {
        Page<OrderDto> list = orderService.getAllPaginated(pageNo, pageSize, sortBy,
                finished);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<OrderDto>> searchByDescriptionPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String description,
            @RequestParam boolean finished) {
        Page<OrderDto> list = this.orderService.searchByDescriptionPaginated(description, pageNo, pageSize,
                sortBy,
                finished);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        orderService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<OrderDto> edit(@PathVariable("id") Integer id,
            @RequestBody OrderDto dto) {
        return new ResponseEntity<>(orderService.edit(id, dto), HttpStatus.OK);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    public Resource loadFileAsResource(String fileName) {
        Path fileStorageLocation = Paths.get("src/main/resources/order-photos/").toAbsolutePath()
                .normalize();
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Scheduled(cron = "0 0/20 0 * * *")
    void removeOrderAndPhotos() {
        List<Integer> ids = new ArrayList<>();
        this.orderService.findAllCompleted().forEach(o -> {
            int lastStep = Integer.MIN_VALUE;
            Date endDate = new Date();
            for (ActionStatusDto actionStatus : o.getActionStatuses()) {
                if (actionStatus.getAction().getStep() > lastStep) {
                    lastStep = actionStatus.getAction().getStep();
                    endDate = actionStatus.getTimeEnd();
                }
            }
            if (endDate != null) {
                long diff = endDate.getTime() - new Date().getTime();
                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 7) {
                    ids.add(o.getId());
                    o.getPhotos().forEach(photo -> {
                        try {
                            this.cloudinary.uploader().destroy(photo.getPublicId(),
                                    ObjectUtils.emptyMap());
                        } catch (Exception e) {
                            this.LOGGER.error("Couldn't delete cloudinary image", e);
                        }
                    });
                    this.LOGGER.info("SchedeluedPhotoDeleter",
                            "Successful deletion of photos in folder:" + o.getId().toString());
                    try {
                        this.cloudinary.uploader().destroy(o.getId().toString(),
                                ObjectUtils.emptyMap());
                    } catch (Exception e) {
                        this.LOGGER.error("Couldn't delete cloudinary folder", e);
                    }
                    this.LOGGER.info("SchedeluedPhotoDeleter",
                            "Successful deletion of folder:" + o.getId().toString());
                }
            }
        });
        ids.forEach(id -> {
            this.orderService.deleteById(id);
            this.LOGGER.info("ScheduledOrderDelter", "Successful deletion of order with id:" + id);
        });

    }
}