package liuyuan.bookmanagement.mapping;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Book extends LitePalSupport {

    @Column(unique = true)
    private String isbn;
    private String bookname;
    private String type;
    private String author;
    private double price;
    private String borrower;
    private String publicationDate;


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public  long getId(){
        return getBaseObjId();
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }



    @Override
    public String toString() {
        return "Book{" +
                "id='"+getId()+'\''+
                "bookname='" + bookname + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", borrower='" + borrower + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                '}';
    }
}
