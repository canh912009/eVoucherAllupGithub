package asia.castis.evoucherservicefe.voucherhandler.service;

import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.enums.EnumMessageType;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.CampaignModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.service.EsPublishService;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.common.utils.CryptoUtils;
import asia.castis.evoucherservicefe.common.utils.ErrorCode;
import asia.castis.evoucherservicefe.exceptions.ClientRequestException;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
import asia.castis.evoucherservicefe.voucherhandler.sender.VoucherHandlerSender;
import asia.castis.evoucherservicefe.voucherhandler.service.impl.VoucherServiceImpl;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {
    @InjectMocks
    VoucherServiceImpl voucherService;

    @Mock
    EsVoucherService esVoucherService;

    @Mock
    CryptoUtils cryptoUtils;
    @Mock
    VoucherHandlerSender voucherHandlerSender;
    @Mock
    PublishRequestSender publishRequestSender;
    @Mock
    EsPublishService esPublishService;

    @Test
    void testActivateVoucher_CanNotFindBySerialNo() throws Exception {
        ActivateRequest activateReq = makeActivateRequest();

        when(esVoucherService.findBySerialNo(activateReq.getSerialNumber())).thenReturn(null);
        assertThrows(ClientRequestException.class, () -> voucherService.activateVoucher(activateReq));
    }

    // test activate voucher voucher is activated
    @Test
    void testActivateVoucher_VoucherIsActivated() throws Exception {
        ActivateRequest activateReq = makeActivateRequest();

        VoucherModel activatedVoucher = makeActivatedVoucher();

        when(esVoucherService.findBySerialNo(activateReq.getSerialNumber())).thenReturn(activatedVoucher);
        ClientRequestException exception  = assertThrows(ClientRequestException.class, () -> voucherService.activateVoucher(activateReq));
        assertEquals(ErrorCode.VOUCHER_ALREADY_ACTIVATED, exception.getCode());
    }

    @Test
    void testActivateVoucher_ExceptionWhileEncrypt() throws Exception {
        ActivateRequest activateRequest = makeActivateRequest();
        when(esVoucherService.findBySerialNo(any(String.class))).thenReturn(new VoucherModel());
        assertThrows(Exception.class, () -> voucherService.activateVoucher(activateRequest));
    }

    @Test
    void testActivateVoucher_NoOutgoingMsg() throws Exception {
        ActivateRequest activateRequest = makeActivateRequest();
        VoucherModel voucher = makeInactiveVoucher();
        voucher.setOutgoingRequest(null);

        when(esVoucherService.findBySerialNo(activateRequest.getSerialNumber())).thenReturn(voucher);
        assertThrows(ClientRequestException.class, () -> voucherService.activateVoucher(activateRequest));
    }

    @NotNull
    private ActivateRequest makeActivateRequest() {
        ActivateRequest activateReq = new ActivateRequest();
        activateReq.setSerialNumber("123456");
        activateReq.setUserName("John Doe");
        activateReq.setPhoneNumber("1234567890");
        return activateReq;
    }

    /***
     * Activated Voucher is voucher that has either userName or userMobileNumber
     * @return
     */
    private VoucherModel makeActivatedVoucher() throws Exception {
        VoucherModel voucherModel = makeVoucher();
        voucherModel.setUserName("John Doe");
        voucherModel.setUserMobileNumber("1234567890");
        return voucherModel;
    }
    private VoucherModel makeInactiveVoucher() throws Exception {
        VoucherModel voucherModel = makeVoucher();
        voucherModel.setUserName(null);
        voucherModel.setUserMobileNumber(null);
        return voucherModel;
    }
    /***
     * Activated Voucher is voucher that has either userName or userMobileNumber
     * @return
     */
    private VoucherModel makeVoucher() throws Exception {
        VoucherModel voucherModel = new VoucherModel();
        voucherModel.setSerialNo("123456");
        voucherModel.setId("abc123-def341");
        voucherModel.setPublishDetailId(1L);
        voucherModel.setSmsType(EnumMessageType.DOWNLOAD);

        PublishMessage publishMessage = new PublishMessage();
        publishMessage.setMessage("Message");

        voucherModel.setOutgoingRequest((new JsonMapper()).writeValueAsString(publishMessage));
        return voucherModel;
    }

    public PublishModel makePublish() {
        CampaignModel campaignModel = new CampaignModel();
        campaignModel.setId(2L);
        PublishModel publishModel = new PublishModel();
        publishModel.setId(1L);
        publishModel.setCampaign(campaignModel);
        return publishModel;
    }
}
