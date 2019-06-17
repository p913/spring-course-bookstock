package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto;

public class UserWithPermissions {
    private long id;

    private String login;

    private String fullName;

    private String email;

    private boolean stockKeeper;

    private UserPermissionInterpretation permReadAndComment;

    private UserPermissionInterpretation permModify;

    private UserPermissionInterpretation permDelete;

    private UserPermissionInterpretation permCreateNew;

    public UserWithPermissions(long id, String login, String fullName, String email, boolean stockKeeper,
                               UserPermissionInterpretation permReadAndComment,
                               UserPermissionInterpretation permModify,
                               UserPermissionInterpretation permDelete) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.email = email;
        this.stockKeeper = stockKeeper;
        this.permReadAndComment = permReadAndComment;
        this.permModify = permModify;
        this.permDelete = permDelete;
        this.permCreateNew = UserPermissionInterpretation.REVOKED;
    }

    public UserWithPermissions(long id, String login, String fullName, String email,  boolean stockKeeper,
                               UserPermissionInterpretation permCreateNew) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.email = email;
        this.stockKeeper = stockKeeper;
        this.permReadAndComment = UserPermissionInterpretation.REVOKED;
        this.permModify = UserPermissionInterpretation.REVOKED;
        this.permDelete = UserPermissionInterpretation.REVOKED;
        this.permCreateNew = permCreateNew;
    }

    public UserWithPermissions() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStockKeeper() {
        return stockKeeper;
    }

    public void setStockKeeper(boolean stockKeeper) {
        this.stockKeeper = stockKeeper;
    }

    public UserPermissionInterpretation getPermReadAndComment() {
        return permReadAndComment;
    }

    public void setPermReadAndComment(UserPermissionInterpretation permReadAndComment) {
        this.permReadAndComment = permReadAndComment;
    }

    public UserPermissionInterpretation getPermModify() {
        return permModify;
    }

    public void setPermModify(UserPermissionInterpretation permModify) {
        this.permModify = permModify;
    }

    public UserPermissionInterpretation getPermDelete() {
        return permDelete;
    }

    public void setPermDelete(UserPermissionInterpretation permDelete) {
        this.permDelete = permDelete;
    }

    public UserPermissionInterpretation getPermCreateNew() {
        return permCreateNew;
    }

    public void setPermCreateNew(UserPermissionInterpretation permCreateNew) {
        this.permCreateNew = permCreateNew;
    }
}
