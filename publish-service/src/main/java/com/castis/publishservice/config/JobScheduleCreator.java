package com.castis.publishservice.config;

import com.castis.publishservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * JobScheduleCreator
 *
 * @author by daont on 15/05/2023
 * @project publish-service
 */

@Slf4j
@Component
public class JobScheduleCreator {

	public JobDetail createSimpleJob(Class<? extends Job> jobClass, boolean isDurable, ApplicationContext context, Long publishId, Long campaignId) {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(jobClass);
		factoryBean.setDurability(isDurable);
		factoryBean.setApplicationContext(context);
		factoryBean.setName(Constants.JOB_CONSTANTS.JOB_NAME_PREFIX.concat(publishId.toString()));
		factoryBean.setGroup(Constants.JOB_CONSTANTS.JOB_GROUP_PREFIX.concat(campaignId.toString()));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	public CronTrigger createCronTrigger(String triggerName, Date startTime, String cronExpression, int misFireInstruction) {
		CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
		factoryBean.setName(triggerName);
		factoryBean.setStartTime(startTime);
		factoryBean.setCronExpression(cronExpression);
		factoryBean.setMisfireInstruction(misFireInstruction);
		try {
			factoryBean.afterPropertiesSet();
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return factoryBean.getObject();
	}

	public SimpleTrigger createSimpleTrigger(Date startTime, Long repeatTime, int misFireInstruction, Long publishId, Long campaignId) {
		SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
		factoryBean.setStartTime(startTime);
		factoryBean.setRepeatInterval(repeatTime);
		factoryBean.setRepeatCount(0);
		factoryBean.setMisfireInstruction(misFireInstruction);
		factoryBean.setName(Constants.JOB_CONSTANTS.TRIGGER_NAME_PREFIX.concat(publishId.toString()));
		factoryBean.setGroup(Constants.JOB_CONSTANTS.TRIGGER_GROUP_PREFIX.concat(campaignId.toString()));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
}
