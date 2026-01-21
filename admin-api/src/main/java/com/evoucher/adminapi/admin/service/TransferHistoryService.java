package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.EVoucherRepository;
import com.evoucher.adminapi.admin.dao.VoucherTransferHistoryRepository;
import com.evoucher.adminapi.admin.dao.models.EVoucher;
import com.evoucher.adminapi.admin.dao.models.VoucherTransferHistory;
import com.evoucher.adminapi.admin.enums.TransferHistoryGetDTOType;
import com.evoucher.adminapi.admin.service.models.CsTransferHistoryDTO;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferHistoryService {
    private final VoucherTransferHistoryRepository repository;
    private final EVoucherRepository eVoucherRepository;

    public Set<CsTransferHistoryDTO> findHistoryByVoucherId(String ev) throws CustomCodeException {
        try {
            List<CsTransferHistoryDTO> firstHistoryNode = repository.findFirstHistoryNodeByVoucherId(ev);

            if (firstHistoryNode == null || firstHistoryNode.isEmpty()) {
                return new HashSet<>();
            } else {
                HashSet<CsTransferHistoryDTO> result = new HashSet<>();
                HashSet<String> relatedVoucher = new HashSet<>();
                firstHistoryNode.forEach(o -> {
                    relatedVoucher.add(o.getVoucherUUID());
                    relatedVoucher.add(o.getToVoucherUUID());
                    result.add(o);
                    List<CsTransferHistoryDTO> allHistory;
                    if (o.getType().equals(TransferHistoryGetDTOType.FROM_VOUCHER.toString())) {
                         allHistory = repository.findHistoryListInRange(o.getStartDate(), o.getTransferDate());
                    } else {
                        allHistory = repository.findHistoryListInRange(o.getTransferDate(), o.getEndDate());
                    }
                    getAllRelatedTransferHistory(result, relatedVoucher, allHistory);
                });
                return new TreeSet<>(result);
            }

        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomCodeException.internalException(e);
        }
    }

    private void getAllRelatedTransferHistory(Set<CsTransferHistoryDTO> result,
                                              HashSet<String> foundRelatedVoucher,
                                              List<CsTransferHistoryDTO> allHistory) {
        allHistory.forEach(o -> {
            if (foundRelatedVoucher.contains(o.getVoucherUUID())) {
                foundRelatedVoucher.add(o.getVoucherUUID());
            }
            if (foundRelatedVoucher.contains(o.getToVoucherUUID())) {
                foundRelatedVoucher.add(o.getToVoucherUUID());
            }
            if (foundRelatedVoucher.contains(o.getVoucherUUID()) || foundRelatedVoucher.contains(o.getToVoucherUUID())) {
                result.add(o);
            }
        });
    }

    public Set<CsTransferHistoryDTO> findHistoryByVoucherIdV2(String ev) throws CustomCodeException {
        List<VoucherTransferHistory> voucherTransferHistories = repository.findTransferHistoryByVoucherId(ev);
        return voucherTransferHistories.stream()
                .map(
                        voucherTransferHistory -> {
                            log.info("Find target Voucher with ev: {}", voucherTransferHistory.getToEv());
                            EVoucher targetVoucher = eVoucherRepository.findById(voucherTransferHistory.getToEv())
                                    .orElseThrow(() -> new CustomCodeException(
                                            MessageUtils.getMessage("evoucher.voucher.not.found"),
                                            HttpStatus.BAD_REQUEST
                                    ));
                            return CsTransferHistoryDTO.builder()
                                    .id(voucherTransferHistory.getId().longValue())
                                    .voucherUUID(voucherTransferHistory.getFromEv())
                                    .toVoucherUUID(voucherTransferHistory.getToEv())
                                    .transferDate(voucherTransferHistory.getTransactionDate())
                                    .targetNumber(voucherTransferHistory.getToUser().getUserMobileNum())
                                    .targetName(voucherTransferHistory.getToUser().getUserNm())
                                    .accessLink(targetVoucher.getShortLink())
                                    .pinStatus(targetVoucher.getVoucherStatusCode())
                                    .pin(targetVoucher.getExternalPinNo())
                                    .transferStatusCode(voucherTransferHistory.getTransferStatusCode())
                                    .build();
                        })
                .collect(Collectors.toSet());
    }
}
