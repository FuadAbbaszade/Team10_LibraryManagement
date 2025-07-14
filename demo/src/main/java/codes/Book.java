package codes;

public class Book {
    private String title;
    private Author author;  
    private String publisher;
    private String publicationDate;
    private int pageCount;    
    private String genre;

    public Book(String title, Author author, String publisher, String publicationDate, int pageCount, String genre) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.genre = genre;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Title: " + title +
               ", Author: " + author.getFullName() +
               ", Nationality: " + author.getNationality() +
               ", Publisher: " + publisher +
               ", Publication Date: " + publicationDate +
               ", Page Count: " + pageCount +
               ", Genre: " + genre;
    }
}