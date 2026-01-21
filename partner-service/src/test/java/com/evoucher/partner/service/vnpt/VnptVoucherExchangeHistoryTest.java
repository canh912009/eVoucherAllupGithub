package com.evoucher.partner.service.vnpt;


import com.evoucher.partner.service.bean.dtos.VnptVoucherExchangeHistoryDto;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import com.evoucher.partner.service.mapper.VnptVoucherExchangeHistoryMapper;
import com.evoucher.partner.service.vnpt.bean.request.VnptQueryBaseRequest;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class VnptVoucherExchangeHistoryTest {
    @Test
    public void toExchangeHistory() throws Exception {
        VnptVoucherExchangeHistoryMapper MAPPER = VnptVoucherExchangeHistoryMapper.INSTANCE;

        VnptQueryBaseRequest request = new VnptQueryBaseRequest();
        request.setRequestId("test");
        request.setProvider("VTT");
        request.setPartnerName("partnerName");

        Card card = new Card();
        card.setPin("1234");
        card.setSerial("1223");
        card.setExpire(new Date());
        card.setAmount(1);
        VnptVoucherExchangeHistoryDto history = MAPPER.toExchangeHistory(
                request,
                VnptExchangeType.TOPUP,
                "ev_test"
        );

        assertThat(history.getExchangeType()).isEqualTo(VnptExchangeType.TOPUP);
        assertThat(history.getEv()).isEqualTo("ev_test");
        assertThat(history.getCardPin()).isNull();
        assertThat(history.getCardSerial()).isNull();
    }
}
