package com.evoucher.adminapi.service;

import com.evoucher.adminapi.admin.dao.EVoucherRepository;
import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.PublishDetailService;
import com.evoucher.adminapi.admin.service.PublishServiceImpl;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.admin.service.publish.impl.*;
import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.config.PublishHandlingStrategyFactory;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PublishServiceTest {
    @InjectMocks
    private PublishServiceImpl publishService;
    @Mock
    private PublishDetailService publishDetailService;
    @Mock
    private EVoucherRepository eVoucherRepository;
    private PropertyConverter propertyConverter = new PropertyConverter();

    @Mock
    private PublishHandlingStrategyFactory serviceFactory;
    private DeliveryBridgeService bridgeService = new DeliveryBridgeService(publishDetailService, propertyConverter, eVoucherRepository, publishService);
    private EmailTypedPublish emailDeliveryService = new EmailTypedPublish(bridgeService);
    private SmsTypedPublish smsDeliveryService = new SmsTypedPublish(bridgeService);
    private DownloadTypedPublish downloadDeliveryService = new DownloadTypedPublish(bridgeService);
    private PaperTypedPublish paperDeliveryService = new PaperTypedPublish(bridgeService);


    @BeforeEach
    void beforeEach() {
        propertyConverter.setKey("XaYbCz3579CzXaYb0246813579aBcDeF");
        lenient().doAnswer(invocationOnMock -> {
            SMSType type = invocationOnMock.getArgument(0);
            switch (type) {
                case DOWNLOAD:
                    return downloadDeliveryService;
                case SMS:
                    return smsDeliveryService;
                case PAPER:
                    return paperDeliveryService;
                case EMAIL:
                    return emailDeliveryService;
                default:
                    log.error("{} type is not supported", type);
                    throw new CustomCodeException(
                            type + " type is not supported", HttpStatus.INTERNAL_SERVER_ERROR
                    );
            }
        }).when(serviceFactory).getStrategy(any(SMSType.class));

    }

    @Nested
    class validateForCreatingAndUpdating {
        @DisplayName("missing number user of publish for download, paper type")
        @ParameterizedTest
        @EnumSource(value = SMSType.class, names = {"DOWNLOAD", "PAPER"})
        void validate_missingNumberOfUser_throwCustomCodeException(SMSType type) {
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setSenderName("senderName Test");
            publishRequest.setApproveStatusCode(ApproveStatus.REQ);
            publishRequest.setSmsType(type);

            CustomCodeException exception =
                    Assertions.assertThrows(
                            CustomCodeException.class,
                            () -> publishService.validatePublishRequest(publishRequest));

            assertEquals(MessageUtils.getMessage("evoucher.publish.empty.numberOfVouchers"), exception.getMessage());
        }

        void validate_callValidationFunctionForUsingUserInfoType(SMSType type) {
            PublishRequest request = new PublishRequest();

        }
    }

    void validate_missingDuplicateYn_throwCustomCodeException(EnumValidYn valid) {
        CustomCodeException exception = assertThrows(
                CustomCodeException.class,
                () -> PublishServiceImpl.validateNoDuplicateYn(valid)
        );
        assertEquals(MessageUtils.getMessage("evoucher.publish.empty.duplicateKey"), exception.getMessage());
    }

    @Test
    void givenUsingSpyMethod_whenSpyingOnList_thenCorrect() {
        List<String> list = new ArrayList<String>();
        List<String> spyList = spy(list);

        spyList.add("one");
        spyList.add("two");

        verify(spyList).add("one");
        verify(spyList).add("two");

        assertThat(spyList).hasSize(2);
    }

}
