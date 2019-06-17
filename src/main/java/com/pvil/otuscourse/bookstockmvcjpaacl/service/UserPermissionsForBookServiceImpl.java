package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockPredefinedAuthorities;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserPermissionsForBookServiceImpl implements UserPermissionsForBookService {
    private final MutableAclService aclService;

    public UserPermissionsForBookServiceImpl(MutableAclService aclService) {
        this.aclService = aclService;
    }

    @Override
    public void grantDefaults(long bookId) {
        // Создать SIDы admina и для ролей
        Sid admin = new PrincipalSid(SecurityContextHolder.getContext().getAuthentication());
        Sid roleReader = new GrantedAuthoritySid(BookStockPredefinedAuthorities.ROLE_READER);
        Sid roleStockKeeper = new GrantedAuthoritySid(BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER);

        // Создать пустой ACL для добавляемой книги
        ObjectIdentity oidThisBook = new ObjectIdentityImpl(Book.class, bookId);
        final MutableAcl aclThisBook = aclService.createAcl(oidThisBook);

        // Определить владельца сущности и права пользователей
        aclThisBook.setOwner(admin);
        aclThisBook.insertAce(aclThisBook.getEntries().size(), BasePermission.READ, roleStockKeeper, true);
        aclThisBook.insertAce(aclThisBook.getEntries().size(), BasePermission.WRITE, roleStockKeeper, true);
        aclThisBook.insertAce(aclThisBook.getEntries().size(), BasePermission.DELETE, roleStockKeeper, true);
        aclThisBook.insertAce(aclThisBook.getEntries().size(), BasePermission.READ, roleReader, true);

        // Обновить ACL в БД
        aclService.updateAcl(aclThisBook);
    }

    @Override
    public void set(long bookId, String principal, Boolean grantRead, Boolean grantWrite, Boolean grantDelete) {
        Sid sid = new PrincipalSid(principal);

        ObjectIdentity oidBook = new ObjectIdentityImpl(Book.class, bookId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oidBook);
        } catch (NotFoundException e) {
            acl = aclService.createAcl(oidBook);
        }

        //Перебираем все ACE, не для указанного пользователя - оставляем как есть.
        //Для указанного пользователя - меняем согласно переданным grant***.
        //Здесь grant** интерпретируется так: true - дать доступ, false - запретить доступ,
        // null - доступ не указан явно, т.е. наследовать права от роли и не создавать ACE для пользователя.
        //Роли удаляем и пересоздаем после - роли должны быть в конце списка
        int i = 0;
        while (i < acl.getEntries().size()) {
            if (acl.getEntries().get(i).getSid().equals(sid) ||
                    acl.getEntries().get(i).getSid() instanceof GrantedAuthoritySid)
                acl.deleteAce(i);
            else
                i++;
        }

        //Права для пользователя, которому сейчас права и назначаем
        if (grantRead != null)
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, grantRead);
        if (grantWrite != null)
            acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, grantWrite);
        if (grantWrite != null)
            acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, grantDelete);

        //И в конце права для ролей
        Sid roleReader = new GrantedAuthoritySid(BookStockPredefinedAuthorities.ROLE_READER);
        Sid roleStockKeeper = new GrantedAuthoritySid(BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER);

        acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleStockKeeper, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, roleStockKeeper, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, roleStockKeeper, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, roleReader, true);


        // Обновить ACL в БД
        aclService.updateAcl(acl);
    }

    @Override
    public void set(String principal, Boolean grantCreate) {
        Sid sid = new PrincipalSid(principal);

        ObjectIdentity oidBook = new ObjectIdentityImpl(Book.class, 0);
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oidBook);
        } catch (NotFoundException e) {
            acl = aclService.createAcl(oidBook);
        }

        int i = 0;
        while (i < acl.getEntries().size()) {
            if (acl.getEntries().get(i).getSid().equals(sid) ||
                    acl.getEntries().get(i).getSid() instanceof GrantedAuthoritySid)
                acl.deleteAce(i);
            else
                i++;
        }

        if (grantCreate!= null)
            acl.insertAce(acl.getEntries().size(), BasePermission.CREATE, sid, grantCreate);

        //И в конце права для ролей
        Sid roleStockKeeper = new GrantedAuthoritySid(BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER);

        acl.insertAce(acl.getEntries().size(), BasePermission.CREATE, roleStockKeeper, true);

        // Обновить ACL в БД
        aclService.updateAcl(acl);
    }

    @Override
    public boolean isGrantedReadAndComment(long bookId, String principal) {
        return isGranted(bookId, principal, BasePermission.READ);
    }

    @Override
    public boolean isGrantedWrite(long bookId, String principal) {
        return isGranted(bookId, principal, BasePermission.WRITE);
    }

    @Override
    public boolean isGrantedDelete(long bookId, String principal) {
        return isGranted(bookId, principal, BasePermission.DELETE);
    }

    @Override
    public boolean isGrantedCreateNew(String principal) {
        return isGranted(0, principal, BasePermission.CREATE);
    }

    private boolean isGranted(long bookId, String principal, Permission permission) {
        Sid sid = new PrincipalSid(principal);

        ObjectIdentity oidBook = new ObjectIdentityImpl(Book.class, bookId);
        Acl acl = aclService.readAclById(oidBook);

        // определить какие права и для кого проверять
        List<Permission> permissions = Collections.singletonList(permission);
        List<Sid> sids = Collections.singletonList(sid);
        // выполнить проверку
        return acl.isGranted(permissions, sids, false);
    }

}
