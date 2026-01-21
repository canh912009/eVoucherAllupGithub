package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class TemplateDataOld {
    private String receiver;
    private String subject;
    private String useInfo;
    private String content;
    private String shortLink;
    private String goodsName;
    private String expirationDate;

    public String[] toArray() {
        return new String[]{receiver, content, shortLink, goodsName, expirationDate};
    }
}
