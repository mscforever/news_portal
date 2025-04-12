package by.romanenko.web_project.utils.ImageUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;

public interface ImagePathReadable {

    String getImagePathFromExternalStorage(HttpServletRequest request) throws IOException, ServletException;

    String getImagePathFromProjectStorage(HttpServletRequest request) throws IOException, ServletException;

    String getImagePathFromWeb(HttpServletRequest request, URI uri) throws IOException;
}
