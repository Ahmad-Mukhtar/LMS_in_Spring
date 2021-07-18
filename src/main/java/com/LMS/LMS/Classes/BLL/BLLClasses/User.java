package com.LMS.LMS.Classes.BLL.BLLClasses;


import com.LMS.LMS.Classes.BLL.Interfaces.IUserinfo;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;


public class User
{
    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    private String Username;

    private String Firstname;

    private String Lastname;

    private String Password;

    private String Email;

    private String Dob;

    private int Noofbooksissued;

    private int NoofbooksReserved;

    private UpdateProfile updateProfile;

    private Books books;

    private IssueBook issueBook;

    private ReserveBook Reservebook;

    private History history;

    private Favourites favourites;

    private Penalty penalty;

    //get all the Info about User profile, Issued Books etc
    public User(String username) throws SQLException, ParseException {

        try {

            this.Username = username;

            updateProfile = new UpdateProfile();

            issueBook = new IssueBook(username);

            Reservebook = new ReserveBook(username);

            //Check if Days Exceed after Due then Remove the Reserved Book
            Reservebook.autoRemoveReservedBook();

            history = new History(username);

            favourites = new Favourites(username);

            books = new Books(username);

            penalty = new Penalty(username);

            //Check if Days Exceed after Due then Set the Penalty
            int Noofdays = issueBook.getdaysafterdue();

            if (Noofdays > 0) {

                penalty.SetPenalty(Noofdays, Username);
            }

            IUserinfo userinfo = DataAccessFactory.getUserinfoDal();

            ArrayList<String> Userinfo = userinfo.setUserinfo(username);

            Firstname = Userinfo.get(0);

            Lastname = Userinfo.get(1);

            Dob = Userinfo.get(2);

            Password = Userinfo.get(3);

            Email = Userinfo.get(4);

            Noofbooksissued = Integer.parseInt(Userinfo.get(5));

            NoofbooksReserved = Integer.parseInt(Userinfo.get(6));
        }
        catch (Exception exception)
        {
            System.out.println(exception.toString());
        }
    }

