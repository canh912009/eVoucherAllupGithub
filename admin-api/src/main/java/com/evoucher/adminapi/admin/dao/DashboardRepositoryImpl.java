package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.common.enums.SendMessageStatus;
import com.evoucher.adminapi.common.enums.VoucherStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static com.evoucher.adminapi.common.utils.DataUtils.setPageInfoToQuery;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DashboardRepositoryImpl implements DashboardRepository{
    private final EntityManager entityManager;

    @Override
    public List<Map<String, Object>> getSupplierChartData(Date startDate, Date endDate) {
        String queryString = "select s.supplier_id as supplierId, s.supplier_nm as supplierName, count(good.goods_id) as itemCount from tb_supplier s\n" +
                "    inner join tb_supplier_contract contract on s.supplier_id = contract.supplier_id\n" +
                "    inner join tb_goods good on contract.supplier_contract_id = good.supplier_contract_id " +
                "where good.strt_dt >= :startDate and good.end_dt <= :endDate " +
                "group by s.supplier_id " +
                "order by s.supplier_id ";
        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getCustomerChartData(Date startDate, Date endDate) {
        String queryString = "select cus.customer_id as customerId, cus.customer_nm as customerName, count(camp.campaign_id) as campaignCount, count(vou.ev) as voucherAmount from tb_customer cus\n" +
                "    inner join tb_campaign camp on camp.customer_id = cus.customer_id\n" +
                "    inner join tb_voucher vou on vou.campaign_id = camp.campaign_id\n" +
                "    where camp.st_dt >= :startDate and camp.ed_dt <= :endDate \n" +
                "    group by cus.customer_id" +
                "    order by cus.customer_id";
        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getCustomerTableData(Pageable pageable) {
        String queryString = "select cus.customer_id as customerId, cus.customer_nm as customerName, count(tc.campaign_id) as campaignCount, count(tv.ev) as voucherCount, sum(COALESCE(tv.voucher_price, 0)) as salesAmount from tb_customer cus\n" +
                "    left join tb_campaign tc on cus.customer_id = tc.customer_id\n" +
                "    left join tb_voucher tv on tc.campaign_id = tv.campaign_id\n" +
                "    group by cus.customer_id\n" +
                "    order by cus.customer_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        setPageInfoToQuery(query, pageable);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public long countCustomerTableData() {
        String queryString = "select count(distinct(cus.customer_id)) from tb_customer cus";
        Query query = entityManager.createNativeQuery(queryString);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public List<Map<String, Object>> getCampaignChartData(String customerId) {
        String queryString = "select tc.campaign_id as campaignId, tc.campaign_nm as campaignName,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount,\n" +
                "       sum(if(tv.voucher_status_cd = :unusedStatus , 1, 0)) AS unusedCount,\n" +
                "       sum(if(tv.voucher_status_cd <> :usedStatus and tv.voucher_status_cd <> :unusedStatus , 1, 0)) AS othersCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , coalesce(tv.voucher_price, 0), 0)) as usedAmount,\n" +
                "       sum(if(tv.voucher_status_cd = :unusedStatus , coalesce(tv.voucher_price, 0), 0)) as unusedAmount,\n" +
                "       sum(if(tv.voucher_status_cd <> :usedStatus and tv.voucher_status_cd <> :unusedStatus , coalesce(tv.voucher_price, 0), 0)) as othersAmount\n" +
                "from tb_campaign tc" +
                "    left join tb_voucher tv on tc.campaign_id = tv.campaign_id\n" +
                "    WHERE tc.customer_id = :customerId \n" +
                "\n" +
                "group by tc.campaign_id\n" +
                "order by tc.campaign_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("unusedStatus", VoucherStatus.NORMAL);
        query.setParameter("customerId", customerId);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getItemTableData(String supplierId, Pageable pageable) {
        String queryString = "select tg.goods_id as itemCode, tg.goods_nm as itemName,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , coalesce(tv.voucher_price, 0), 0)) as usedAmount\n" +
                "    from tb_goods tg\n" +
                "    left join tb_voucher tv on tv.goods_id = tg.goods_id\n" +
                "    left join tb_brand tb on tg.brand_id = tb.brand_id\n" +
                "    left join tb_publish tp on tv.publish_id = tp.publish_id\n" +
                "    where tb.supplier_id = :supplierId \n" +
                "group by tg.goods_id\n" +
                "order by tg.goods_id";


        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);
        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("supplierId", supplierId);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        setPageInfoToQuery(query, pageable);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getBrandItemTableData(String brandId, Pageable pageable) {
        String queryString = "select tg.goods_id as itemCode, tg.goods_nm as goodName,\n" +
                "    count(distinct (tp.publish_id)) as publishCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , coalesce(tv.voucher_price, 0), 0)) as usedAmount\n" +
                "    from tb_goods tg\n" +
                "    left join tb_voucher tv on tv.goods_id = tg.goods_id\n" +
                "    left join tb_brand tb on tg.brand_id = tb.brand_id\n" +
                "    left join tb_publish tp on tv.publish_id = tp.publish_id\n" +
                "    where tb.brand_id = :brandId\n" +
                "group by tg.goods_id\n" +
                "order by tg.goods_id";


        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);
        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("brandId", brandId);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        setPageInfoToQuery(query, pageable);

        return query.getResultList();
    }

    @Override
    public long countItemTableData(String supplierId) {
        String queryString = "select count(tg.goods_id) from tb_goods tg where tg.supplier_id = :supplierId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("supplierId", supplierId);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public long countBrandItemTableData(String brandId) {
        String queryString = "select count(tg.goods_id) from tb_goods tg where tg.brand_id = :brandId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("brandId", brandId);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public List<Map<String, Object>> getBrandTableData(String supplierId, Pageable pageable) {
        String queryString = "select tb.brand_id as brandId, tb.brand_nm as brandName,\n" +
                "    count(distinct (tp.publish_id)) as publishCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , coalesce(tv.voucher_price, 0), 0)) as usedAmount\n" +
                "    from tb_brand tb\n" +
                "    inner join tb_supplier ts on tb.supplier_id = ts.supplier_id\n" +
                "    left join tb_goods tg on tb.brand_id = tg.brand_id\n" +
                "    left join tb_voucher tv on tg.goods_id = tv.goods_id\n" +
                "    left join tb_publish tp on tp.publish_id = tv.publish_id\n" +
                "where tb.supplier_id = :supplierId\n" +
                "group by tb.brand_id\n" +
                "order by tb.brand_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("supplierId", supplierId);

        setPageInfoToQuery(query, pageable);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();
    }

    @Override
    public long countBrandTableData(String supplierId) {
        String queryString = "select count(tb.brand_id) from tb_brand tb where tb.supplier_id = :supplierId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("supplierId", supplierId);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public List<Map<String, Object>> getStoreTableData(String brandId, Pageable pageable) {
        String queryString = "select ts.store_id as storeId, ts.store_nm as storeName,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , coalesce(tv.voucher_price, 0), 0)) as usedAmount\n" +
                "    from tb_store ts\n" +
                "    left join tb_exchange_history teh on ts.store_id = teh.store_id\n" +
                "    left join tb_voucher tv on teh.ev = tv.ev\n" +
                "    where ts.brand_id = :brandId\n" +
                "    group by ts.store_id\n" +
                "    order by ts.store_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("brandId", brandId);

        setPageInfoToQuery(query, pageable);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.getResultList();

    }

    @Override
    public long countStoreTableData(String brandId) {
        String queryString = "select count(ts.store_id) from tb_store ts where ts.brand_id = :brandId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("brandId", brandId);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public List<Map<String, Object>> getStoreGoodsChartData(String storeId, Date startDate, Date endDate) {

        String queryString = "select tg.goods_id as goodCode, tg.goods_nm as goodName,\n" +
                "       DATE_FORMAT(teh.transaction_dt, '%Y-%m-%d') as exchangeDate,\n" +
                "       sum(if(tv.voucher_status_cd = :usedStatus , 1, 0)) AS usedCount\n" +
                "    from tb_goods tg\n" +
                "    left join tb_exchange_history teh on tg.goods_id = teh.goods_id\n" +
                "    left join tb_voucher tv on tg.goods_id = tv.goods_id\n" +
                "    left join tb_store ts on teh.store_id = ts.store_id\n" +
                "    where ts.store_id = :storeId and teh.transaction_dt >= :startDate and teh.transaction_dt <= :endDate\n" +
                "    group by exchangeDate, tg.goods_id\n" +
                "    order by tg.goods_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("usedStatus", VoucherStatus.USED);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setParameter("storeId", storeId);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getCampaignTableData(String customerId, Date startDate, Date endDate, Pageable pageable) {

        String queryString = "select tc.campaign_id as campaignId, tc.campaign_nm as campaignName,\n" +
                "       count(distinct (tp.publish_id)) as deliveryCount,\n" +
                "        count(distinct (tpd.publish_dtl_id)) as totalVoucherCount,\n" +
                "        sum(if(tpd.publish_dtl_status_cd = :sendMessageSuccessStatus, 1, 0)) as totalSendSuccessCount,\n" +
                "        sum(if(tpd.publish_dtl_status_cd = :sendMessageFailStatus, 1, 0)) as totalSendFailCount\n" +
                "    from tb_campaign tc\n" +
                "        left join tb_publish tp on tc.campaign_id = tp.campaign_id\n" +
                "        left join tb_publish_detail tpd on tp.publish_id = tpd.publish_id\n" +
                "    where tc.st_dt >= :startDate and tc.ed_dt <= :endDate and tc.customer_id = :customerId\n" +
                "    group by tc.campaign_id\n" +
                "    order by tc.campaign_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("sendMessageSuccessStatus", SendMessageStatus.RESULT_SUCCESS);
        query.setParameter("sendMessageFailStatus", SendMessageStatus.RESULT_FAIL);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setParameter("customerId", customerId);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        setPageInfoToQuery(query, pageable);

        return query.getResultList();
    }

    @Override
    public long countCampaignTableDataCount(String customerId, Date startDate, Date endDate) {
        String queryString = "select count(tc.campaign_id) from tb_campaign tc where tc.st_dt >= :startDate and tc.ed_dt <= :endDate and tc.customer_id = :customerId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setParameter("customerId", customerId);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public List<Map<String, Object>> getDeliveryStatusTableData(String customerId, Date startDate, Date endDate, Pageable pageable) {
        String queryString = "select tp.publish_id as deliveryId, tp.publish_nm as deliveryName,\n" +
                "    count(distinct (tpd.publish_dtl_status_cd)) as voucherCount,\n" +
                "    sum(if(tpd.publish_dtl_status_cd = :sendMessageSuccessStatus, 1, 0)) as totalSendSuccessCount,\n" +
                "        sum(if(tpd.publish_dtl_status_cd = :sendMessageFailStatus, 1, 0)) as totalSendFailCount\n" +
                "    from tb_publish tp\n" +
                "    left join tb_publish_detail tpd on tp.publish_id = tpd.publish_id\n" +
                "    where tp.publish_dt >= :startDate and tp.publish_dt <= :endDate and tp.customer_id = :customerId\n" +
                "    group by tp.publish_id\n" +
                "    order by tp.publish_id";

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString);

        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setParameter("sendMessageSuccessStatus", SendMessageStatus.RESULT_SUCCESS);
        query.setParameter("sendMessageFailStatus", SendMessageStatus.RESULT_FAIL);
        query.setParameter("customerId", customerId);

        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        setPageInfoToQuery(query, pageable);
        return query.getResultList();
    }

    @Override
    public long countDeliveryStatusTableData(String customerId, Date startDate, Date endDate) {
        String queryString = "select count(tp.publish_id) from tb_publish tp where tp.publish_dt >= :startDate and tp.publish_dt <= :endDate and tp.customer_id = :customerId";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        query.setParameter("customerId", customerId);
        return Long.parseLong(query.getSingleResult().toString());
    }


}
