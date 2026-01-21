package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.ExternalPinUploadDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.PinSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class ExternalPinUploadRepositoryImpl implements ExternalPinUploadRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<ExternalPinUploadDTO> searchExternalPinUpload(FilterSearchCms filterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        List<ExternalPinUploadDTO> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT epu.upload_id as id, epu.upload_nm as uploadName, epu.goods_id as goodsId, ");
        strQuery.append("       epu.upload_file_path as uploadFilePath, epu.upload_file_nm as uploadFileName, ");
        strQuery.append("       epu.row_cnt as rowCount, epu.memo as memo, epu.status as status, ");
        strQuery.append("       epu.reg_id regId, epu.reg_dt regDt, epu.updt_dt updtDt, epu.updt_id updtId ");
        strQuery.append(" FROM tb_ext_pin_upload epu ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchCms, strQuery, mapParam);
        strQuery.append(" ORDER BY epu.updt_dt DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("uploadName", StandardBasicTypes.STRING);
        query.addScalar("goodsId", StandardBasicTypes.INTEGER);
        query.addScalar("uploadFilePath", StandardBasicTypes.STRING);
        query.addScalar("uploadFileName", StandardBasicTypes.STRING);
        query.addScalar("rowCount", StandardBasicTypes.INTEGER);
        query.addScalar("memo", StandardBasicTypes.STRING);
        query.addScalar("status", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(ExternalPinUploadDTO.class));

        List<ExternalPinUploadDTO> listQuery = query.getResultList();
        if (!listQuery.isEmpty()) {
            result = listQuery;
        }
        return result;
    }

    @Override
    public Long countExternalPinUpload(FilterSearchCms filterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(epu.upload_id) ");
        strQuery.append(" FROM tb_ext_pin_upload epu ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    @Override
    public List<PinSearchDTO> searchVnptPin(String brandId, String brandTitle, Pageable pageable) {
        return null;
    }

    @Override
    public Long countVnptPin(String brandId, String brandTitle) {
        return null;
    }

    private void setParam(FilterSearchCms filterSearchCms,
                          StringBuilder strQuery,
                          Map<String, Object> mapParam) {
        int goodsId = Integer.MIN_VALUE;
        if (NumberUtils.isCreatable(filterSearchCms.getGoodsId())) {
            goodsId = Integer.parseInt(filterSearchCms.getGoodsId());
        }

        strQuery.append(" AND epu.goods_id = :goodsId");
        mapParam.put("goodsId", goodsId);
    }
}