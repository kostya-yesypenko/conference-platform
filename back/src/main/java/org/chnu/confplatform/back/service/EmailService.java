package org.chnu.confplatform.back.service;


import freemarker.template.TemplateException;
import org.chnu.confplatform.back.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    public void sendHtmlMessage(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("BAKERY");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        emailSender.send(message);
    }

    public String getHtmlConfirmationContent(User user, String confirmationLink, String link) throws IOException {
        String htmlContent = "";
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("confirmationLink", confirmationLink);
            model.put("link", link);
            freemarkerConfigurer.getConfiguration().getTemplate("mail-confirmation-email.ftl").process(model, stringWriter);
            htmlContent = stringWriter.getBuffer().toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return htmlContent;
    }

    public String getHtmlNotificationStatusConfirmContent(User user, String link) throws IOException {
        String htmlContent = "";
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("link", link);
            freemarkerConfigurer.getConfiguration().getTemplate("mail-notification-status-confirm-email.ftl").process(model, stringWriter);
            htmlContent = stringWriter.getBuffer().toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return htmlContent;
    }

    public String getHtmlNotificationStatusDeliveryContent(User user, String link) throws IOException {
        String htmlContent = "";
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("link", link);
            freemarkerConfigurer.getConfiguration().getTemplate("mail-notification-status-delivery-email.ftl").process(model, stringWriter);
            htmlContent = stringWriter.getBuffer().toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return htmlContent;
    }

    public String getHtmlNotificationStatusDenyContent(User user, String link) throws IOException {
        String htmlContent = "";
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("link", link);
            freemarkerConfigurer.getConfiguration().getTemplate("mail-notification-status-deny-email.ftl").process(model, stringWriter);
            htmlContent = stringWriter.getBuffer().toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return htmlContent;
    }
}
