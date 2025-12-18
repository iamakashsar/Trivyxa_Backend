package com.trivyxa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.trivyxa.dto.ContactRequest;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = System.getenv("MAIL_USERNAME");
    private final String TO_EMAIL   = System.getenv("MAIL_TO");

    public void sendContactMail(ContactRequest req) {

        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setFrom(FROM_EMAIL);       // ✅ IMPORTANT
            mail.setTo(TO_EMAIL);           // ✅ configurable
            mail.setSubject("📩 New Project Inquiry – TRIVYXA");

            String body =
                    "========================================\n" +
                    "        🚀 NEW PROJECT INQUIRY\n" +
                    "========================================\n\n" +

                    "Dear TRIVYXA Team,\n\n" +
                    "You have received a new project inquiry from your website.\n\n" +

                    "----------------------------------------\n" +
                    "👤 CLIENT DETAILS\n" +
                    "----------------------------------------\n" +
                    "• Name: " + req.getName() + "\n" +
                    "• Email: " + req.getEmail() + "\n" +
                    "• Phone: " + safe(req.getPhone(), "Not Provided") + "\n\n" +

                    "----------------------------------------\n" +
                    "🧩 PROJECT INFORMATION\n" +
                    "----------------------------------------\n" +
                    "• Selected Service: " + safe(req.getService(), "Not Selected") + "\n" +
                    "• Estimated Budget: " + safe(req.getBudget(), "Not Specified") + "\n\n" +

                    "----------------------------------------\n" +
                    "📝 PROJECT DESCRIPTION\n" +
                    "----------------------------------------\n" +
                    safe(req.getMessage(), "No description provided") + "\n\n" +

                    "========================================\n" +
                    "       📅 Submitted via TRIVYXA.COM\n" +
                    "========================================\n";

            mail.setText(body);

            mailSender.send(mail);

        } catch (MailException ex) {
            // ❌ Do NOT crash the API
            System.err.println("❌ Failed to send email: " + ex.getMessage());
            throw new RuntimeException("Email sending failed");
        }
    }

    private String safe(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value : fallback;
    }
}
