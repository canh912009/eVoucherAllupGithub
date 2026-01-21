package asia.castis.evoucherservicefe.publishrequest.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String to, String template,
                          String voucherUrl, String serialNumber, String expireMessage) throws MessagingException, UnsupportedEncodingException {

        log.info("Start sending voucher to {}", to);
        Context context = new Context();
        // Set variables for the template from the POST request data
        context.setVariable("voucherUrl", voucherUrl);
        context.setVariable("serialNumber", serialNumber);
        context.setVariable("expireMessage", expireMessage);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String htmlContent = templateEngine.process(template, context);

        helper.setFrom("no_reply@aquaretail.net");
        helper.setTo(to);
        helper.setSubject("Bạn đã nhận được voucher từ aQuà!");
        helper.setText(htmlContent, true); // Set true for HTML content

        // Send the email
        mailSender.send(mimeMessage);
        log.info("Voucher sent successfully. voucherUrl={}", voucherUrl);
    }
}