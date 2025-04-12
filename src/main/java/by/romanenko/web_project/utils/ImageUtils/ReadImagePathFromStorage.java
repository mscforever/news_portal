package by.romanenko.web_project.utils.ImageUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ReadImagePathFromStorage implements ImagePathReadable {

    /**
     * Метод для сохранения изображения на компьютере
     */
    @Override
    public String getImagePathFromExternalStorage(HttpServletRequest request) throws IOException, ServletException {
        // указываем абсолютный путь, где будем сохранять картинки
        String externalDirPath = "E:/практика/webapps/homeWork05/news_agregator/src/main/webapp/images";

        File imagesDir = getDirOrCreateIfAbsent(externalDirPath);

        return saveImage(request, imagesDir);
    }

    /**
     * Метод для сохранения изображения в папке проекта на томкат
     * В данном случае корневая директория - webapp
     */
    @Override
    public String getImagePathFromProjectStorage(HttpServletRequest request) throws IOException, ServletException {
        // указываем путь относительно корневой директории проекта (ГДЕ РАЗВЕРНУТО ПРИЛОЖЕНИЕ)

        // используем request для получения информации о веб-приложении, в котором этот реквест обрабатывается
        ServletContext context = request.getServletContext();
        // папка которую надо найти в веб-приложении
        String relativePath = "images";
        // ServletContext возвращает абсолютный путь к папке
        String absolutePath = context.getRealPath(relativePath);

        File imagesDir = getDirOrCreateIfAbsent(absolutePath);

        return saveImage(request, imagesDir);
    }

    //этот метод не должен быть реализован в этой версии метода чтения имени файла
    @Override
    public String getImagePathFromWeb(HttpServletRequest request, URI uri) throws IOException {
        return null;
    }

    /**
     * Метод для проверки, есть ли папка
     * если она отсутствует, то создаем ее
     */
    private File getDirOrCreateIfAbsent(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * Метод сохраняет картинку в указанную папку из параметров и возвращает имя сохраненной картинки,
     * которое трансформируется в путь к картинке путем добавления названия папки и слеша
     *
     * @param imagesDir - папка, куда будем сохранять картинки
     *                  return путь к картинке, который будет сохраняться в БД и впоследствии использоваться для получения доступа к картинке
     */
    private String saveImage(HttpServletRequest request, File imagesDir) throws IOException, ServletException {
        String relativePath = "images";

        // по имени поля из формы из реквеста получаем объект Part, который хранит не только картинку, но и ее атрибуты
        Part newsPicPart = request.getPart("newsPic");

        if (newsPicPart != null && newsPicPart.getSize() > 0) {
            // оригинальное имя файла, который был отправлен пользователем через форму
            String fileName = newsPicPart.getSubmittedFileName();
            // имя файла после слешей
            String baseFileName = new File(fileName).getName();
            // создаем в папке imagesDir объект, который указывает на местоположение для файла baseFileName
            File file = new File(imagesDir, baseFileName);

            try (InputStream inputStream = newsPicPart.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return relativePath + "/" + baseFileName;
        }
        return null;
    }
}

