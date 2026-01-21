package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import com.evoucher.adminapi.cms.dao.XPAYProviderRepository;
import com.evoucher.adminapi.cms.dao.models.XPAYProvider;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.XPAYProviderDto;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.cms.service.XPAYProviderBasicService.ErrorCode;
import static com.evoucher.adminapi.cms.service.XPAYProviderBasicService.getErrorMessageByCode;
import static com.evoucher.adminapi.common.utils.Constant.pageableFromFilter;

@Service
@Slf4j
@RequiredArgsConstructor
public class XPAYProviderService {
    private final XPAYProviderBasicService basicService;
    private final XPAYProviderRepository repository;

    public BaseResponse getAll(FilterSearchCms filter, Integer page, Integer pageSize) {
        Pageable pageable = pageableFromFilter(filter, page, pageSize);
        try {
            Specification<MessageTemplate> condition = generateSearchFilter(filter);
            long count = repository.count(condition);
            BaseResponse response = new BaseResponse();
            response.setTotalCount(count);
            if (count > 0) {
                List<XPAYProviderDto> list = Optional.ofNullable(repository.findAll(condition, pageable))
                        .orElse(new ArrayList<>())
                        .stream().map(basicService::toDto)
                        .collect(Collectors.toList());
                response.setData(list);
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private Specification<MessageTemplate> generateSearchFilter(FilterSearchCms filter) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(filter)) {
                // Return an always-true predicate if the list is empty
                return criteriaBuilder.conjunction();
            }
            List<Predicate> conditions = new ArrayList<>();
            if (Objects.nonNull(filter.getValidYn())) {
                conditions.add(criteriaBuilder.equal(root.get("validYn"), filter.getValidYn()));
            }
            if (Objects.nonNull(filter.getProviderCode())) {
                conditions.add(criteriaBuilder.like(root.get("providerCd"), String.format("%%%s%%", filter.getProviderCode())));
            }
            if (Objects.nonNull(filter.getProviderName())) {
                conditions.add(criteriaBuilder.like(root.get("providerNm"), String.format("%%%s%%", filter.getProviderName())));
            }

            if (CollectionUtils.isEmpty(conditions)) {
                // Return an always-true predicate if the list is empty
                return criteriaBuilder.conjunction();
            } else {
                return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
            }
        };
    }

    public BaseResponse delete(String id) throws CustomCodeException {
        try {
            XPAYProvider exist = basicService.findById(id);

            if (exist.getValidYn() == EnumValidYn.N) {
                log.error("provider: {} is invalid", id);
                throw new CustomCodeException(
                        ErrorCode.INVALID,
                        getErrorMessageByCode(ErrorCode.INVALID),
                        HttpStatus.BAD_REQUEST
                );
            }
            exist.setValidYn(EnumValidYn.N);
            basicService.save(exist);
            return BaseResponse.ok(exist.getProviderCd());
        } catch (EntityNotFoundException e) {
            log.error("can not find XPAY provider by id: " + id);
            throw new CustomCodeException(
                    ErrorCode.NOT_FOUND,
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public BaseResponse findById(String id) throws CustomCodeException {
        try {
            return BaseResponse.ok(basicService.findDtoById(id));
        } catch (EntityNotFoundException e) {
            log.error("can not find XPAY provider by id: " + id);
            throw new CustomCodeException(
                    ErrorCode.NOT_FOUND,
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public BaseResponse createNew(@Valid XPAYProviderDto provider) throws CustomCodeException {
        if (basicService.existById(provider.getProviderCd())) {
            XPAYProvider existed = basicService.findById(provider.getProviderCd());
            if (existed.getValidYn() == EnumValidYn.Y) {
                log.error("provider [{}] is existed", provider.getProviderCd());
                throw new CustomCodeException(
                        ErrorCode.EXISTED,
                        MessageUtils.getMessage(getErrorMessageByCode(ErrorCode.EXISTED), provider.getProviderCd()),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        XPAYProvider result = basicService.save(basicService.toEntity(provider));
        return BaseResponse.ok(basicService.toDto(result));
    }

    @Transactional
    public BaseResponse update(@Valid XPAYProviderDto provider) throws CustomCodeException {
        XPAYProvider oldProvider = basicService.findById(provider.getProviderCd());
        provider.setTopupProviderCd(oldProvider.getTopupProviderCd());

        XPAYProvider result = basicService.save(basicService.toEntity(provider));
        return BaseResponse.ok(basicService.toDto(result));
    }

}
