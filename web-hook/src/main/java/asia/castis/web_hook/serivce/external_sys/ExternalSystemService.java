package asia.castis.web_hook.serivce.external_sys;

import asia.castis.web_hook.bean.dto.ExtVoucherUsingInfo;
import asia.castis.web_hook.bean.entity.Voucher;

import java.util.List;
import java.util.Map;

public interface ExternalSystemService {
    Map<String, ExtVoucherUsingInfo> synchronizeStatus(List<Voucher> voucherList);
}
