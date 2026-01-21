package asia.castis.evoucher.push;

import asia.castis.evoucher.push.utils.Utils;
import org.apache.commons.net.util.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;
@RunWith(SpringRunner.class)
public class Base64EncodeUTF8Test {

    @Test
    public void base64EncodeUTF8() {
        String utf8Message = "Nội dung cần encode";
        String resultMessage = Utils.endCodeUnicodeMessage(utf8Message);
        System.out.println("Result message: " + resultMessage);
        Assert.assertEquals("TuG7mWkgZHVuZyBj4bqnbiBlbmNvZGU=", resultMessage);
    }
}
