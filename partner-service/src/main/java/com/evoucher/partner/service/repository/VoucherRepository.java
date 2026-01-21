package com.evoucher.partner.service.repository;

import com.evoucher.partner.service.bean.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;



public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    @Query(value = "select count(tgv.id) from tb_voucher v inner join tb_goods g on v.goods_id = g.goods_id inner join tb_goods_vnpt tgv on g.goods_id = tgv.parent_goods_id where ev = ?1 and tgv.provider_cd = ?2 and tgv.valid_yn = 'Y'", nativeQuery = true)
    Long countVoucherByEvAndValidProviderCode(String ev, String providerCode);
}