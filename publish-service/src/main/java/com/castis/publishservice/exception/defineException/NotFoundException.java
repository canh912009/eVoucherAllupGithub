package com.castis.publishservice.exception.defineException;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private Integer code;

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public static NotFoundException publish(Long publishId) {
        return new NotFoundException("Can not find publish with publishId=" + publishId);
    }
    public static NotFoundException publishDetail(Long publishDetailId) {
        return new NotFoundException("Can not find publish detail with publishDetailId=" + publishDetailId);
    }
    public static NotFoundException voucher(String ev) {
        return new NotFoundException("Can not find voucher with ev=" + ev);
    }
    public static NotFoundException voucherResendHistory(Integer id) {
        return new NotFoundException("Can not find resend history with voucherResendHistoryId=" + id);
    }
    public static NotFoundException customer(String id) {
        return new NotFoundException("Can not find customer with customerId=" + id);
    }
    public static NotFoundException goods(Long id) {
        return new NotFoundException("Can not find product with goodsId=" + id);
    }
}