package com.castis.publishservice.repository;

import com.castis.publishservice.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<EndUser, Long> {
    List<EndUser> findByUserMobileNumLikeAndUserNmLike(String userMobileNum, String userNm);
    List<EndUser> findByUserMobileNumOrderByIdDesc(String userMobileNum);
}
