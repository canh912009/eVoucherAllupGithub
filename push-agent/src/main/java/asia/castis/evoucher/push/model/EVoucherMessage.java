package asia.castis.evoucher.push.model;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serializable;

public class EVoucherMessage implements Serializable {
    private String phoneNumber;
    private String fullTextMessage;
    private String create_dt;

    public EVoucherMessage(String phoneNumber, String fullTextMessage) {
        this.phoneNumber = phoneNumber;
        this.fullTextMessage = fullTextMessage;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullTextMessage() {
        return fullTextMessage;
    }

    public void setFullTextMessage(String fullTextMessage) {
        this.fullTextMessage = fullTextMessage;
    }

    public String getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(String create_dt) {
        this.create_dt = create_dt;
    }
}
