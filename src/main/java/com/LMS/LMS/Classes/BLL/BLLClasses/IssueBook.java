package com.LMS.LMS.Classes.BLL.BLLClasses;


import com.LMS.LMS.Classes.BLL.Interfaces.IBookissue;

import java.sql.SQLException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class IssueBook
{
 private  int Bookid;

 private String Username;

 private String Issuedate;

 private String DueDate;

 private ArrayList<IssueBook> IssuedBooks;

 public ArrayList<IssueBook> getIssuedBooks() {
        return IssuedBooks;
    }

 private IBookissue bookissue;


    public IssueBook(String usn) throws SQLException {
        bookissue=DataAccessFactory.getIssueBookDal();
        this.Username=usn;
        IssuedBooks=bookissue.getIssuedBooks(usn);
    }

    public int getBookid() {
        return Bookid;
    }

    public void setBookid(int bookid) {
        Bookid = bookid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getIssuedate() {
        return Issuedate;
    }

    public void setIssuedate(String issuedate) {
        Issuedate = issuedate;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public IssueBook(int bookid, String username) throws SQLException {

     this.Bookid=bookid;

     this.Username=username;

     Date currentDate = new Date();

     this.Issuedate= currentDate.toString();

     Calendar c = Calendar.getInstance();

     c.add(Calendar.DATE, 10);

     this.DueDate=c.getTime().toString();
 }

    public IssueBook(int bookid, String username,String IssueDate,String Duedate) throws SQLException {

        this.Bookid=bookid;

        this.Username=username;

        this.Issuedate=IssueDate;

        this.DueDate=Duedate;
    }

    public boolean IssueABook(int bookid,String username) throws SQLException {

     Date currentDate = new Date();

     Calendar c = Calendar.getInstance();

     c.add(Calendar.DATE, 10);

     return bookissue.issueBook(bookid,username,currentDate.toString(),c.getTime().toString());
 }

    boolean RenewBook(int Bookid,String Username,String issuedate,String Duedate) throws SQLException
    {
        return bookissue.RenewBook(Bookid,Username,issuedate,Duedate);
    }

    public boolean ReturnBook(int Bookid,String Username) throws SQLException {
        return bookissue.ReturnBook(Bookid,Username);
    }

    public boolean checkIssuedbook(int bookid)
    {
        for (IssueBook issuedBook : IssuedBooks) {
            if (issuedBook.getBookid() == bookid) {
                return false;
            }
        }
        return true;
    }

    public int getdaysafterdue() throws ParseException {

        int noofdays=0;

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");


        for (IssueBook ib:IssuedBooks) {

            calendar.setTime(sdf.parse(ib.getDueDate()));
            int Monthnumber = calendar.get(Calendar.MONTH) + 1;
            String MonthNumber = "0" + Monthnumber;
            String[] duedate = ib.getDueDate().split(" ");
            String duedateofbook = duedate[2] + " " +MonthNumber + " " + duedate[5];
            try {
                Date date1 = myFormat.parse(duedateofbook);
                Date date2 = new Date();
                long diff = date2.getTime() - date1.getTime();
                long value=TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
                if (value>0)
                {
                    noofdays= (int) (noofdays+value);
                }

            } catch (ParseException e) {
                System.out.println(e.toString());
            }

        }
        return noofdays;
    }


}
