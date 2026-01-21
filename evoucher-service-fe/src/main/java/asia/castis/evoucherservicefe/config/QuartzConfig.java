package asia.castis.evoucherservicefe.config;

import asia.castis.evoucherservicefe.job.expire.ExpireVoucherJob;
import asia.castis.evoucherservicefe.job.restore.RestoreVoucherJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@Slf4j
public class QuartzConfig {
    @Value("${quartz.expireVoucher.cron}")
    private String expireVoucherCron;
    @Value("${quartz.restoreVoucher.cron}")
    private String restoreVoucherCron;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        log.info("Start configuring quartz");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        log.info("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(DataSource quartzDataSource) {

        // Jobs
        Trigger[] triggers = {
                ExpireVoucherTrigger().getObject(),
                RestoreVoucherTrigger().getObject()
        };

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        log.info("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTriggers(triggers);

        // Comment the following line to use the default Quartz job store.
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean("ExpireVoucherTrigger")
    public CronTriggerFactoryBean ExpireVoucherTrigger() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(ExpireVoucherJobDetail().getObject());
        factoryBean.setCronExpression(expireVoucherCron);
        factoryBean.setName("ExpireVoucherTrigger");
        return factoryBean;
    }

    @Bean
    public JobDetailFactoryBean ExpireVoucherJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ExpireVoucherJob.class);
        factoryBean.setDurability(true);
        factoryBean.setName("ExpireVoucherJobDetail");
        return factoryBean;
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JobDetailFactoryBean RestoreVoucherJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(RestoreVoucherJob.class);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean("RestoreVoucherTrigger")
    public CronTriggerFactoryBean RestoreVoucherTrigger() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(RestoreVoucherJobDetail().getObject());
        factoryBean.setCronExpression(restoreVoucherCron);
        return factoryBean;
    }
}