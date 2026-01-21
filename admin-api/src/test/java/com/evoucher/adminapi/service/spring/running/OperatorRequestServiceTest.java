package com.evoucher.adminapi.service.spring.running;

import com.evoucher.adminapi.AdminApiApplication;
import com.evoucher.adminapi.admin.dao.OperatorRequestRepository;
import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.service.OperatorLogicalService;
import com.evoucher.adminapi.admin.service.OperatorRequestBasicService;
import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.OperatorRequestDto;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdminApiApplication.class)
@Slf4j
@EnabledIfEnvironmentVariable(named = "TEST_TYPE", matches = "FULL_TEST")
public class OperatorRequestServiceTest {
    @Autowired
    private OperatorRequestRepository repository;
    @Autowired
    private OperatorLogicalService service;
    @Autowired
    private OperatorRequestBasicService basicService;

    private String savedEv = "test-ev";

    @Test
    void search_emptyDb_returnEmptyResult() {
        FilterSearchAdmin filter = FilterSearchAdmin.builder().build();
        BaseResponse response = service.searchOperatorRequest(filter, null, null);
        Assertions.assertThat(response.getData()).isNull();
        Assertions.assertThat(response.getTotalCount()).isEqualTo(0);
    }

    @Test
    void search_allApproved_returnEmptyResult() {
        OperatorRequestDto approved = OperatorRequestDto.builder()
                .ev(savedEv)
                .memo("test")
                .approver("1")
                .requester("1")
                .approveMemo("test")
                .reqStatus(OperatorRequestStatus.APPROVED)
                .build();
        OperatorRequestDto requested = OperatorRequestDto.builder()

                .build();
    }

}
