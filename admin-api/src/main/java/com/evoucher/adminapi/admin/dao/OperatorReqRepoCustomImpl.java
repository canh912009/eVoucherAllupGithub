package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.search_response.OperatorSearchRes;
import com.evoucher.adminapi.common.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.evoucher.adminapi.common.utils.DataUtils.*;

@Repository
@RequiredArgsConstructor
@Transactional
public class OperatorReqRepoCustomImpl implements OperatorReqRepoCustom {
    private final EntityManager entityManager;
    @Override
    @SuppressWarnings("unchecked")
    public List<OperatorSearchRes> searchByFilter(FilterSearchAdmin filter, Pageable pageable) throws DatabaseException {
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("select op_req.req_id as requestId,\n");
            queryString.append("       p.publish_id as publishId,\n");
            queryString.append("       p.publish_nm as publishName,\n");
            queryString.append("       c.customer_id as customerId,\n");
            queryString.append("       c.customer_nm as customerName,\n");
            queryString.append("       v.ev as ev,\n");
            queryString.append("       u.user_nm as targetName\n,");
            queryString.append("       u.user_mobile_num as targetNumber,\n");
            queryString.append("       op_req.req_status as requestStatus,\n");
            queryString.append("       op_req.req_dt as requestDate\n");
            queryString.append("    from tb_operator_request op_req\n");
            queryString.append("    inner join tb_voucher v on op_req.ev = v.ev\n");
            queryString.append("    inner join tb_publish p on p.publish_id = v.publish_id\n");
            queryString.append("    inner join tb_customer c on p.customer_id = c.customer_id\n");
            queryString.append("    left join tb_user u on v.user_id = u.id\n");
            queryString.append("where op_req.req_status = 'REQUESTED' \n");

            appendWhereClauseForSearchByFilter(queryString, filter);

            NativeQueryImpl<OperatorSearchRes> query =
                    (NativeQueryImpl<OperatorSearchRes>) entityManager.createNativeQuery(queryString.toString(), "operator_request_search_mapping");

            setQueryValueToParam(query, filter);
            setPageInfoToQuery(query, pageable);

            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private static void appendWhereClauseForSearchByFilter(StringBuilder queryString, @NotNull FilterSearchAdmin filter) {
        if (StringUtils.isNotBlank(filter.getPublishId())) {
            appendLikeQuery(queryString,"p.publish_id", "publishId");
        }

        if (StringUtils.isNotBlank(filter.getPublishName())) {
            appendLikeQuery(queryString, "p.publish_nm", "publishName");
        }

        if (StringUtils.isNotBlank(filter.getCustomerId())) {
            appendLikeQuery(queryString, "c.customer_id", "customerId");
        }

        if (StringUtils.isNotBlank(filter.getCustomerName())) {
            appendLikeQuery(queryString, "c.customer_nm", "customerName");
        }

        if (StringUtils.isNotBlank(filter.getEv())) {
            appendLikeQuery(queryString, "v.ev", "ev");
        }

        if (StringUtils.isNotBlank(filter.getTargetName())) {
            appendLikeQuery(queryString, "u.user_nm", "targetName");
        }

        if (StringUtils.isNotBlank(filter.getTargetNumber())) {
            appendLikeQuery(queryString,  "u.user_mobile_num", "targetNumber");
        }

//        if (StringUtils.isNotBlank(filter.getRequestStatus())) {
//            appendEqualsQuery(queryString, "op_req.req_status", "requestStatus");
//        }
    }

    private void setQueryValueToParam(Query query, @NotNull FilterSearchAdmin filter) {
        if (StringUtils.isNotBlank(filter.getPublishId())) {
            setLikeParam(query, "publishId", filter.getPublishId());
        }

        if (StringUtils.isNotBlank(filter.getPublishName())) {
            setLikeParam(query, "publishName", filter.getPublishName());
        }

        if (StringUtils.isNotBlank(filter.getCustomerId())) {
            setLikeParam(query, "customerId", filter.getCustomerId());
        }

        if (StringUtils.isNotBlank(filter.getCustomerName())) {
            setLikeParam(query, "customerName", filter.getCustomerName());
        }

        if (StringUtils.isNotBlank(filter.getEv())) {
            setLikeParam(query, "ev", filter.getEv());
        }

        if (StringUtils.isNotBlank(filter.getTargetName())) {
            setLikeParam(query, "targetName", filter.getTargetName());
        }

        if (StringUtils.isNotBlank(filter.getTargetNumber())) {
            setLikeParam(query,  "targetNumber", filter.getTargetNumber());
        }

//        if (StringUtils.isNotBlank(filter.getRequestStatus())) {
//            setEqualParam(query, "requestStatus", filter.getRequestStatus());
//        }
    }

    @Override
    public long countAllByFilter(FilterSearchAdmin filter) throws DatabaseException {
        try {
            StringBuilder queryString = getOpReqSearchCountQuery();

            appendWhereClauseForSearchByFilter(queryString, filter);

            Query query = entityManager.createNativeQuery(queryString.toString());
            setQueryValueToParam(query, filter);
            return Optional.ofNullable((BigInteger) query.getSingleResult())
                    .orElse(BigInteger.ZERO).longValue();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private static StringBuilder getOpReqSearchCountQuery() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count(op_req.req_id)\n");
        queryString.append("    from tb_operator_request op_req\n");
        queryString.append("    inner join tb_voucher v on op_req.ev = v.ev\n");
        queryString.append("    inner join tb_publish p on p.publish_id = v.publish_id\n");
        queryString.append("    inner join tb_customer c on p.customer_id = c.customer_id\n");
        queryString.append("where op_req.req_status = 'REQUESTED'\n");
        return queryString;
    }
}
