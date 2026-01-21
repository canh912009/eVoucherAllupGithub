package asia.castis.web_hook.bean.dto.response.ur_box;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UrBoxVoucherList {
    private String id;
    private String linkCart;
    private String linkCombo;
    private String transaction_id;
    private String created;
    private long created_timestamp;
    private String pay_time;
    private String pay_status;
    private int pay_status_code;
    private List<Detail> detail;
    private int item_quantity;

    @Data
    @ToString
    public static class Detail {
        private String id;
        private String link;
        private String type;
        private String usage_status;
        private Integer usage_status_code;
        private String using_time;
        private String gift_id;
        private String gift_detail_id;
        private String delivery;
        private Integer deliveryCode;
        private String code_image;
        private String delivery_required;
        private List<String> topup;
        private String gift_title;
        private String expired;
        private String code;
        private String code_display;
        private int code_display_type;
        private String gift_detail_title;
        private String price;
        private String image;
        private String images_rectangle;
        private String brandTitle;
        private String brandImage;
    }
}
