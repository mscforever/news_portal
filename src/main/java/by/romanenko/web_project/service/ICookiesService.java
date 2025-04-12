package by.romanenko.web_project.service;

import by.romanenko.web_project.model.Auth;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface ICookiesService {
    Cookie createOrUpdateRememberMeCookie (HttpServletRequest request, Auth auth) throws ServiceException;
}
