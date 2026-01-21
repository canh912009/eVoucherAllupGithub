package asia.castis.evoucher.push;

import asia.castis.evoucher.push.model.PAMessageCallBackSms;
import asia.castis.evoucher.push.service.PAMessageCallBackSmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MocMvcSmsTests {
    @MockBean
    private PAMessageCallBackSmsService paMessageCallBackSmsService;
    @Test
    public void paMessageCallBackSmsServiceSaveTest() {
        given(this.paMessageCallBackSmsService.save(new PAMessageCallBackSms()))
                .willReturn(new PAMessageCallBackSms());
    }
    @Test
    public void paMessageCallBackSmsServiceFindByIdTest() {
        given(this.paMessageCallBackSmsService.findById("12345678"))
                .willReturn(new PAMessageCallBackSms());
    }
    @Test
    public void paMessageCallBackSmsServiceFindByMessageIdTest() {
        given(this.paMessageCallBackSmsService.findByMessageId("12345678"))
                .willReturn(new PAMessageCallBackSms());
    }
}
