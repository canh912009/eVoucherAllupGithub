package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.EVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EVoucherRepository extends JpaRepository<EVoucher, String> {

    List<EVoucher> findAllByCancelDateBefore(Date date);

    @Query("from EVoucher e where e.eV IN :evs")
    List<EVoucher> findAllByEVIn(List<String> evs);
}
