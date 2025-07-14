package codes;



import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    List<Book> books = new ArrayList<>();

    public void addBook(Book book){
        books.add(book);
    }
    public void listBooks(){
        System.out.println("total books:" + books.size());
        for (Book book : books){
            System.out.println(book);
        }
    }
    public List<Book> searchBooks(String field, String value){
        List<Book> results = new ArrayList<>();
        for (Book book : books){
            if (field.equalsIgnoreCase("title")){
                if (book.getTitle().equalsIgnoreCase(value)){
                    results.add(book);
                }
            }
            if (field.equalsIgnoreCase("genre")){
                if (book.getGenre().equalsIgnoreCase(value)){
                    results.add(book);
                }
            }
            if (field.equalsIgnoreCase("publisher")){
                if (book.getPublisher().equalsIgnoreCase(value)){
                    results.add(book);
                }
            }
            if (field.equalsIgnoreCase("date")){
                if (book.getPublicationDate().equalsIgnoreCase(value)){
                    results.add(book);
                }
            }
            if (field.equalsIgnoreCase("author")){
                if (book.getAuthor().getFullName().equalsIgnoreCase(value)){
                    results.add(book);
                }
            }
            if (field.equalsIgnoreCase("pagecount")){
                if (Integer.toString(book.getPageCount()).equals(value)){
                    results.add(book);
                }
            }

        }
        return results;

    }

    public void deleteBook(Book book){
        books.remove(book);
    }

    public void updateBook(Book book, Scanner sc) {
        System.out.println("Updating book: " + book.getTitle());

        System.out.print("New Title (leave blank to keep current): ");
        String title = sc.nextLine();
        if (!title.isEmpty()) book.setTitle(title);

        System.out.print("New Genre (leave blank to keep current): ");
        String genre = sc.nextLine();
        if (!genre.isEmpty()) book.setGenre(genre);

        System.out.print("New Publisher (leave blank to keep current): ");
        String publisher = sc.nextLine();
        if (!publisher.isEmpty()) book.setPublisher(publisher);

        System.out.print("New Publication Date (leave blank to keep current): ");
        String date = sc.nextLine();
        if (!date.isEmpty()) book.setPublicationDate(date);

        System.out.print("New Pagecount (leave blank to keep current): ");
        String pageCountInput = sc.nextLine();
        if (!pageCountInput.isEmpty()) {
            try {
                int pageCount = Integer.parseInt(pageCountInput);
                book.setPageCount(pageCount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, keeping current page count.");
            }
        }

        System.out.print("Update Author Information? (y/n): ");
        String updateAuthor = sc.nextLine();
        if (updateAuthor.equalsIgnoreCase("y")) {
            System.out.print("New Author Name: ");
            String authorName = sc.nextLine();
            System.out.print("New Author Nationality: ");
            String nationality = sc.nextLine();

            book.getAuthor().setFullname(authorName);
            book.getAuthor().setNationality(nationality);
        }
    }
    public List<Book> getBooks(){
        return books;
    }
    public void setBooks(){
        this.books = books;
    }
}
