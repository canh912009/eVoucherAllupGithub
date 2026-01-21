package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
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
public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<MenuGroupDTO> searchMenuGroup(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        List<MenuGroupDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT mg.menu_grp_id id, mg.menu_grp_nm menuGroupName, mg.sort_order sortOrder, ");
        sql.append(" mg.valid_yn validYn, mg.reg_id regId, mg.reg_dt regDt, mg.updt_id updtId, mg.updt_dt updtDt ");
        sql.append(" FROM tb_menu_group mg WHERE 1 = 1 AND mg.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // Set page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("menuGroupName", StandardBasicTypes.STRING);
        query.addScalar("sortOrder", StandardBasicTypes.INTEGER);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(MenuGroupDTO.class));

        List<MenuGroupDTO> menuGroupDTOS = query.getResultList();
        if (!CollectionUtils.isEmpty(menuGroupDTOS)) {
            result = menuGroupDTOS;
        }

        return result;
    }

    @Override
    public Long countMenuGroup(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(mg.menu_grp_id) ");
        sql.append(" FROM tb_menu_group mg WHERE 1 = 1 AND mg.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object menuNumber = query.getSingleResult();

        return Long.parseLong(menuNumber.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (Objects.nonNull(filterSearchAuth.getMenuGroupId())) {
            sql.append(" AND mg.menu_grp_id = :menuGroupId ");
            mapParam.put("menuGroupId", filterSearchAuth.getMenuGroupId());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getMenuGroupName())) {
            sql.append(" AND mg.menu_grp_nm LIKE :menuGroupName ");
            mapParam.put("menuGroupName", '%' + filterSearchAuth.getMenuGroupName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (mg.menu_grp_id LIKE :keyWord OR LOWER(mg.menu_grp_nm) LIKE :keyWord) ");
            mapParam.put("keyWord", ('%' + filterSearchAuth.getKeyWord().trim() + '%'));
        }
    }
}
