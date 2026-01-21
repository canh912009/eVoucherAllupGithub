package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
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
public class CodeGroupRepositoryImpl implements CodeGroupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<CodeGroupDTO> searchCodeGroup(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        List<CodeGroupDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT cg.cd_grp_id codeGroupId, cg.cd_grp_nm codeGroupName, ");
        sql.append(" cg.valid_yn validYn, cg.reg_id regId, cg.reg_dt regDt, cg.updt_id updtId, cg.updt_dt updtDt ");
        sql.append(" FROM tb_code_group cg WHERE 1 = 1 AND cg.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // Set page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("codeGroupId", StandardBasicTypes.STRING);
        query.addScalar("codeGroupName", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(CodeGroupDTO.class));

        List<CodeGroupDTO> menuGroupDTOS = query.getResultList();
        if (!CollectionUtils.isEmpty(menuGroupDTOS)) {
            result = menuGroupDTOS;
        }

        return result;
    }

    @Override
    public Long countCodeGroup(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(cg.cd_grp_id) ");
        sql.append(" FROM tb_code_group cg WHERE 1 = 1 AND cg.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object menuNumber = query.getSingleResult();

        return Long.parseLong(menuNumber.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (StringUtils.isNotBlank(filterSearchAuth.getCodeGroupId())) {
            sql.append(" AND cg.cd_grp_id = :codeGroupId ");
            mapParam.put("codeGroupId", filterSearchAuth.getCodeGroupId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getCodeGroupName())) {
            sql.append(" AND cg.cd_grp_nm LIKE :codeGroupName ");
            mapParam.put("codeGroupName", '%' + filterSearchAuth.getCodeGroupName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (cg.cd_grp_id LIKE :keyWord OR LOWER(cg.cd_grp_nm) LIKE :keyWord) ");
            mapParam.put("keyWord", ('%' + filterSearchAuth.getKeyWord().trim() + '%'));
        }
    }
}
