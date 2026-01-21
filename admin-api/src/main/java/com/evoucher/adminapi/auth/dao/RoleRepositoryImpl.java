package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.RoleDTO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Transactional
public class RoleRepositoryImpl implements RoleRepositoryCustom {
    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RoleDTO> searchRole(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        List<RoleDTO> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT r.role_code roleCode, r.role_nm roleName, r.sort_order sortOrder, ");
        sql.append(" r.valid_yn validYn, r.reg_id regId, r.reg_dt regDt, r.updt_id updtId, r.updt_dt updtDt ");
        sql.append("FROM tb_role r WHERE 1 = 1 AND valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("roleCode", StandardBasicTypes.STRING);
        query.addScalar("roleName", StandardBasicTypes.STRING);
        query.addScalar("sortOrder", StandardBasicTypes.INTEGER);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(RoleDTO.class));

        List<RoleDTO> list = query.getResultList();

        if (!CollectionUtils.isEmpty(list)) {
            result = list;
        }
        return result;
    }

    @Override
    public Long countRole(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(r.role_code) ");
        sql.append("FROM tb_role r WHERE 1 = 1 AND valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object count = query.getSingleResult();

        return Long.parseLong(count.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (StringUtils.isNotBlank(filterSearchAuth.getRoleCode())) {
            sql.append(" AND r.role_code = :id ");
            mapParam.put("id", filterSearchAuth.getRoleCode().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getRoleName())) {
            sql.append(" AND r.role_nm LIKE :roleName ");
            mapParam.put("roleName", '%' + filterSearchAuth.getRoleName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (r.role_code LIKE :keyWord OR r.role_nm LIKE :keyWord) ");
            mapParam.put(KEYWORD, '%' + filterSearchAuth.getKeyWord().trim() + '%');
        }
    }
}
