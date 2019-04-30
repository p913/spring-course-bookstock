package com.pvil.otuscourse.bookstockmvcclassic.webcontroller;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcclassic.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcclassic.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcclassic.service.CommentariesService;
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
    public String editBook(@RequestParam(value = "id", required = false) String bookId, Model model) {
        if (bookId != null)
            model.addAttribute("book", bookStockService.getById(bookId).orElse(new Book()));
        else
            model.addAttribute("book", new Book());
        model.addAttribute("authors", authorsService.getAll());
        return "editbook";
    }

    @GetMapping("/delbook")
    public String delBook(@RequestParam(value = "id") String bookId) {
        bookStockService.delete(new Book(bookId));
        return "bookstock";
    }

    @PostMapping("/savebook")
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
                             @ModelAttribute("reader") String reader,
                             @ModelAttribute("text") String text,
                             RedirectAttributes redirectAttributes) {
        commentariesService.add(new Book(bookId), new Commentary(text, new Date(), reader));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

    @GetMapping("/delcomment")
    public String delComment(@RequestParam("bookid") String bookId,
                             @RequestParam("commentid") String commentId,
                             RedirectAttributes redirectAttributes) {
        commentariesService.delete(new Book(bookId), new Commentary(commentId));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

}
