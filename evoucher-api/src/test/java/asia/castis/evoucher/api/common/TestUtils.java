package asia.castis.evoucher.api.common;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.dto.request.choose.ChosenItem;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.entity.*;
import org.assertj.core.util.Strings;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.instancio.Select.field;

public class TestUtils {
    public static final String EV = "111-123-112-113";
    public static final Integer PUBLISH_ID = 333;
    public static final String SUPPLIER_ID = "S001";
    public static final String BRAND_ID = Strings.append("_001").to(SUPPLIER_ID);
    public static final int GOODS_ID = 1;
    public static final Double SELL_PRICE = 10.0;

    public static EVoucher getDbVoucher() {
        return Instancio.of(EVoucher.class)
                .set(field(EVoucher::getEV), EV)
                .set(field(EVoucher::getPublishId), PUBLISH_ID)
                .set(field(EVoucher::getParentVoucherEv), null)
                .set(field(EVoucher::getOriginalEv), null)
                .set(field(EVoucher::getVoucherStatusCode), VoucherStatusCode.NORMAL)
                .create();
    }

    public static Publish getDbPublish() {
        return Instancio.of(Publish.class)
                .set(field(Publish::getId), PUBLISH_ID)
                .create();
    }

    public static ChosenRequestV1 getChosenRequest() {
        return Instancio.of(ChosenRequestV1.class)
                .set(field(ChosenRequestV1::getParentVoucherId), EV)
                .set(field(ChosenRequestV1::getType), SystemType.CHOICE.name())
                .set(field(ChosenRequestV1::getProducts), getChoices())
                .create();
    }

    private static List<ChosenItem> getChoices() {
        Model<ChosenItem> chosenItemModel = Instancio.of(ChosenItem.class)
                .set(field(ChosenItem::getGoodsId), GOODS_ID)
                .set(field(ChosenItem::getQuantity), 2)
                .toModel();
        return Instancio.ofList(chosenItemModel).size(2).create();
    }

    public static Goods getDbProduct() {
        return Instancio.of(Goods.class)
                .set(field(Goods::getId), GOODS_ID)
                .set(field(Goods::getSellPrice), SELL_PRICE)
                .create();
    }

    public static BulkBrand getDbBulkBrand() {
        return Instancio.of(BulkBrand.class)
                .set(field(BulkBrand::getBrand), getDbBrand())
                .create();
    }

    private static Set<BulkGoods> getDbBulkGoods(int numberOfGoods) {
        return IntStream.rangeClosed(0, numberOfGoods)
                .mapToObj(i -> Instancio.of(BulkGoods.class)
                        .set(field(BulkGoods::getDisplayIdx), numberOfGoods - i)
                        .create())
                .collect(Collectors.toSet());
    }

    public static Supplier getDbSupplier() {
        return Instancio.of(Supplier.class)
                .set(field(Supplier::getId), SUPPLIER_ID)
                .create();
    }

    public static Brand getDbBrand() {
        return Instancio.of(Brand.class)
                .set(field(Brand::getId), BRAND_ID)
                .set(field(Brand::getSupplierId), SUPPLIER_ID)
                .create();
    }

    public static ChosenRequestV1 generateBulkChosenRequest() {
        ChosenRequestV1 request = getChosenRequest();
        request.setType(SystemType.BULK.name());
        return request;
    }

    public static ResponseData<List<String>> getResponseData() {
        return ResponseData.ok(Instancio.ofList(String.class).create());
    }
}
