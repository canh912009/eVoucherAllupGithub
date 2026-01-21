package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
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
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<MenuDTO> searchMenu(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        List<MenuDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT m.menu_id id, m.menu_grp_id menuGroupId, m.menu_nm menuName, m.sort_order sortOrder, ");
        sql.append(" m.menu_url menuUrl, m.valid_yn validYn, m.reg_id regId, m.reg_dt regDt, m.updt_id updtId, m.updt_dt updtDt ");
        sql.append(" FROM tb_menu m WHERE 1 = 1 AND m.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // Set page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("menuGroupId", StandardBasicTypes.INTEGER);
        query.addScalar("menuName", StandardBasicTypes.STRING);
        query.addScalar("sortOrder", StandardBasicTypes.INTEGER);
        query.addScalar("menuUrl", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(MenuDTO.class));

        List<MenuDTO> menuDTOS = query.getResultList();
        if (!CollectionUtils.isEmpty(menuDTOS)) {
            result = menuDTOS;
        }

        return result;
    }

    @Override
    public Long countMenu(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(m.menu_id) ");
        sql.append(" FROM tb_menu m WHERE 1 = 1 AND m.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object menuNumber = query.getSingleResult();

        return Long.parseLong(menuNumber.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (Objects.nonNull(filterSearchAuth.getMenuId())) {
            sql.append(" AND m.menu_id = :menuId ");
            mapParam.put("menuId", filterSearchAuth.getMenuId());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getMenuName())) {
            sql.append(" AND m.menu_nm LIKE :menuName ");
            mapParam.put("menuName", '%' + filterSearchAuth.getMenuName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getMenuUrl())) {
            sql.append(" AND m.menu_url LIKE :menuUrl ");
            mapParam.put("menuUrl", '%' + filterSearchAuth.getMenuUrl().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (m.menu_id LIKE :keyWord OR LOWER(m.menu_nm) LIKE :keyWord OR m.menu_url LIKE :keyWord) ");
            mapParam.put("keyWord", ('%' + filterSearchAuth.getKeyWord().trim() + '%'));
        }
    }
}
