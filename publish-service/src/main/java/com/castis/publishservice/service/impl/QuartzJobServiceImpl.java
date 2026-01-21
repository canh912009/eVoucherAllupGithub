package com.castis.publishservice.service.impl;

import com.castis.publishservice.config.JobScheduleCreator;
import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.request.PublishRequest;
import com.castis.publishservice.entity.PublishSchedule;
import com.castis.publishservice.exception.defineException.QuartzCreationException;
import com.castis.publishservice.job.PublishVoucherJob;
import com.castis.publishservice.repository.PublishScheduleRepository;
import com.castis.publishservice.service.QuartzJobService;
import com.castis.publishservice.service.external_pin.ExtServiceFactory;
import com.castis.publishservice.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {
    private final SchedulerFactoryBean schedulerFactoryBean;

    private final PublishScheduleRepository publishScheduleRepository;

    private final ApplicationContext context;

    private final JobScheduleCreator scheduleCreator;
    private final ExtServiceFactory extServiceFactory;

    @Override
    @Transactional
    public void createPublishSchedule(PublishDTO publish, PublishRequest publishRequest) throws QuartzCreationException {
        try {
            log.info("Create publish job publish={}, triggerTime={}, publishRequest={}", publish.getId(), publish.getBookingDate(), Utils.toJson(publishRequest));

            //reserve external pin for booking voucher
            if (extServiceFactory.isSupportType(publish.getGood().getSystem())) {
                log.info("Start reserve pins");
                extServiceFactory.getServiceByType(publish.getGood().getSystem())
                        .reservePin(publishRequest.getUsers().size(), publish.getGood(), publish.getBookingDate());
            }

            //create publish schedule
            PublishSchedule publishSchedule = new PublishSchedule();
            publishSchedule.setPublishId(publishRequest.getPublishId());
            publishSchedule.setStartAt(publish.getBookingDate());
            // add publish schedule to scheduler
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = scheduleCreator.createSimpleJob(PublishVoucherJob.class, false, context,
                                        publish.getId(), publish.getCampaign().getId());

            jobDetail.getJobDataMap().put("publishId", publish.getId());
            jobDetail.getJobDataMap()
                    .put(
                            "userIdList",
                            publishRequest.getUsers()
                                    .stream()
                                    .map(
                                            o -> String.valueOf(o.getId()))
                                    .collect(Collectors.joining(",")));

            //create trigger at booking time
            Trigger trigger = scheduleCreator.createSimpleTrigger(publish.getBookingDate(), 1L,
                                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW, publish.getId(), publish.getCampaign().getId());
            scheduler.scheduleJob(jobDetail, trigger);
            publishScheduleRepository.save(publishSchedule);
            log.info("Scheduled publish job successfully for publish={}, triggerTime={}",
                        publish.getId(), publish.getBookingDate());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new QuartzCreationException(e.getMessage());
        }
    }

    @Override
    public void cancelScheduledJob(String jobName, String jobGroup) throws QuartzCreationException {
        log.info("Cancel scheduled job job name={}, job group={}", jobName, jobGroup);
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            // Get all triggers associated with the job
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            for (Trigger trigger : triggers) {
                scheduler.unscheduleJob(trigger.getKey());
            }

            boolean deleted = scheduler.deleteJob(jobKey);

            if (deleted) {
                log.info("Job and all associated triggers successfully removed for job name={}, job group={}", jobName, jobGroup);
            } else {
                log.warn("Job not found or could not be deleted for job name={}, job group={}", jobName, jobGroup);
            }
        } catch (Exception e) {
            log.error("Error when delete job with job name={}, job group={}", jobName, jobGroup);
            log.error(e.getMessage(), e);
            throw new QuartzCreationException(e.getMessage());
        }
    }
}
