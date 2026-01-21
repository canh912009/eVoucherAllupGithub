package com.castis.pos_api.service.basic;

import com.castis.pos_api.dto.PosTransactionDto;
import com.castis.pos_api.dto.request.CancelRequest;
import com.castis.pos_api.dto.request.FinalizeSingleRequest;
import com.castis.pos_api.dto.request.FinalizeListRequest;
import com.castis.pos_api.dto.request.ValidatingRequest;
import com.castis.pos_api.entity.PosTransaction;
import com.castis.pos_api.enum_constant.PosRequestType;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.mapper.PosTransactionMapper;
import com.castis.pos_api.repositories.PosTransactionRepository;
import com.castis.pos_api.service.common.EntityService;
import com.castis.pos_api.utils.CustomResponse;
import com.castis.pos_api.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class PosTransactionBasicService extends EntityService<PosTransaction, String, PosTransactionDto> {
    private static final PosTransactionMapper MAPPER = PosTransactionMapper.INSTANCE;
    private final PosTransactionRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public JpaRepository<PosTransaction, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "Pos Transaction";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException("can not find pos transaction by id: " + id);
    }

    @Override
    public PosTransaction toEntity(PosTransactionDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public PosTransactionDto toDto(PosTransaction entity) {
        return MAPPER.toDto(entity);
    }

    public PosTransactionDto findDtoById(String id) throws ApplicationException {
        try {
            return toDto(findById(id));
        } catch (EntityNotFoundException e) {
            throw new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Transaction not found");
        }
    }

    public PosTransactionDto saveDto(PosTransactionDto dto) throws ApplicationException {
        log.info("save transaction dto: {}", dto.getTransactionId());
        PosTransaction entity = toEntity(dto);
        entity = this.save(entity);
        log.info("transaction is saved");
        return toDto(entity);
    }

    public PosTransactionDto makeNewByFinalizeSingleItem(FinalizeListRequest request, String appId)
            throws ApplicationException {
        log.info("make new pos transaction from finalize single item request : {}", request);
        try {
            PosTransactionDto transaction = MAPPER.fromFinalizeSingleItemRequest(request);

            // make new transactionId
            transaction.setTransactionId(DataUtils.genNewUUID());
            transaction.setRequestDate(new Date());
            transaction.setAppId(appId);

            transaction.setRequestType(PosRequestType.FINALIZE_SINGLE);
            transaction.setRequestBody(objectMapper.writeValueAsString(request));

            log.info("transaction created");
            return transaction;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }
    public PosTransactionDto makeNewByFinalizePrepaid(FinalizeSingleRequest request, String appId)
            throws ApplicationException {
        log.info("make new pos transaction from finalize prepaid request : {}", request);
        try {
            PosTransactionDto transaction = MAPPER.fromFinalizePrePaidRequest(request);

            // make new transactionId
            transaction.setTransactionId(DataUtils.genNewUUID());
            transaction.setRequestDate(new Date());
            transaction.setAppId(appId);

            transaction.setRequestType(PosRequestType.FINALIZE_PREPAID);
            transaction.setRequestBody(objectMapper.writeValueAsString(request));

            log.info("transaction created");
            return transaction;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }

    public PosTransactionDto makeNewByValidatingRequest(ValidatingRequest request, String appId)
            throws ApplicationException {
        log.info("make new pos transaction from validating request : {}", request);
        try {
            PosTransactionDto transaction = MAPPER.fromValidatingRequest(request);

            // make new transactionId
            transaction.setTransactionId(DataUtils.genNewUUID());
            transaction.setRequestDate(new Date());
            transaction.setAppId(appId);

            transaction.setRequestType(PosRequestType.VALIDATE);
            transaction.setRequestBody(objectMapper.writeValueAsString(request));

            log.info("transaction created");
            return transaction;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }

    }

    public void update(PosTransactionDto transaction, FinalizeSingleRequest request) {
        try {
            transaction.setRequestBody(
                    transaction.getRequestBody()
                            .concat(", finalize: ")
                            .concat(objectMapper.writeValueAsString(request)));
            transaction.setRequestType(PosRequestType.FINALIZE_PREPAID);
            MAPPER.updateByFinalizeRequest(transaction, request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public void update(PosTransactionDto transaction, CancelRequest request) {
        try {
            transaction.setRequestBody(
                    transaction.getRequestBody()
                            .concat(", cancel: ")
                            .concat(objectMapper.writeValueAsString(request)));
            transaction.setRequestType(PosRequestType.CANCEL);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

}
