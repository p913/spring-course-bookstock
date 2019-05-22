package com.pvil.otuscourse.bookstockrestajax.restcontroller.matcher;

import com.pvil.otuscourse.bookstockrestajax.domain.Author;
import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.domain.Commentary;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.Map;

public class BooksStockJsonMatchers {
    public static Matcher<Author> sameAsAuthorsList(List<Author> authors) {
        return new JsonArrayWithPojoListMatcher<>(authors, (a, j) -> isAuthorEqualToJsonObject(a, j));
    };

    public static Matcher<Book> sameAsBooksListWithoutComments(List<Book> books) {
        return new JsonArrayWithPojoListMatcher<>(books, (b, j) -> isBookEqualToJsonObject(b, j));
    };

    public static Matcher<Commentary> sameAsCommentsList(List<Commentary> commentaries) {
        return new JsonArrayWithPojoListMatcher<>(commentaries, (c, j) -> isCommentaryEqualToJsonObject(c, j));
    };

    public static boolean isAuthorEqualToJsonObject(Author author, Object jsonObject) {
        if (jsonObject instanceof Map) {
            Map<String, Object> jsonObjectAsMap = (Map) jsonObject;
            return author.getId().equals(jsonObjectAsMap.get("id"))
                    && author.getName().equals(jsonObjectAsMap.get("name"));
        }
        return false;
    }

    public static boolean isBookEqualToJsonObject(Book book, Object jsonObject) {
        if (jsonObject instanceof Map) {
            Map<String, Object> jsonObjectAsMap = (Map) jsonObject;
            return book.getId().equals(jsonObjectAsMap.get("id"))
                    && book.getTitle().equals(jsonObjectAsMap.get("title"))
                    && isAuthorEqualToJsonObject(book.getAuthor(), jsonObjectAsMap.get("author"))
                    && book.getGenre().equals(jsonObjectAsMap.get("genre"))
                    && book.getIsbn().equals(jsonObjectAsMap.get("isbn"))
                    && book.getPublisher().equals(jsonObjectAsMap.get("publisher"))
                    && book.getYear() == Integer.parseInt(jsonObjectAsMap.get("year").toString());
        }
        return false;
    }

    public static boolean isCommentaryEqualToJsonObject(Commentary commentary, Object jsonObject) {
        if (jsonObject instanceof Map) {
            Map<String, Object> jsonObjectAsMap = (Map) jsonObject;
            return commentary.getId().equals(jsonObjectAsMap.get("id"))
                    && commentary.getReader().equals(jsonObjectAsMap.get("reader"))
                    && commentary.getText().equals(jsonObjectAsMap.get("text"));
        }
        return false;
    }

}
