package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.common.config.PropertyConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class EndUserRepositoryImpl implements EndUserRepositoryCustom {

    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    private final PropertyConverter propertyConverter;

    @Override
    public List<EndUserDTO> searchEndUser(FilterSearchCms FilterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT c.user_mobile_num userMobileNum, c.user_nm userNm, c.gender gender, ");
        strQuery.append(" c.birthday birthday, c.address address, c.email email ");
        strQuery.append(" FROM tb_user c ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(FilterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("userMobileNum", StandardBasicTypes.STRING);
        query.addScalar("userNm", StandardBasicTypes.STRING);
        query.addScalar("gender", StandardBasicTypes.STRING);
        query.addScalar("birthday", StandardBasicTypes.STRING);
        query.addScalar("address", StandardBasicTypes.STRING);
        query.addScalar("email", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(EndUserDTO.class));
        List<EndUserDTO> endUserDTOS = (List<EndUserDTO>) query.getResultList();
        for (EndUserDTO en : endUserDTOS) {
            en.setUserNm(propertyConverter.convertToEntityAttribute(en.getUserNm()));
            en.setUserMobileNum(propertyConverter.convertToEntityAttribute(en.getUserMobileNum()));
        }
        return endUserDTOS;
    }


    @Override
    public Long countUser(FilterSearchCms FilterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(c.user_mobile_num) ");
        strQuery.append(" FROM tb_user c ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(FilterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    private void setParam(FilterSearchCms FilterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(FilterSearchCms)) return;

        if (StringUtils.isNotBlank(FilterSearchCms.getUserMobileNum())) {
            strQuery.append(" AND c.user_mobile_num = :userMobileNum");
            mapParam.put("userMobileNum", FilterSearchCms.getUserMobileNum().trim());
        }
        if (StringUtils.isNotBlank(FilterSearchCms.getUserName())) {
            strQuery.append(" AND c.user_nm LIKE :userNm");
            mapParam.put("userNm", '%' + FilterSearchCms.getUserName().trim() + '%');
        }
    }
}