package asia.castis.evoucher.push;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;

@EnableRabbit
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
public class PushAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PushAgentApplication.class, args);
    }
    /*
    @Bean
    public ApplicationRunner testAddPublishSchedule() {
        log.debug("ApplicationRunner Debug");
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                SendingInquiryScheduler.getListPublicScheduleIdToInquiry().add(1);
                SendingInquiryScheduler.getListPublicScheduleIdToInquiry().add(2);
                SendingInquiryScheduler.getListPublicScheduleIdToInquiry().add(3);
            }
        };
    }
     */
    /*
    @Bean
    public ApplicationRunner startupMailSender(JavaMailSender mailSender) {
        return (args) -> mailSender.send((msg) -> {
            var helper = new MimeMessageHelper(msg);
            helper.setTo("dangthedoan@gmail.com");
            helper.setFrom("doan@castis.asia");
            helper.setSubject("8-3 Sending E-mail");
            helper.setText("All is well.");
        });
    }
     */
}
