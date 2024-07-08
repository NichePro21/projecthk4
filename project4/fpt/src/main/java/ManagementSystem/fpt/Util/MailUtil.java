//package ManagementSystem.fpt.Util;
//
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//
//@Component
//public class MailUtil {
//
////	@Value("${spring.mail.username}")
////	private String fromMail;
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Autowired
//    TemplateEngine templateEngine;
//
//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        javaMailSender.send(message);
//    }
//
//    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//
//        try {
//            helper.setTo(to);
//            helper.setSubject(subject);
//            String htmlContent = templateEngine.process(templateName, context);
//            helper.setText(htmlContent, true);
//            javaMailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            // Handle exception
//        }
//    }
//}
