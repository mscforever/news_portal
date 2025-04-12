package by.romanenko.web_project.controller.utils;

import by.romanenko.web_project.model.UserRole;

public class UrlFormatterUtil {
    public static String formatRedirectUrl(UserRole role){
        return "GO_TO_"+role+"_ACCOUNT_PAGE";
    }
}
