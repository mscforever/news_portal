package by.romanenko.web_project.utils.ImageUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;

public class ReadImagePathFromWeb implements ImagePathReadable {
    //этот метод не должен быть реализован в этой версии метода чтения имени файла
    @Override
    public String getImagePathFromExternalStorage(HttpServletRequest request) throws IOException, ServletException {
        return null;
    }

    //этот метод не должен быть реализован в этой версии метода чтения имени файла
    @Override
    public String getImagePathFromProjectStorage(HttpServletRequest request) throws IOException, ServletException {
        return null;
    }

    //будет реализован позже
    @Override
    public String getImagePathFromWeb(HttpServletRequest request, URI uri) throws IOException {
        return "";
    }
}
