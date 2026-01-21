package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.MessageTemplateRepository;
import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import com.evoucher.adminapi.admin.mapper.MessageTemplateMapper;
import com.evoucher.adminapi.admin.service.models.MessageTemplateDTO;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.common.utils.Constant.ERROR_MESSAGE_KEY.MESSAGE_TEMPLATE.NOT_FOUND;
import static com.evoucher.adminapi.common.utils.Constant.ERROR_MESSAGE_KEY.MESSAGE_TEMPLATE.NULL_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageTemplateService {
    private final MessageTemplateRepository repository;
    private static final MessageTemplateMapper MAPPER = MessageTemplateMapper.INSTANCE;

    public static MessageTemplateDTO toDTO(MessageTemplate entity) {
        return MAPPER.toDTO(entity);
    }

    public MessageTemplate findById(Integer id) throws CustomCodeException {
        log.info("find message template by id : {}", id);
        if (Objects.isNull(id)) {
            log.error("message template id is null");
            throw new CustomCodeException(
                    MessageUtils.getMessage(NULL_ID),
                    HttpStatus.BAD_REQUEST
            );
        }
        return repository.findById(id).orElseThrow(
                () -> new CustomCodeException(
                        MessageUtils.getMessage(NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    public List<MessageTemplateDTO> findAllDTO(List<String> system) {
        log.info("get all message templates");
        return repository.findAll(generateFilter(system)).stream()
                .map(MessageTemplateService::toDTO)
                .collect(Collectors.toList());
    }

    private Specification<MessageTemplate> generateFilter(List<String> systems) {
        return (message, query, criteriaBuilder) -> {
            if (!CollectionUtils.isEmpty(systems)) {
                // Construct the IN clause
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(message.get("system"));
                for (String system : systems) {
                    inClause.value(system);
                }
                return inClause;
            } else {
                // Return an always-true predicate if the list is empty
                return criteriaBuilder.conjunction();
            }
        };
    }
}
