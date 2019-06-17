package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.User;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.UserPermissionsForBookService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.UsersService;
import com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto.BookWithPermissions;
import com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto.UserPermissionInterpretation;
import com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller.dto.UserWithPermissions;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller()
@RequestMapping("/admin")
public class AdminController {
    private final UsersService usersService;

    private final BookStockService bookStockService;

    private final UserPermissionsForBookService userPermissionsForBookService;

    public AdminController(UsersService usersService, BookStockService bookStockService, UserPermissionsForBookService userPermissionsForBookService) {
        this.usersService = usersService;
        this.bookStockService = bookStockService;
        this.userPermissionsForBookService = userPermissionsForBookService;
    }

    @GetMapping("/permissions")
    public String getAdminPage(Model model) {
        List<User> users = usersService.getAll();
        List<Book> books = bookStockService.getAllBooks();

        List<BookWithPermissions> bookPermissions = books.stream().map(b -> {
            BookWithPermissions bookWithPermissions = new BookWithPermissions(b);
            users.forEach(u -> {
                bookWithPermissions.getUserWithPermissionsList().add(
                        new UserWithPermissions(u.getId(), u.getLogin(), u.getFullName(),
                                u.getEmail(), u.isStockKeeper(),
                                getUserPermissionInterpretationForReadAndComment(b.getId(), u.getLogin()),
                                getUserPermissionInterpretationForWrite(b.getId(), u.getLogin()),
                                getUserPermissionInterpretationForDelete(b.getId(), u.getLogin())));
            });
            return bookWithPermissions;
        }).collect(Collectors.toList());

        List<User> stockKeepers = usersService.getStockKeepers();
        BookWithPermissions newBookPermissions = new BookWithPermissions(new Book(0));
        stockKeepers.forEach(u -> {
            newBookPermissions.getUserWithPermissionsList().add(
                    new UserWithPermissions(u.getId(), u.getLogin(), u.getFullName(),
                            u.getEmail(), u.isStockKeeper(),
                            getUserPermissionInterpretationForCreateNew(u.getLogin())));
                });

        model.addAttribute("booksPermissions", bookPermissions);
        model.addAttribute("newbookPermissions", newBookPermissions);
        return "admin";
    }

    @PostMapping("/permissionsforbook")
    public String setPermissionsForBook(Model model,
                                 long bookId,
                                 long userId,
                                 @RequestParam("read") UserPermissionInterpretation permReadAndComment,
                                 @RequestParam("write") UserPermissionInterpretation permWrite,
                                 @RequestParam("delete") UserPermissionInterpretation permDelete) {
        Optional<Book> book = bookStockService.getBookById(bookId);
        Optional<User> user = usersService.getById(userId);

        if (book.isPresent() && user.isPresent())
            userPermissionsForBookService.set(bookId, user.get().getLogin(),
                    translateFromUserPermissionInterpretation(permReadAndComment),
                    translateFromUserPermissionInterpretation(permWrite),
                    translateFromUserPermissionInterpretation(permDelete));

        return getAdminPage(model);
    }

    @PostMapping("/permissionsfornew")
    public String setPermissionsForCreateNew(Model model,
                                              long userId,
                                              @RequestParam("create") UserPermissionInterpretation permCreate) {
        Optional<User> user = usersService.getById(userId);

        if (user.isPresent())
            userPermissionsForBookService.set(user.get().getLogin(),
                    translateFromUserPermissionInterpretation(permCreate));

        return getAdminPage(model);
    }

    private Boolean translateFromUserPermissionInterpretation(UserPermissionInterpretation perm) {
        switch (perm) {
            case GRANTED: return true;
            case REVOKED: return false;
            default: return null;
        }
    }

    private UserPermissionInterpretation getUserPermissionInterpretationForReadAndComment(long bookId, String principal) {
        UserPermissionInterpretation perm = UserPermissionInterpretation.INHERITED;
        try {
            perm = userPermissionsForBookService.isGrantedReadAndComment(bookId, principal)
                    ? UserPermissionInterpretation.GRANTED
                    : UserPermissionInterpretation.REVOKED;
        } catch (NotFoundException e) {
        }
        return perm;
    }

    private UserPermissionInterpretation getUserPermissionInterpretationForWrite(long bookId, String principal) {
        UserPermissionInterpretation perm = UserPermissionInterpretation.INHERITED;
        try {
             perm = userPermissionsForBookService.isGrantedWrite(bookId, principal)
                    ? UserPermissionInterpretation.GRANTED
                    : UserPermissionInterpretation.REVOKED;
        } catch (NotFoundException e) {
        }
        return perm;
    }

    private UserPermissionInterpretation getUserPermissionInterpretationForDelete(long bookId, String principal) {
        UserPermissionInterpretation perm = UserPermissionInterpretation.INHERITED;
        try {
            perm = userPermissionsForBookService.isGrantedDelete(bookId, principal)
                    ? UserPermissionInterpretation.GRANTED
                    : UserPermissionInterpretation.REVOKED;
        } catch (NotFoundException e) {
        }
        return perm;
    }

    private UserPermissionInterpretation getUserPermissionInterpretationForCreateNew(String principal) {
        UserPermissionInterpretation perm = UserPermissionInterpretation.INHERITED;
        try {
            perm = userPermissionsForBookService.isGrantedCreateNew(principal)
                    ? UserPermissionInterpretation.GRANTED
                    : UserPermissionInterpretation.REVOKED;
        } catch (NotFoundException e) {
        }
        return perm;
    }

}
