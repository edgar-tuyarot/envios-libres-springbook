package com.enviosp2p.Enviosp2p.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoActivacion(String destinatario, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(destinatario);
            helper.setSubject("Activación de Cuenta - EnviosP2P");

            // HTML del correo
            String htmlContent = """
                <h1>¡Bienvenido a EnviosP2P!</h1>
                <p>Para activar tu cuenta, por favor haz clic en el siguiente enlace:</p>
                <a href="http://localhost:8080/auth/activar?token=%s">ACTIVAR MI CUENTA</a>
                <p>Si no fuiste tú, ignora este mensaje.</p>
                """.formatted(token);

            helper.setText(htmlContent, true); // true indica que es HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            // En producción aquí usaríamos un log.error
            throw new RuntimeException("Error al enviar el correo de activación", e);
        }
    }
}