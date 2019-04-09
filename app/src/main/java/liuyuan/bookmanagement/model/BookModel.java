package liuyuan.bookmanagement.model;

import org.litepal.LitePal;

import java.util.List;

import liuyuan.bookmanagement.mapping.Book;
import liuyuan.bookmanagement.mapping.History;

public class BookModel {

    /**
     * 获取全部信息
     * @return
     */
    public IDataBookItem getAll(){
        return new IDataBookItem() {
            @Override
            public List<Book> getData() {
                return LitePal.findAll(Book.class);
            }
        };
    }






    /**
     * 查询已经借出的书
     * @return
     */
    public IDataBookItem getloanBooks(){
        return new IDataBookItem() {
            @Override
            public List<Book> getData() {
                return LitePal.where("borrower != ?","").order("publicationDate").find(Book.class);
            }
        };
    }


    public  Book findByIsbn(String isbn){
        List<Book> bookList = LitePal.where("isbn = ?", isbn).find(Book.class);
        return bookList.get(0);
    }



    /**
     * 借书
     * @param id 书id
     * @param name 借书人名
     */
    public boolean setLoanBook(Long id, String name){
        Book book = LitePal.find(Book.class, id);
        if (book==null) return false;

        book.setBorrower(name);

        int update = book.update(id);

        History history = new History();
        HistoryModel.BookToHistory(book,history);

        boolean save = history.save();


        if (update>=1&&save) return true;
        return false;
    }


    /**
     * 还书
     * @param id 书id
     */
    public boolean setStillBook(Long id){
        Book book = LitePal.find(Book.class, id);
        if (book==null) return false;

        book.setBorrower("");

        int update = book.update(id);

        if (update>=1) return true;
        return false;
    }




}
