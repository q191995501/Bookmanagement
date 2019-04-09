package liuyuan.bookmanagement.model;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import liuyuan.bookmanagement.mapping.Book;
import liuyuan.bookmanagement.mapping.History;

public class HistoryModel implements IDataBookItem {


    @Override
    public List<Book> getData() {

        List<History> all = LitePal.findAll(History.class);
        List<Book> bookList=new ArrayList<>();

        for (int i=all.size()-1;i>=0;i--){
            History history = all.get(i);

            Book book = new Book();
            HistoryModel.HistoryToBook(history,book);

            bookList.add(book);

        }

        return bookList;
    }


    public static void HistoryToBook(History history,Book book){
        book.setBorrower(history.getBorrower());
        book.setAuthor(history.getAuthor());
        book.setBookname(history.getBookname());
        book.setPrice(history.getPrice());
        book.setPublicationDate(history.getPublicationDate());
        book.setType(history.getType());
        book.setIsbn(history.getIsbn());
    }


    public static void BookToHistory(Book book,History history){
        history.setBorrower(book.getBorrower());
        history.setAuthor(book.getAuthor());
        history.setBookname(book.getBookname());
        history.setPrice(book.getPrice());
        history.setType(book.getType());
        history.setIsbn(book.getIsbn());
    }







}
