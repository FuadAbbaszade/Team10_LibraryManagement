package codes;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public List<Book> filterBooks(List<Book> books, String genre, String authorName, int minPages) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            boolean match = true;

            if (!genre.equals("") && !book.getGenre().equalsIgnoreCase(genre)) {
                match = false;
            }

            if (!authorName.equals("") && !book.getAuthor().getFullName().toLowerCase().contains(authorName.toLowerCase())) {
                match = false;
            }

            if (minPages > 0 && book.getPageCount() < minPages) {
                match = false;
            }

            if (match) {
                result.add(book);
            }
        }

        return result;
    }
}
