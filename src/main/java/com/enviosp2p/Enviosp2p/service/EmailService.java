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

    //Correo de Activacion
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




    //Correo de recupero de contraseña
    public void enviarCorreoRecuperacion(String correo, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(correo);
            helper.setSubject("Activación de Cuenta - EnviosP2P");

            // HTML del correo
            String htmlContent = """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>
                    <h2 style='color: #333;'>Restablecer Contraseña</h2>
                    <p>Hola,</p>
                    <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>
                    <p>Si fuiste tú, haz clic en el siguiente botón para continuar:</p>
                    
                    <div style='text-align: center; margin: 30px 0;'>
                        <a href='%s' style='background-color: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;'>
                            Restablecer Contraseña
                        </a>
                    </div>
                    
                    <p style='font-size: 12px; color: #666;'>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>
                    <p style='font-size: 12px; color: #007bff; word-break: break-all;'>%s</p>
                    
                    <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>
                    <p style='font-size: 12px; color: #999;'>Si no solicitaste este cambio, puedes ignorar este correo.</p>
                </div>
                """.formatted(link, link);

            helper.setText(htmlContent, true); // true indica que es HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            // En producción aquí usaríamos un log.error
            throw new RuntimeException("Error al enviar el correo de activación", e);
        }
    }


}