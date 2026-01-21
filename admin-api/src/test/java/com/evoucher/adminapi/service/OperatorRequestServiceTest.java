package com.evoucher.adminapi.service;

import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.mapper.OperatorRequestMapper;
import com.evoucher.adminapi.admin.mapper.OperatorRequestMapperImpl;
import com.evoucher.adminapi.admin.service.OperatorLogicalService;
import com.evoucher.adminapi.admin.service.OperatorRequestBasicService;
import com.evoucher.adminapi.admin.service.VoucherBasicService;
import com.evoucher.adminapi.admin.service.models.OperatorRequestDto;
import com.evoucher.adminapi.admin.service.models.VoucherDto;
import com.evoucher.adminapi.auth.service.AuthService;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.enums.VoucherStatusCode;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class OperatorRequestServiceTest {
    @Mock
    private OperatorRequestBasicService basicService;
    @Mock
    private VoucherBasicService voucherService;
    @Spy
    private OperatorRequestMapper mapper = new OperatorRequestMapperImpl();
    @Mock
    private AuthService authService;
    @InjectMocks
    @Spy
    private OperatorLogicalService service;

    private VoucherDto voucher;
    private OperatorRequestDto request;
    private final String savedVoucherEv = "test-ev";
    private final Long savedRequestId = 1L;

    @BeforeEach
    void setup() {
        voucher = new VoucherDto();
        voucher.setEV(savedVoucherEv);
        request = new OperatorRequestDto();
        request.setEv(savedVoucherEv);
        lenient().when(voucherService.findDtoById(anyString())).thenReturn(voucher);
        lenient().when(authService.getLoggedInUserId()).thenReturn("1");
    }

    @Nested
    class validationFailTest {

        @Test
        @DisplayName("throw CustomCodeException when exist request under approval of request ev")
        void createNew_existRequestUnderApproval_throwCustomCodeException() {
            Long requestingCount = 1L;

            doNothing().when(service).validateExtendableVoucher(any());
            when(basicService.countAllByEvAndStatus(anyString(), any())).thenReturn(requestingCount);

            request.setMemo("testMemo");
            CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
                service.createNew(request);
            });
            Assertions.assertEquals("Request is under approval", exception.getMessage());
        }


        @ParameterizedTest
        @EnumSource(value = VoucherStatusCode.class, names = {"USED", "EXPIRE", "DISABLED"})
        @DisplayName("throw CustomCodeException when voucher status is not allowed to extend")
        void createNew_voucherStatusIsNotAllowed_throwCustomCodeException(VoucherStatusCode status) {
            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(status.name());

            request.setMemo("testMemo");
            CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> service.createNew(request));
            Assertions.assertEquals("Voucher in this status can not be extended.", exception.getMessage());
        }


        @ParameterizedTest
        @EnumSource(value = VoucherStatusCode.class, names = {"USED", "EXPIRE", "DISABLED"})
        @DisplayName("validate vouchers status when approve request")
        void approveRequest_voucherStatusIsNotAllowed_throwCustomCodeException(VoucherStatusCode status) {
            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(status.name());

            request.setReqId(savedRequestId);
            request.setApproveMemo("approve memo");
            request.setReqStatus(OperatorRequestStatus.APPROVED);

            OperatorRequestDto requested = OperatorRequestDto.builder()
                    .reqId(savedRequestId)
                    .ev(savedVoucherEv)
                    .reqStatus(OperatorRequestStatus.REQUESTED)
                    .build();

            when(basicService.findDtoById(savedRequestId)).thenReturn(requested);

            CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> service.approveRequest(request));
            Assertions.assertEquals("Voucher in this status can not be extended.", exception.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = OperatorRequestStatus.class, names = {"APPROVED", "REJECTED"})
        @DisplayName("validate created request status is invalid")
        void approveRequest_createdRequestIsInvalid_throwCustomCodeException(OperatorRequestStatus status) {
            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(status.name());

            OperatorRequestDto requested = OperatorRequestDto.builder()
                    .reqId(savedRequestId)
                    .ev(savedVoucherEv)
                    .reqStatus(status)
                    .build();

            //
            request.setReqId(savedRequestId);
            request.setApproveMemo("approve memo");
            request.setReqStatus(OperatorRequestStatus.APPROVED);

            when(basicService.findDtoById(savedRequestId)).thenReturn(requested);

            CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> service.approveRequest(request));
            Assertions.assertEquals(String.format("Invalid Status %s", status.name()), exception.getMessage());
        }
    }

    @Nested
    class SuccessTest {
        @ParameterizedTest
        @EnumSource(value = VoucherStatusCode.class, names = {"NORMAL", "PART_USED"})
        @DisplayName("make new request successfully")
        void createNew_successFullFlow_triggerRepositorySave(VoucherStatusCode statusCode) {
            request.setEv(savedVoucherEv);
            request.setMemo("test");

            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(statusCode.name());
            voucher.setSystemType(SystemType.INTERNAL);
            //set voucher expire date to tomorrow
            voucher.setExpirationDate(DateUtils.extendDays(new Date(), 1));
            when(basicService.saveDto(any())).then(invocationOnMock -> {
                OperatorRequestDto result = invocationOnMock.getArgument(0);
                result.setReqId(savedRequestId);
                return result;
            });


            BaseResponse response = service.createNew(request);
            Assertions.assertEquals(savedRequestId, response.getData());
        }

        @DisplayName("voucher expire date is changed")
        @Test
        void approveRequest_validInput_voucherExpireDateChanged() {
            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(VoucherStatusCode.NORMAL.name());
            voucher.setSystemType(SystemType.INTERNAL);

            OperatorRequestDto requested = OperatorRequestDto.builder()
                    .reqId(savedRequestId)
                    .ev(savedVoucherEv)
                    .reqStatus(OperatorRequestStatus.REQUESTED)
                    .build();

            //
            request.setReqId(savedRequestId);
            request.setApproveMemo("approve memo");
            request.setReqStatus(OperatorRequestStatus.APPROVED);

            when(basicService.findDtoById(savedRequestId)).thenReturn(requested);
            doNothing().when(voucherService).extendVoucherExpiredDate(anyString(), any());


            service.approveRequest(request);
            verify(voucherService, times(1)).extendVoucherExpiredDate(anyString(), any());
        }
        @DisplayName("voucher expire date is changed")
        @Test
        void rejectRequest_validInput_voucherExpireDateNoChanged() {
            voucher.setEV(savedVoucherEv);
            voucher.setVoucherStatusCode(VoucherStatusCode.NORMAL.name());
            voucher.setSystemType(SystemType.INTERNAL);

            OperatorRequestDto requested = OperatorRequestDto.builder()
                    .reqId(savedRequestId)
                    .ev(savedVoucherEv)
                    .reqStatus(OperatorRequestStatus.REQUESTED)
                    .build();

            //
            request.setReqId(savedRequestId);
            request.setApproveMemo("approve memo");
            request.setReqStatus(OperatorRequestStatus.REJECTED);

            when(basicService.findDtoById(savedRequestId)).thenReturn(requested);


            service.approveRequest(request);
            verify(voucherService, times(0)).extendVoucherExpiredDate(anyString(), any());
        }
    }



}
