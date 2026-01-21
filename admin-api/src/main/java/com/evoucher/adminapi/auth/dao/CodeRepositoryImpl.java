package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Repository
@Transactional
public class CodeRepositoryImpl implements CodeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<CodeDTO> searchCode(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        List<CodeDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT c.cd_id codeId, c.cd_grp_id codeGroupId, c.cd_nm codeName, c.sort_order sortOrder, ");
        sql.append(" c.valid_yn validYn, c.reg_id regId, c.reg_dt regDt, c.updt_id updtId, c.updt_dt updtDt ");
        sql.append(" FROM tb_code c WHERE 1 = 1 AND c.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // Set page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("codeId", StandardBasicTypes.STRING);
        query.addScalar("codeGroupId", StandardBasicTypes.STRING);
        query.addScalar("codeName", StandardBasicTypes.STRING);
        query.addScalar("sortOrder", StandardBasicTypes.INTEGER);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(CodeDTO.class));

        List<CodeDTO> codeDTOS = query.getResultList();
        if (!CollectionUtils.isEmpty(codeDTOS)) {
            result = codeDTOS;
        }

        return result;
    }

    @Override
    public Long countCode(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(c.cd_id) ");
        sql.append(" FROM tb_code c WHERE 1 = 1 AND c.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object menuNumber = query.getSingleResult();

        return Long.parseLong(menuNumber.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (StringUtils.isNotBlank(filterSearchAuth.getCodeId())) {
            sql.append(" AND c.cd_id = :codeId ");
            mapParam.put("codeId", filterSearchAuth.getCodeId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getCodeName())) {
            sql.append(" AND c.cd_nm LIKE :codeName ");
            mapParam.put("codeName", '%' + filterSearchAuth.getCodeName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (c.cd_id LIKE :keyWord OR LOWER(c.cd_nm) LIKE :keyWord) ");
            mapParam.put("keyWord", ('%' + filterSearchAuth.getKeyWord().trim() + '%'));
        }
    }
}
