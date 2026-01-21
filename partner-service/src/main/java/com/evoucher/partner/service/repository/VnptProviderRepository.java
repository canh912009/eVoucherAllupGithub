package com.evoucher.partner.service.repository;


import com.evoucher.partner.service.bean.entity.VnptProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VnptProviderRepository extends JpaRepository<VnptProvider, String> {

}