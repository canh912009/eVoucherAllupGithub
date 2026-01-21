package asia.castis.evoucher.push;

import asia.castis.evoucher.push.model.PAMessageCallBackSms;
import asia.castis.evoucher.push.model.PublishMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@RunWith(SpringRunner.class)
@JsonTest
public class SmsJsonTests {
    @Autowired
   private JacksonTester<PublishMessage> publishMessageJson;
    /*
   @Test
   public void publicMessageSerializeTest() throws Exception {
       PublishMessage publishMessage = new PublishMessage();
       publishMessage.setMessage("Message content for testing purpose");
       publishMessage.setBrandName("FTI");
       publishMessage.setMobileNumber("0966312666");
       assertThat(this.publishMessageJson.write(publishMessage))
               .isEqualToJson("public_message.json");

   }

     */
   @Test
    public void publicMessageDeserializeTest() throws Exception {
       String content = "{\"mobileNumber\":\"0966312666\", \"message\":\"Message content for testing purpose\",\"brandName\":\"FTI\",\"publishDetailId\":0,\"imageUrl\":null,\"templateId\":0,\"templateData\":null,\"smsMsg\":null,\"failover\":0,\"callbackUrl\":null}";
       PublishMessage publishMessage = new PublishMessage("0966312666","Message content for testing purpose","FTI");
       PublishMessage result = this.publishMessageJson.parse(content).getObject();

       assertThat(result.getMessage()).isEqualTo("Message content for testing purpose");
       assertThat(result.getMobileNumber()).isEqualTo("0966312666");
       assertThat(result.getBrandName()).isEqualTo("FTI");
   }
}
