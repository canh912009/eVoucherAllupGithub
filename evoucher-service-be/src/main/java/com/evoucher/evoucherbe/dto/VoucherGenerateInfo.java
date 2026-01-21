package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.entity.SettlementLog;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VoucherGenerateInfo {
    private final List<EVoucher> eVouchers;
    private final List<String> urlShortLinks;
    private final List<SettlementLog> settlementLogs;

    public VoucherGenerateInfo() {
        this.eVouchers = new ArrayList<>();
        this.urlShortLinks = new ArrayList<>();
        this.settlementLogs = new ArrayList<>();
    }

    public synchronized void addEVoucher(EVoucher eVoucher) {
        eVouchers.add(eVoucher);
    }

    public synchronized void addUrlShortLinks(String urlShortLink) {
        urlShortLinks.add(urlShortLink);
    }

    public synchronized void addSettlementLogs(SettlementLog settlementLog) {
        settlementLogs.add(settlementLog);
    }
}
