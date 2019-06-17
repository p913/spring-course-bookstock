package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockUserDetails;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.CommentariesService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.GenresService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
public class BookStockController {
    private final BookStockService bookStockService;

    private final AuthorsService authorsService;

    private final GenresService genresService;

    private final CommentariesService commentariesService;

    public BookStockController(BookStockService bookStockService, AuthorsService authorsService,
                               GenresService genresService, CommentariesService commentariesService) {
        this.bookStockService = bookStockService;
        this.authorsService = authorsService;
        this.genresService = genresService;
        this.commentariesService = commentariesService;
    }

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute("books", bookStockService.getAllBooks());
        return "bookstock";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchtext") String searchText, Model model) {
        model.addAttribute("books", bookStockService.searchBooks(searchText));
        model.addAttribute("searchtext", searchText);
        return "bookstock";
    }

    @GetMapping("/viewbook")
    public String getBookByIdWithComments(@RequestParam("id") long bookId, Model model) {
        model.addAttribute("book", bookStockService.getBookById(bookId).orElse(new Book()));
        model.addAttribute("comments", commentariesService.getAllForBook(new Book(bookId)));
        return "viewbook";
    }

    @GetMapping("/editbook")
    public String editBook(@RequestParam(value = "id", required = false) String bookId, Model model) {
        if (bookId != null)
            model.addAttribute("book", bookStockService.getBookById(Long.valueOf(bookId)).orElse(new Book()));
        else
            model.addAttribute("book", new Book());
        model.addAttribute("authors", authorsService.getAllAuthors());
        model.addAttribute("genres", genresService.getAllGenres());
        return "editbook";
    }

    @GetMapping("/delbook")
    public String delBook(@RequestParam(value = "id") long bookId) {
        bookStockService.deleteBook(new Book(bookId));
        return "redirect:/";
    }

    @PostMapping("/savebook")
    public String save(@Valid Book book,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "editbook";
        }

        if (book.getId() == 0)
            bookStockService.addBook(book);
        else
            bookStockService.updateBook(book);

        redirectAttributes.addAttribute("id", book.getId());

        return "redirect:/viewbook";
    }

    @PostMapping("/addcomment")
    public String addComment(@RequestParam("bookid") long bookId,
                             @ModelAttribute("text") String text,
                             RedirectAttributes redirectAttributes) {
        BookStockUserDetails userDetails = (BookStockUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Book> book = bookStockService.getBookById(bookId);
        if (book.isEmpty())
            throw new EntityNotFoundException(String.format("Book with id=%d not found for add comment", bookId));
        commentariesService.addCommentary(new Commentary(text, OffsetDateTime.now(), userDetails.getUser(), book.get()));

        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

    @GetMapping("/delcomment")
    public String delComment(@RequestParam("bookid") long bookId,
                             @RequestParam("commentid") long commentId,
                             RedirectAttributes redirectAttributes)  {
        commentariesService.deleteCommentary(new Commentary(commentId));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }


}
