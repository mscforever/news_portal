package by.romanenko.web_project.utils;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailSending {
    public static void sendEmail(String userEmail, String userMessage, Part inputFile)
            throws IOException, ServletException {
        // класс для работы с набором свойств, хранящихся в виде пар "ключ=значение"
        // в нашем случае вытягиваем адрес почты и пароль к почте из файла
        // email.properties
        Properties properties = loadEmailProperties();

        final String adminEmail = properties.getProperty("admin.email");
        final String password = properties.getProperty("admin.password");

        // создание сессии для управления параметрами соединения и аутентификации при
        // отправке электронной почты
        Session session = createEmailSession(adminEmail, password);

        try {
            Message msg = createEmailMessage(session, adminEmail, userEmail, userMessage, inputFile);
            // отправка сообщения на сервер SMTP через класс Transport из JavaMail API
            Transport.send(msg);
            System.out.println("Сообщение успешно отправлено.");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при отправке сообщения", e);
        }
    }

    private static Properties loadEmailProperties() throws IOException {
        Properties properties = new Properties();
        // создаем входной поток на основе данных, доступных в контексте веб-приложения
        // getClass() не воспользоваться, т.к.getClass() нельзя вызывать из статического
        // контекста
        try (InputStream input = EmailSending.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                throw new IOException("Не удалось найти файл email.properties");
            }
            // станд.метод, считывает данные из указанного входного потока
            properties.load(input);
        }
        return properties;
    }

    private static Session createEmailSession(String adminEmail, String password) {
        // настройка свойств почтового соединения
        // взяты из thunderbird, подключенного к яндекс почте с заданным отдельным
        // паролем для приложения
        Properties mailProps = new Properties();
        // сервер требует проверки подлинности пользователя (логина и пароля)
        mailProps.put("mail.smtp.auth", "true");
        // включение протокола STARTTLS, который используется для обеспечения
        // защищенного соединения при передаче данных по SMTP
        mailProps.put("mail.smtp.starttls.enable", "true");
        // сервер, к которому будет подключаться клиент для отправки электронной почты
        mailProps.put("mail.smtp.host", "smtp.yandex.ru");
        // порт для отправки электронной почты с поддержкой STARTTLS (стандартный порт
        // для SMTP с шифрованием)
        mailProps.put("mail.smtp.port", "587");

        // создается анонимный класс, который наследует Authenticator
        return Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, password);
            }
        });
    }

    private static Message createEmailMessage(Session session, String adminEmail, String userEmail, String userMessage,
                                              Part inputFile) throws MessagingException, IOException, ServletException {
        // создание сообщения
        Message msg = new MimeMessage(session);
        // отправляем от имени администратора на имя администратора
        msg.setFrom(new InternetAddress(adminEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
        // заголовок письма
        msg.setSubject("Сообщение от пользователя: " + userEmail);

        // создание многосоставного сообщения (textPart + attachmentPart)
        Multipart multipart = new MimeMultipart();
        // добавление текстовой части
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(userMessage);
        // добавляем вложение в многосоставное сообщение
        multipart.addBodyPart(textPart);

        // обработка файла во вложении
        if (inputFile != null && inputFile.getSize() > 0) {
            // это экзмепляр для хранения вложения
            MimeBodyPart attachmentPart = createAttachmentPart(inputFile);
            multipart.addBodyPart(attachmentPart);
        }
        // вставляем многосоставное сообщение как содержимое сообщения
        msg.setContent(multipart);
        return msg;
    }

    private static MimeBodyPart createAttachmentPart(Part inputFile)
            throws ServletException, IOException, MessagingException {
        // прописываем, что прикреплять можно только файлы определенных расширений
        String mimeType = inputFile.getContentType();
        if (!"image/png".equals(mimeType) && !"application/pdf".equals(mimeType)) {
            throw new ServletException("Разрешены только файлы форматов PNG и PDF");
        }
        // считывание содержимого файла в массив байтов
        byte[] attachmentData = readAttachmentData(inputFile);
        MimeBodyPart attachmentPart = new MimeBodyPart();
        // создаем обработчик, который будет использовать наш массив байтов и наш тип
        // вложений
        attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachmentData, mimeType)));
        // имя файла = имя загруженного пользователем файла
        attachmentPart.setFileName(inputFile.getSubmittedFileName());
        // файл будет отображаться не как часть текста, а как приложение
        attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
        return attachmentPart;
    }

    public static byte[] readAttachmentData(Part inputFile) throws IOException {
        // ByteArrayOutputStream используется как буффер для временного хранения
        // считанных данных
        // вместо этого можно записать в файл на диске FileOutputStream fileOutputStream
        // = new FileOutputStream("path/to/output/file")
        // или в коллекцию List<Byte> byteList = new ArrayList<>();
        try (InputStream inputStream = inputFile.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
