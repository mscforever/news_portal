package by.romanenko.web_project.utils;

import jakarta.activation.DataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Класс для работы с содержимым в виде массива байтов
//в нашем случае для чтения вложенного в форму содержимого
//читает данные без их сохранения в файл
public class ByteArrayDataSource implements DataSource {
    private final byte[] data;
    private final String contentType;

    public ByteArrayDataSource(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return "ByteArrayDataSource";
    }

    // настройка класса только для чтения
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("This DataSource is read-only");
    }
}
