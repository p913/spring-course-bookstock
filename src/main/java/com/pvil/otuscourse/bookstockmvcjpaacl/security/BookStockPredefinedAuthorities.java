package com.pvil.otuscourse.bookstockmvcjpaacl.security;

public class BookStockPredefinedAuthorities {
    public static final String ADMIN_USER_LOGIN = "admin";

    /**
     * Все зарегистрированные пользователи являются читателями и могут оставлять комментарии
     */
    public static final String ROLE_READER = "ROLE_READER";

    /**
     * Некоторые зарегистрированные пользователи могут быть кладовщиками - добавлять,
     * удалять и исправлять описание книг, модерировать отзывы.
     */
    public static final String ROLE_STOCK_KEEPER = "ROLE_STOCK_KEEPER";

    /**
     * Предопределенный пользователь admin имеет роль ROLE_ADMIN.
     * В частности, роль позволяет назначать права доступа к книгам другим пользователям
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
