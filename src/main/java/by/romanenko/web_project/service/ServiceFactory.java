package by.romanenko.web_project.service;

import by.romanenko.web_project.dao.DAOFactory;
import by.romanenko.web_project.service.impl.*;

public final class ServiceFactory {

    private static ServiceFactory instance;

    // Сервисы, инициализируемые при первом доступе
    private ICheckService checkService;
    private INewsService newsService;
    private IAuthorizationService authorizationService;
    private IRegistrationService registrationService;
    private IChangeProfileService changeProfileService;
    private ICookiesService cookiesService;


    // Статический блок для инициализации фабрики
//    static {
//            instance = new ServiceFactory();
//    }
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    // Метод для получения экземпляра фабрики
//    public static ServiceFactory getInstance() {
//        if (instance == null) {
//            throw new IllegalStateException("ServiceFactory не был инициализирован.");
//        }
//        return instance;
//    }

    // Ленивая инициализация сервисов
    public ICheckService getCheckService() throws ServiceException {
        if (checkService == null) {
            checkService = new CheckServiceImpl();
        }
        return checkService;
    }

    public INewsService getNewsService() throws ServiceException {
        if (newsService == null) {
            newsService = new NewsServiceImpl();
        }
        return newsService;
    }

    public IAuthorizationService getAuthorizationService() throws ServiceException {
        if (authorizationService == null) {
            authorizationService = new AuthorizationServiceImpl();
        }
        return authorizationService;
    }

    public IRegistrationService getRegistrationService() throws ServiceException {
        if (registrationService == null) {
            registrationService = new RegistrationServiceImpl();
        }
        return registrationService;
    }

    public IChangeProfileService getChangeProfileService() throws ServiceException {
        if (changeProfileService == null) {
            changeProfileService = new ChangeProfileServiceImpl();
        }
        return changeProfileService;
    }

    public ICookiesService getCookiesService() throws ServiceException {
        if (cookiesService == null) {
            cookiesService = new CookiesServiceImpl();
        }
        return cookiesService;
    }
}
