package com.pvil.otuscourse.bookstockmvcformlogin.webcontroller;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcformlogin.security.BookStockUserDetails;
import com.pvil.otuscourse.bookstockmvcformlogin.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcformlogin.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcformlogin.service.CommentariesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class BookStockController {
    private final BookStockService bookStockService;

    private final AuthorsService authorsService;

    private final CommentariesService commentariesService;

    public BookStockController(BookStockService bookStockService, AuthorsService authorsService,
                               CommentariesService commentariesService) {
        this.bookStockService = bookStockService;
        this.authorsService = authorsService;
        this.commentariesService = commentariesService;
    }

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute("books", bookStockService.getAll());
        return "bookstock";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchtext") String searchText, Model model) {
        model.addAttribute("books", bookStockService.getContainsAnyOf(searchText));
        model.addAttribute("searchtext", searchText);
        return "bookstock";
    }

    @GetMapping("/viewbook")
    public String getBookByIdWithComments(@RequestParam("id") String bookId, Model model) {
        model.addAttribute("book", bookStockService.getById(bookId).orElse(new Book()));
        return "viewbook";
    }

    @GetMapping("/editbook")
    @PreAuthorize("hasRole('ROLE_STOCK_KEEPER')")
    public String editBook(@RequestParam(value = "id", required = false) String bookId, Model model) {
        if (bookId != null)
            model.addAttribute("book", bookStockService.getById(bookId).orElse(new Book()));
        else
            model.addAttribute("book", new Book());
        model.addAttribute("authors", authorsService.getAll());
        return "editbook";
    }

    @GetMapping("/delbook")
    @PreAuthorize("hasRole('ROLE_STOCK_KEEPER')")
    public String delBook(@RequestParam(value = "id") String bookId) {
        bookStockService.delete(new Book(bookId));
        return "redirect:/";
    }

    @PostMapping("/savebook")
    @PreAuthorize("hasRole('ROLE_STOCK_KEEPER')")
    public String save(@Valid Book book,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "editbook";
        }

        if (book.getId().isEmpty())
            book.setId(null);
        bookStockService.save(book);
        redirectAttributes.addAttribute("id", book.getId());

        return "redirect:/viewbook";
    }

    @PostMapping("/addcomment")
    public String addComment(@RequestParam("bookid") String bookId,
                             @ModelAttribute("text") String text,
                             RedirectAttributes redirectAttributes) {
        BookStockUserDetails userDetails = (BookStockUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentariesService.add(new Book(bookId), new Commentary(text, new Date(), userDetails.getUser()));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

    @GetMapping("/delcomment")
    @PreAuthorize("hasRole('ROLE_STOCK_KEEPER')")
    public String delComment(@RequestParam("bookid") String bookId,
                             @RequestParam("commentid") String commentId,
                             RedirectAttributes redirectAttributes)  {
        commentariesService.delete(new Book(bookId), new Commentary(commentId));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }


}
