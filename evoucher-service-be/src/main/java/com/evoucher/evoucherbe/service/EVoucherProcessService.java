package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.dto.partner_service.request.McpTransactionResult;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.message.BaseResponse;

import javax.transaction.Transactional;
import java.util.List;


public interface EVoucherProcessService {
    BaseResponse processUsedVoucher(EVoucherHistoryProcess eVoucherHistoryProcess);

    public BaseResponse processExchangeVoucher(VoucherExchangeReq exchangeReq) throws CustomCodeException;

    public BaseResponse processExchangeVouchers(List<VoucherExchangeReq> exchangeReq) throws CustomCodeException;

    public BaseResponse processCancelingExchange(VoucherExchangeReq exchangeReq) throws CustomCodeException;

    BaseResponse receiveVoucher(EVoucherTransferProcess voucherTransferProcess);

    @Transactional
    BaseResponse processActivateVoucher(EVoucherActivateProcess activateProcessObj) throws CustomCodeException;

    void processActivateVoucher(ActivationRequest activateProcessObj) throws EntityNotFoundException, CustomCodeException;

    void processUpdateVoucher(VoucherJobResultDTO voucherJobResultDTO);

    void processDisableVoucherResult(VoucherDisableProcessResponse response);

    BaseResponse updateVoucherStatus(List<UpdatingVoucherReq> request);

    void handleMcpTransactionResultReceived(McpTransactionResult mcpTransactionResult);
}
