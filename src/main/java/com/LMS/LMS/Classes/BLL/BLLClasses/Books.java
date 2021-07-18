package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IBook;

import java.sql.SQLException;
import java.util.ArrayList;

public class Books
{

    private int id;

    private String Bookname;

    private String Genre;

    private String BookDescription;

    private int CurrentStock;

    private String BookImageLink;

    private String Authorname;

    private String Publishername;

    private IBook books;


    private ArrayList<Books> booksArrayList;

    public Books() {

    }

    //Get all the Books From database and store it in List
    public Books(String username) throws SQLException {

        try {
            books=DataAccessFactory.getBookDal();

            this.booksArrayList=books.getBooks();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }


    }

    //Copy Constructor
    public Books(Books books)
    {
        this.id=books.id;
        this.Bookname=books.Bookname;
        this.Genre=books.Genre;
        this.BookDescription=books.Genre;
        this.CurrentStock=books.CurrentStock;
        this.BookImageLink=books.BookImageLink;
        this.Authorname=books.Authorname;
        this.Publishername=books.Publishername;
    }

    public ArrayList<Books> getBooksArrayList() {
        return booksArrayList;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBookname(String bookname) {
        Bookname = bookname;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public void setBookDescription(String bookDescription) {
        BookDescription = bookDescription;
    }

    public void setCurrentStock(int currentStock) {
        CurrentStock = currentStock;
    }

    public void setBookImageLink(String bookImageLink) {
        BookImageLink = bookImageLink;
    }

    public void setAuthorname(String authorname) {
        Authorname = authorname;
    }

    public void setPublishername(String publishername) {
        Publishername = publishername;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getBookname() {
        return Bookname;
    }

    public String getGenre() {
        return Genre;
    }

    public String getBookDescription() {
        return BookDescription;
    }

    public int getCurrentStock() {
        return CurrentStock;
    }

    public String getBookImageLink() {
        return BookImageLink;
    }

    public String getAuthorname() {
        return Authorname;
    }

    public String getPublishername() {
        return Publishername;
    }

    //Get Speacific Book Stock
    public boolean getBookStock(int bookid)
    {
        for (Books books :booksArrayList ) {
            if (books.getId() == bookid) {
                return books.getCurrentStock() > 0;
            }
        }

        return true;
    }

    //Check if Book id is valid
    public boolean checkbook(int bookid)
    {
        for (int i=0;i<booksArrayList.size();i++)
        {
            if(booksArrayList.get(i).getId()==bookid)
            {
                return true;
            }
        }
        return false;
    }

    //Get a single book
    public Books getbook(int bookid)
    {
        for (int i=0;i<booksArrayList.size();i++)
        {
            if(booksArrayList.get(i).getId()==bookid)
            {
                return booksArrayList.get(i);
            }
        }
        return null;
    }


}
