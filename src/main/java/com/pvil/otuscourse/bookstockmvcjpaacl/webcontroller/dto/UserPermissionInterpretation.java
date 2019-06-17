package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto;

public enum UserPermissionInterpretation {

    /**
     * Доступ разрешен данному пользователю
     */
    GRANTED,

    /**
     * Доступ разрешен данному пользователю
     */
    REVOKED,

    /**
     * Доступ определяются ролями пользователя, т.е. не
     * указывается конкретно для данного пользователя. Это означает
     * отсутствие записи в ACL
     */
    INHERITED;
}
