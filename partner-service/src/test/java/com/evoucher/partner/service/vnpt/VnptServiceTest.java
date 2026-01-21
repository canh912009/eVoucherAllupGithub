package com.evoucher.partner.service.vnpt;

import com.evoucher.partner.service.bean.dtos.ThirdPartyHistoryDto;
import com.evoucher.partner.service.exception.define_exception.VnptException;
import com.evoucher.partner.service.vnpt.bean.request.DownloadSoftPinRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.PaymentCdvRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.TopUpRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.VnptQueryBaseRequest;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import com.evoucher.partner.service.vnpt.service.VnptQueryService;
import com.evoucher.partner.service.vnpt.service.VnptService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {VnptQueryService.class, VnptConfiguration.class, })
@AutoConfigureJson
@Slf4j
public class VnptServiceTest {
    @Autowired
    private VnptQueryService vnptQueryService;
    private static List<String> purchase_providers = List.of("APPOTA","BEE","FPT","FUNTAP","GARENA","GOSU","SCOIN","SOHA","VGP","VMS","VMSDATA2","VNM","VNP","VNPDATA","VTC","VTT","VTTDATA3","WINTEL","ZING");

    @Test
    public void queryBalance_returnCurrentBalance() {
        Long balance = vnptQueryService.queryBalance();
        assertThat(balance).isPositive();
    }

    @Test
    public void paymentCdv_returnSuccessAmount() {
        PaymentCdvRequestQuery request = new PaymentCdvRequestQuery();

        setVnptRequestBaseData(request);
        request.setProvider("VTT");
        request.setAccount("0372594810");
        request.setAmount(100000);

        vnptQueryService.prepareData(request);
        vnptQueryService.prepareDetail(request);
        long result = vnptQueryService.paymentCDV(request, new ThirdPartyHistoryDto());

        assertThat(result).isPositive();
    }

    @Test
    public void buyCard_returnCardList() {
        DownloadSoftPinRequestQuery request = new DownloadSoftPinRequestQuery();
        setVnptRequestBaseData(request);
        request.setProvider("VMS");
        request.setQuantity(1);
        request.setAmount(30000);

        vnptQueryService.prepareData(request);
        vnptQueryService.prepareDetail(request);

        List<Card> cardList = vnptQueryService.downloadSoftPin(request, new ThirdPartyHistoryDto());

        assertThat(cardList).isNotNull().isNotEmpty().hasSize(request.getQuantity());

    }

//    @Test
    public void checkUnsupportedProvider() {
        List<String> notSupported = new ArrayList<>();
        for (String providerCode : purchase_providers) {
            try {
                DownloadSoftPinRequestQuery request = new DownloadSoftPinRequestQuery();
                setVnptRequestBaseData(request);
                request.setProvider(providerCode);
                request.setQuantity(1);
                request.setAmount(50000);

                vnptQueryService.prepareData(request);
                vnptQueryService.prepareDetail(request);

                List<Card> cardList = vnptQueryService.downloadSoftPin(request, new ThirdPartyHistoryDto());
//                assertThat(cardList).isNotNull().isNotEmpty().hasSize(request.getQuantity());

            } catch (Exception e) {
                log.warn("{} not supported", providerCode);
                notSupported.add(providerCode);
            }
        }
        log.warn("not supported list: {}", notSupported);
    }


    @Test
    public void topup_returnSuccess() throws VnptException {
        TopUpRequestQuery request = new TopUpRequestQuery();
        request.setRequestId(UUID.randomUUID().toString());
        request.setTarget("0372594810");
        request.setAmount(30000);
        request.setProvider("VTT");

        vnptQueryService.prepareData(request);
        vnptQueryService.prepareDetail(request);
        vnptQueryService.topUp(request, new ThirdPartyHistoryDto());
    }


    @Test
    public void topup_returnFail() throws VnptException {
        TopUpRequestQuery request = new TopUpRequestQuery();
        request.setRequestId(UUID.randomUUID().toString());
        request.setTarget("037259481044");
        request.setAmount(30000);
        request.setProvider("VTT4");

        vnptQueryService.prepareData(request);
        vnptQueryService.prepareDetail(request);
        vnptQueryService.topUp(request, new ThirdPartyHistoryDto());
    }

    @Test
    public void processNumberWithRegionCodeZero() throws Exception {
        String input = "03040084013";
        String expect = "03040084013";
        TopUpRequestQuery topup = new TopUpRequestQuery();
//        topup.setTarget("+84934562889");
//        topup.setTarget("0934562889");
        topup.setTarget(input);

        VnptService.validateMobileNumberPrefix(topup.getTarget());
        VnptService.replaceRegionCodeWithZero(topup);
        log.info("after remove region code: {}", topup.getTarget());
        assertThat(topup.getTarget()).startsWith("0");
        Assertions.assertEquals(expect, topup.getTarget());
    }

    @Test
    public void processNumberWithRegionCodePlus() throws Exception {
        String input = "+843040084013";
        String expect = "03040084013";
        TopUpRequestQuery topup = new TopUpRequestQuery();
//        topup.setTarget("+84934562889");
//        topup.setTarget("0934562889");
        topup.setTarget(input);

        VnptService.validateMobileNumberPrefix(topup.getTarget());
        VnptService.replaceRegionCodeWithZero(topup);
        log.info("after remove region code: {}", topup.getTarget());
        assertThat(topup.getTarget()).startsWith("0");
        Assertions.assertEquals(expect, topup.getTarget());
    }

    @Test
    public void processNumberWithRegionCodeDoubleZero() throws Exception {
        String input = "00843040084013";
        String expect = "03040084013";
        TopUpRequestQuery topup = new TopUpRequestQuery();
//        topup.setTarget("+84934562889");
//        topup.setTarget("0934562889");
        topup.setTarget(input);

        VnptService.validateMobileNumberPrefix(topup.getTarget());
        VnptService.replaceRegionCodeWithZero(topup);
        log.info("after remove region code: {}", topup.getTarget());
        assertThat(topup.getTarget()).startsWith("0");
        Assertions.assertEquals(expect, topup.getTarget());
    }

    private void setVnptRequestBaseData(VnptQueryBaseRequest request) {
        request.setRequestId(UUID.randomUUID().toString());
    }

//    @Test
//    public void parseVnptCardString() {
//        parseVnptCard("/k1StKLX02FcysS1oLItl4jCsqaiHdH+yPNT9DsYJ1O3FmUOpmomf/uuYclEnR34twA34JpVOVdz\\npS8jDD9JT6CN4H56iwh6");
//    }

    private void parseVnptCard(String listCardString) {
        List<Card> cards = vnptQueryService.parseVnptCard(listCardString);

        log.info("{}", cards);
        assertThat(cards).isNotNull().hasSize(1);
    }
    @Test
    public void sign_returnEncryptedSign() {
        log.info(vnptQueryService.sign("partnerTest"));
    }
}
