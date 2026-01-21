package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Category> searchCategory(FilterSearchCms filterSearchCms, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> category = query.from(Category.class);

        List<Predicate> conditions = getConditions(filterSearchCms, cb, category);

        query.select(category);

        if (!CollectionUtils.isEmpty(conditions)) {
            query.where(conditions.toArray(Predicate[]::new));
        }

        return em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long countCategory(FilterSearchCms filterSearchCms, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Category> category = query.from(Category.class);

        List<Predicate> conditions = getConditions(filterSearchCms, cb, category);

        query.select(cb.count(category));

        if (!CollectionUtils.isEmpty(conditions)) {
            query.where(conditions.toArray(Predicate[]::new));
        }
        return em.createQuery(query).getSingleResult();
    }

    private List<Predicate> getConditions(FilterSearchCms filterSearchCms, CriteriaBuilder cb, Root<?> root) {
        List<Predicate> result = new ArrayList<>();

        if (StringUtils.isNotBlank(filterSearchCms.getCategoryCode())) {
            result.add(cb.like(root.get("categoryCode"), String.format("%%%s%%", filterSearchCms.getCategoryCode().trim())));
        }
        if (StringUtils.isNotBlank(filterSearchCms.getCategoryName())) {
            result.add(cb.like(root.get("categoryName"), String.format("%%%s%%", filterSearchCms.getCategoryName().trim())));
        }
        if (filterSearchCms.getValidYn() != null) {
            result.add(cb.equal(root.get("validYn"), filterSearchCms.getValidYn()));
        }
        return result;
    }

}