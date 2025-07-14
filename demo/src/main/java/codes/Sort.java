package codes;

import java.util.*;

public class Sort {

    public void sortBooks(List<Book> list, String key) {
        if (key.equals("title")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
        } else if (key.equals("author")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getAuthor().getFullName().compareTo(b.getAuthor().getFullName());
                }
            });
        } else if (key.equals("publisher")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getPublisher().compareTo(b.getPublisher());
                }
            });
        } else if (key.equals("date")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getPublicationDate().compareTo(b.getPublicationDate());
                }
            });
        } else if (key.equals("pages")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getPageCount() - b.getPageCount();
                }
            });
        } else if (key.equals("genre")) {
            Collections.sort(list, new Comparator<Book>() {
                public int compare(Book a, Book b) {
                    return a.getGenre().compareTo(b.getGenre());
                }
            });
        } else {
            System.out.println("Invalid sort key.");
        }
    }
}