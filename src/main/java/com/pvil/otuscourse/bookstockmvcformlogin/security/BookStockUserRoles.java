package com.pvil.otuscourse.bookstockmvcformlogin.security;

public class BookStockUserRoles {
    /**
     * Все зарегистрированные пользователи являются читателями и могут оставлять комментарии
     */
    public static final String ROLE_READER = "ROLE_READER";

    /**
     * Некоторые зарегистрированные пользователи могут быть кладовщиками - добавлять,
     * удалять и исправлять описание книг, модерировать отзывы.
     */
    public static final String ROLE_STOCK_KEEPER = "ROLE_STOCK_KEEPER";
}
