package asia.castis.evoucher.api.dto.response.vnpt;

import asia.castis.evoucher.api.common.enums.VnptCardAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VnptResponse {
    List<VnptGoodsResponse> vnptProducts;
    VnptCardAction selectedAction;
    TopupResult topupResult;
    CardCodeResult cardCodeResult;
}
