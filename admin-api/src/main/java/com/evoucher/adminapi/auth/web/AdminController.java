package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.AdminService;
import com.evoucher.adminapi.auth.service.models.*;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getAdminCurrent() {
        log.info("Get Admin current with token");
        AdminDTO adminDTO = adminService.getAdminCurrent();

        return new ResponseEntity<>(new BaseResponse(adminDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        AdminDTO adminDTO = adminService.findById(id);

        return new ResponseEntity<>(new BaseResponse(adminDTO), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteAdmin(@PathVariable String id) {
        BaseResponse resp = new BaseResponse(adminService.deleteAdmin(id));

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createAdmin(@Valid @RequestBody AdminRequest adminRequest) {
        String id = adminService.createAdmin(adminRequest);

        return new ResponseEntity<>(new BaseResponse(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateAdmin(@PathVariable(value = "id") String id,
                                                    @Valid @RequestBody AdminUpdateRequest adminUpdateRequest) {
        String result = adminService.updateAdmin(id, adminUpdateRequest);

        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchAdmin(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SearchAdminResponse> result = adminService.searchAdmin(filterSearchAuth);
        BaseResponse resp = new BaseResponse(result.getContent());
        resp.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<BaseResponse> changePassword(
            @RequestBody AdminChangePasswordRequest changePasswordRequest) {
        adminService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
    }
}
