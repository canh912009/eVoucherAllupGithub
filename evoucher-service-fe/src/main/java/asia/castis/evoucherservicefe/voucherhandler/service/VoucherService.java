package asia.castis.evoucherservicefe.voucherhandler.service;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.Voucher;
import asia.castis.evoucherservicefe.common.enums.EnumMessageType;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;
import asia.castis.evoucherservicefe.exceptions.ParseRequestException;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.publishrequest.dto.ChosenRequest;
import asia.castis.evoucherservicefe.voucherhandler.dto.UsingVoucherRequest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface VoucherService {
    @NotNull
    VoucherModel parseVoucher(Voucher voucherFromRequest, Long id, EnumMessageType anEnum) throws ParseRequestException;

    ResponseData<List<String>> chooseChoiceItem(ChosenRequest choiceRequest);

    void activateVoucher(ActivateRequest activateRequest) throws Exception;

    void saveVouchers(List<VoucherModel> vouchers);

    VoucherModel findById(String ev) throws NotFoundException;

    void saveOne(VoucherModel voucherModel);

    void delete(Set<String> tobeDeleteVouchers);

}