    //Getters ad Setters
    public void setUsername(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    //Get all the Books
    public ArrayList<Books> getBooksArrayList() {
        return books.getBooksArrayList() ;
    }

    //Get Only The Reserved Books
    public ArrayList<ReserveBook> getReservedBooks() {
        return Reservebook.getReservedBooks();
    }

    //Get Only The Books in history
    public ArrayList<History> getHistoryBooks() {
        return history.getHistoryBooks();
    }

    //get all the Books Wich are in favourites
    public ArrayList<Favourites> getFavouriteBooks() { return favourites.getFavouriteBooks(); }

    //Sort All the Books By Ascending Order
    public ArrayList<Books> sortByAsc() {

        ArrayList<Books> Sortbooks=new ArrayList<>();

        for (Books b:books.getBooksArrayList()) {
            Books books=new Books(b);
            Sortbooks.add(books);
        }

        Sortbooks.sort(Comparator.comparing(Books::getBookname));

        return Sortbooks;
    }

    //get all the Issued Books
    public ArrayList<IssueBook> getIssuedBooks() {
        return issueBook.getIssuedBooks();
    }

    //Sort All the Books By descending Order
    public ArrayList<Books> sortBydsc() {
        ArrayList<Books> Sortbooks=new ArrayList<>();

        for (Books b:books.getBooksArrayList()) {
            Books books=new Books(b);
            Sortbooks.add(books);
        }

        Sortbooks.sort(Comparator.comparing(Books::getBookname).reversed());

        return Sortbooks;
    }

    //Search The Required Book
    public ArrayList<Books> searchBooks(String Searchvalue)
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

    //Serach The Book based on filter Value
    public ArrayList<Books> filterbyGenre(String Filtervalue) {
        ArrayList<Books> SearchResults=new ArrayList<>();

        for (Books b:books.getBooksArrayList()) {
            Books books=new Books(b);
            if (books.getGenre().equals(Filtervalue))
            {
                SearchResults.add(books);
            }

        }
        return SearchResults;
    }

    //Update The user profile Only Update the Fields Which the User Entered Empty Fields Value
    //will be Same as previous
    public Boolean updateProfile(String FirstName, String LastName, String PassWord, String EmaiL, String DOB) throws SQLException {

        try {

            if (FirstName.isEmpty()) {
                FirstName = Firstname;
            }
            if (LastName.isEmpty()) {
                LastName = Lastname;
            }

            if (PassWord.isEmpty()) {
                PassWord = Password;
            }

            if (DOB.isEmpty()) {
                DOB = Dob;
            }

            if (EmaiL.isEmpty()) {
                EmaiL = Email;
            }

            Boolean status = updateProfile.updateProfile(FirstName, LastName, PassWord, EmaiL, DOB, Username);

            if (status) {
                Firstname = FirstName;

                Lastname = LastName;

                Password = PassWord;

                Email = EmaiL;

                Dob = DOB;
            }
            return status;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;
    }

    //Issue A book if not issued and no of already Issued Books is less than 3
    public int issueABook(int bookid) throws SQLException {

        try {

            //if noof book >3
            if (Noofbooksissued < 3) {

                //Check if the Book Exists
                if (books.checkbook(bookid)) {


                    //Check if stock is Enough
                    if (books.getBookStock(bookid)) {
                        //If book is Already Issued
                        for (IssueBook issueBook : issueBook.getIssuedBooks()) {
                            if (issueBook.getBookid() == bookid && issueBook.getUsername().equals(Username)) {
                                return 0;
                            }
                        }
                        //Issued the Book and if Reserved than Remove it from Reserved Books
                        if (issueBook.IssueABook(bookid, Username)) {
                            IssueBook issueBook1 = new IssueBook(bookid, Username);

                            issueBook.getIssuedBooks().add(issueBook1);

                            Noofbooksissued = Noofbooksissued + 1;

                            if (Reservebook.checkReservedBook(bookid)) {
                                NoofbooksReserved = NoofbooksReserved - 1;

                                Reservebook.returnReservedBook(bookid, Username);
                            } else {

                                //If Book is not Reserved than Decrese the Stock by 1
                                for (int i = 0; i < books.getBooksArrayList().size(); i++) {
                                    if (books.getBooksArrayList().get(i).getId() == bookid) {
                                        books.getBooksArrayList().get(i).setCurrentStock(books.getBooksArrayList().get(i).getCurrentStock() - 1);

                                        break;
                                    }
                                }
                            }

                            return 1;

                        } else {
                            return 2;
                        }
                    }
                    //If the Book was already Reserved and Stock was 0 than Issue it
                    else if (Reservebook.checkReservedBook(bookid)) {

                        for (IssueBook issueBook : issueBook.getIssuedBooks()) {
                            if (issueBook.getBookid() == bookid && issueBook.getUsername().equals(Username)) {
                                return 0;
                            }
                        }
                        if (issueBook.IssueABook(bookid, Username)) {
                            IssueBook issueBook1 = new IssueBook(bookid, Username);

                            issueBook.getIssuedBooks().add(issueBook1);

                            Noofbooksissued = Noofbooksissued + 1;

                            NoofbooksReserved = NoofbooksReserved - 1;

                            Reservebook.returnReservedBook(bookid, Username);

                            return 1;

                        } else {
                            return 2;
                        }
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }

            } else {
                return 4;
            }

        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return 2;
    }

    //Reserve A Book if not issued and no of already reserved Books is less than 3
    public int reserveABook(int bookid) throws SQLException {
        try {

            //if NOofReserved Books is Less than 3
            if (NoofbooksReserved < 3) {

                //If Book exists
                if (books.checkbook(bookid)) {

                    //If Book is ALready Issued than Don't Reserve it
                    if (issueBook.checkIssuedbook(bookid)) {
                        //Check if it is in Stock
                        if (books.getBookStock(bookid)) {
                            for (ReserveBook reserveBook : Reservebook.getReservedBooks()) {
                                if (reserveBook.getBookid() == bookid && reserveBook.getUsername().equals(Username)) {
                                    return 0;
                                }
                            }
                            //Reserve the Book
                            if (Reservebook.ReserveABook(bookid, Username)) {

                                ReserveBook reserveBook = new ReserveBook(bookid, Username);

                                Reservebook.getReservedBooks().add(reserveBook);

                                NoofbooksReserved = NoofbooksReserved + 1;

                                for (int i = 0; i < books.getBooksArrayList().size(); i++) {
                                    if (books.getBooksArrayList().get(i).getId() == bookid) {
                                        books.getBooksArrayList().get(i).setCurrentStock(books.getBooksArrayList().get(i).getCurrentStock() - 1);

                                        break;
                                    }
                                }

                                return 1;

                            } else {
                                return 2;
                            }
                        } else {
                            return 5;
                        }
                    } else {
                        return 6;
                    }
                } else {
                    return 3;
                }

            } else {
                return 4;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return 2;
    }

    //Return The Book and Add it into History Table
    public boolean returnABook(int bookid) {

        try {

            if (!issueBook.checkIssuedbook(bookid)) {

                if (issueBook.ReturnBook(bookid, Username)) {

                    Noofbooksissued = Noofbooksissued - 1;
                    for (int i = 0; i < issueBook.getIssuedBooks().size(); i++) {
                        if (issueBook.getIssuedBooks().get(i).getBookid() == bookid) {

                            issueBook.getIssuedBooks().remove(i);
                            break;
                        }
                    }

                    for (int i = 0; i < books.getBooksArrayList().size(); i++) {
                        if (books.getBooksArrayList().get(i).getId() == bookid) {
                            books.getBooksArrayList().get(i).setCurrentStock(books.getBooksArrayList().get(i).getCurrentStock() + 1);

                            break;
                        }
                    }


                    for (History history : history.getHistoryBooks()) {
                        if (history.getBookid() == bookid) {
                            return true;
                        }
                    }


                    history.AddtoHistory(bookid, Username);

                    History h = new History(bookid, Username);

                    history.getHistoryBooks().add(h);

                    return true;
                } else {
                    return false;
                }


            } else {
                return false;
            }
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;
    }

    //Renew the Issued Book By increasing Six More days in due date
    public int renewBook(int Bookid) throws SQLException, ParseException {

        try {

            for (IssueBook issuedBook : issueBook.getIssuedBooks()) {

                if (issuedBook.getBookid() == Bookid) {
                    Date currentDate = new Date();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                    c.setTime(sdf.parse(issuedBook.getDueDate()));

                    c.add(Calendar.DATE, 6);

                    if (issueBook.RenewBook(Bookid, Username, currentDate.toString(), c.getTime().toString())) {
                        issuedBook.setDueDate(c.getTime().toString());
                        return 0;
                    } else {
                        return 1;
                    }

                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return 2;
    }

    //Add the Book in Favourites
    public boolean AddtoFavourites(int Bookid) throws SQLException {

        try {

            return  favourites.addtoFavouries(Bookid,Username);
        }
        catch (Exception exception)
        {
            System.out.println(exception.toString());
        }

    return false;
    }

    //Remove the Book From favourites
    public boolean RemoveFromFavourites(int Bookid) throws SQLException {

        try {
            return favourites.removeFromFavourites(Bookid,Username);
        }
        catch (Exception exception)
        {
            System.out.println(exception.toString());
        }
        return  false;
    }

    //Check if the Book is Favourited
    public boolean checkFavouriteBook(int bookid)
    {
        return favourites.checkFavouriteBook(bookid);
    }
    //Get a Particular Book
    public Books getbook(int bookid)
    {
        return books.getbook(bookid);
    }

    //Get the Penalty price
    public int getPenaltyPrice()
    {
        return penalty.getPrice();
    }

}
