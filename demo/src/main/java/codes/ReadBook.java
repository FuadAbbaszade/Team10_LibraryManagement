package codes;

import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 
public class ReadBook{
   

    public void loadBooks(String filepath,Library library){
        List<Book> books=new ArrayList<>();

        try{
            File file=new File(filepath);
            Scanner read=new Scanner(file);
            while(read.hasNextLine()){
                String database=read.nextLine();
                String[] parts=database.split(",");

                String title = parts[0].trim();
                String authorFullName = parts[1].trim();
                String authorNationality = parts[2].trim();
                String publisher = parts[3].trim();
                String publicationDate = parts[4].trim();
                int pageCount = Integer.parseInt(parts[5].trim());
                String genre = parts[6].trim();
                Author author = new Author(authorFullName, authorNationality);
                Book book = new Book(title, author, publisher, publicationDate, pageCount, genre);
                library.addBook(book);
            }
            read.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Error Occured which is: ");
            e.printStackTrace();
        }

    }


    public static void saveBook(List<Book> books, String filepath){
        try{
            
            FileWriter writer= new FileWriter(filepath,false);

            for(Book book: books){
                String line=String.join(",",
                book.getTitle(),
                book.getAuthor().getFullName(),
                book.getAuthor().getNationality(),
                book.getPublisher(),
                book.getPublicationDate(),
                String.valueOf(book.getPageCount()),
                book.getGenre()
                );
                writer.write(line+"\n");
            }
        writer.close();
        }

        catch(IOException e){
            System.out.println("Error occured");
            e.printStackTrace();
        }
    }

    
}