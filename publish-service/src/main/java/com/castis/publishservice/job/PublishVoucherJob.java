package com.castis.publishservice.job;

import com.castis.publishservice.dto.EndUserDto;
import com.castis.publishservice.dto.request.PublishRequest;
import com.castis.publishservice.entity.EndUser;
import com.castis.publishservice.repository.UserRepository;
import com.castis.publishservice.service.ProcessService;
import com.castis.publishservice.service.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Strings;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor
public class PublishVoucherJob extends QuartzJobBean {

    @Autowired
    private ProcessService processService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        long publishId = dataMap.getLong("publishId");
        String numberNos = dataMap.getString("numberNos");
        String userIdList = dataMap.getString("userIdList");
        log.info("Start publish vouchers of publishId: " + publishId);
        List<Long> userIds;
        if (Objects.nonNull(userIdList) && !userIdList.isEmpty()) {
            List<String> userList = Arrays.asList(Strings.split(userIdList, ','));
            userIds = userList.stream().map(Long::valueOf).collect(Collectors.toList());
        } else {
            // Get list of user based on numberNos
            // Find the latest user with that phone number
            userIds = new ArrayList<>();
            List<String> numberNoList = Arrays.asList(Strings.split(numberNos, ','));
            numberNoList.forEach(number -> {
                Long userId;
                List<EndUser> dbUsers = userRepository.findByUserMobileNumOrderByIdDesc(number);
                if (dbUsers.isEmpty()) {
                    userId = createUser(number);
                } else {
                    userId = dbUsers.stream().findFirst().get().getId();
                }
                userIds.add(userId);
            });
        }

        // change publish status to publishing
        log.info("publish with user ids: {}", userIds);
        List<EndUserDto> users = userService.findAllDtoByIdIn(userIds);
        PublishRequest req = new PublishRequest(publishId, users);
        processService.publishVouchers(publishId, req);
    }

    private long createUser(String number) {
        EndUser endUser = EndUser.builder()
                .userMobileNum(number)
                .userNm(number)
                .build();
        EndUser savedUser = userRepository.save(endUser);
        return savedUser.getId();
    }
}
