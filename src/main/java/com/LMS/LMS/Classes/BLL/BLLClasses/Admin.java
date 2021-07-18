package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IAdmin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class Admin
{

    private  String Adminname;


    private IAdmin iAdmin;

    private Books books;


    //Constuctor get all the info Related to admin
    public Admin(String adminname) throws SQLException
    {
        iAdmin=DataAccessFactory.getAdmindal();

        this.Adminname=adminname;

        books=new Books(adminname);
    }

    public String getAdminname() {
        return Adminname;
    }

    public void setAdminname(String adminname) {
        Adminname = adminname;
    }

    //Get all the Books
    public ArrayList<Books> getAllBooks()
    {
        return books.getBooksArrayList();
    }

    //Search The Required Book
    public ArrayList<Books> getsearchResults(String Searchvalue)
    {
        ArrayList<Books> SearchResults=new ArrayList<>();

        for (Books b:books.getBooksArrayList()) {
            Books books=new Books(b);
            if (books.getBookname().contains(Searchvalue))
            {
                SearchResults.add(books);
            }

        }
        return SearchResults;
    }

    //Update Profile
    public boolean UpdateProfile(String Password) throws SQLException {
        return iAdmin.changePassword(Adminname,Password);
    }
    //Add Book
    public boolean AddBooks(int Bookid,String Bookname,String BookDescription,String Bookimglink,String Genre,int CurrentStock,String Authorname,String Publishername) throws SQLException {

        //If Book id is Unque then add it
        if(!books.checkbook(Bookid)) {

             if (iAdmin.addBook(Bookid, Bookname, BookDescription, Bookimglink, Genre, CurrentStock, Authorname, Publishername)) {
                 Books b = new Books();
                 b.setId(Bookid);
                 b.setBookname(Bookname);
                 b.setBookDescription(BookDescription);
                 b.setBookImageLink(Bookimglink);
                 b.setCurrentStock(CurrentStock);
                 b.setAuthorname(Authorname);
                 b.setPublishername(Publishername);
                 b.setGenre(Genre);
                 books.getBooksArrayList().add(b);
                 return true;
             } else {
                 return false;
             }
         }
         else
         {
             return false;
         }
    }
    //Delete the  Book
    public boolean DeleteBook(int Bookid) throws SQLException {
        if(books.checkbook(Bookid))
        {
            //Remove the Book and Delete its image file
            if (iAdmin.removeBook(Bookid))
            {
                for (int i = 0; i < books.getBooksArrayList().size(); i++) {
                    if (books.getBooksArrayList().get(i).getId() == Bookid) {

                        File F = new File("src/main/webapp/"+books.getBooksArrayList().get(i).getBookImageLink());
                        if(F.exists())
                        {
                            F.delete();
                        }
                        books.getBooksArrayList().remove(i);
                        break;
                    }
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    //Update  a Book
    public boolean UpdateBooks(int Bookid,String Bookname,String BookDescription,String Bookimglink,String Genre,int CurrentStock,String Authorname,String Publishername) throws SQLException {
        //if Book Exists
        if(books.checkbook(Bookid)) {

            Books b=books.getbook(Bookid);
            //set Current Valus to the Previous values Which are empty
            if (Bookname.isEmpty())
            {
                Bookname=b.getBookname();
            }

            if (BookDescription.isEmpty())
            {
                BookDescription=b.getBookDescription();
            }
            if (Bookimglink.isEmpty())
            {
                Bookimglink=b.getBookImageLink();
            }
            if (Genre.isEmpty())
            {
                Genre=b.getGenre();
            }
            if (CurrentStock==0)
            {
                CurrentStock=b.getCurrentStock();
            }
            if (Authorname.isEmpty())
            {
                Authorname=b.getAuthorname();
            }
            if (Publishername.isEmpty())
            {
                Publishername=b.getPublishername();
            }
            if (iAdmin.updateBook(Bookid, Bookname, BookDescription, Bookimglink, Genre, CurrentStock, Authorname, Publishername)) {
                for (int i = 0; i <books.getBooksArrayList().size() ; i++)
                {
                    if (books.getBooksArrayList().get(i).getId()==Bookid)
                    {
                        books.getBooksArrayList().get(i).setCurrentStock(CurrentStock);
                        books.getBooksArrayList().get(i).setBookname(Bookname);
                        books.getBooksArrayList().get(i).setBookImageLink(Bookimglink);
                        books.getBooksArrayList().get(i).setBookDescription(BookDescription);
                        books.getBooksArrayList().get(i).setGenre(Genre);
                        books.getBooksArrayList().get(i).setAuthorname(Authorname);
                        books.getBooksArrayList().get(i).setPublishername(Publishername);
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    //Get a Single Book By its Id
    public Books getBook(int Bookid)
    {
        return books.getbook(Bookid);
    }
}
