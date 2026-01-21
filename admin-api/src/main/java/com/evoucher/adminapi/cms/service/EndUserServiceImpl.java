package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.dao.EndUserRepository;
import com.evoucher.adminapi.cms.dao.models.EndUser;
import com.evoucher.adminapi.cms.mapper.EndUserMapper;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndUserServiceImpl implements EndUserService {

    private final EndUserRepository endUserRepository;

    private final EndUserMapper endUserMapper;

    @Override
    public EndUserDTO findById(String id) {
        log.info("Find EndUser with userId: {}", id);
        EndUser endUser = endUserRepository.findByUserMobileNum(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.user.not.found"),
                        HttpStatus.BAD_REQUEST));
        return endUserMapper.toDTO(endUser);
    }

    @Override
    public EndUserDTO createEndUser(EndUserRequest endUserRequest) {
        log.info("Find EndUser with userId: {}", endUserRequest.getUserMobileNum());
        boolean existsById = endUserRepository.existsByUserMobileNum(endUserRequest.getUserMobileNum());
        if (existsById)
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.user.exists"),
                    HttpStatus.BAD_REQUEST);
        EndUser user = endUserMapper.toEntity(endUserRequest);
        log.info("Save EndUser with userId: {}", endUserRequest.getUserMobileNum());
        return endUserMapper.toDTO(endUserRepository.save(user));

    }

    @Override
    public EndUserDTO updateEndUser(String id, EndUserRequest endUserRequest) {
        log.info("Find EndUser with userId: {}", id);
        EndUser endUser = endUserRepository.findByUserMobileNum(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.user.not.found"),
                        HttpStatus.BAD_REQUEST));
        EndUser user = endUserMapper.toEntity(endUserRequest);
        log.info("Update EndUser with userId: {}", endUserRequest.getUserMobileNum());
        return endUserMapper.toDTO(endUserRepository.save(user));
    }

    @Override
    public String deleteEndUserById(String id) {
        log.info("Find EndUser with userId: {}", id);
        EndUser endUser = endUserRepository.findByUserMobileNum(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.user.not.found"),
                        HttpStatus.BAD_REQUEST));
        log.info("Delete EndUser with userId: {}", id);
        endUserRepository.delete(endUser);
        return id;
    }

    @Override
    public Page<EndUserDTO> search(FilterSearchCms filterSearchCms) {
        log.info("Start Search EndUser");
        Pageable pageable = PageRequest.of(filterSearchCms.getPage() - 1, filterSearchCms.getPageSize());
        String userMobileNum = filterSearchCms.getUserMobileNum();
        String userNm = filterSearchCms.getUserName();

        Page<EndUser> endUsers = null;
        if (Objects.isNull(userMobileNum) && Objects.isNull(userNm)) {
            // Tìm kiếm tất cả
            endUsers = endUserRepository.findAll(pageable);
        } else if (Objects.isNull(userMobileNum) || Objects.isNull(userNm)) {
            // Tìm kiếm theo một trong hai trường
            endUsers = endUserRepository.findByUserMobileNumOrUserNm(userMobileNum, userNm, pageable);
        } else {
            // Tìm kiếm cả hai trường cùng lúc
            endUsers =  endUserRepository.findByUserMobileNumAndUserNm(userMobileNum, userNm, pageable);
        }
        return new PageImpl<>(endUserMapper.toListDTO(endUsers.getContent()), pageable, endUsers.getTotalElements());
    }
}
