package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IReserveBooks;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReserveBook
{

    private  int Bookid;

    private String Username;

    private String Reservationdate;

    private String DueDate;

    private ArrayList<ReserveBook> ReservedBooks;

    public ArrayList<ReserveBook> getReservedBooks() {
        return ReservedBooks;
    }

    public void setReservedBooks(ArrayList<ReserveBook> reservedBooks) {
        ReservedBooks = reservedBooks;
    }

    private IReserveBooks reserveBooks;

    public ReserveBook(String username) throws SQLException {

        reserveBooks=DataAccessFactory.getreserveBookDal();

        ReservedBooks=reserveBooks.getReservededBooks(username);

        this.Username=username;
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

    public String getReservationdate() {
        return Reservationdate;
    }

    public void setIssuedate(String reservationdate) {
        Reservationdate = reservationdate;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public ReserveBook(int bookid, String username) throws SQLException {

        this.Bookid=bookid;

        this.Username=username;

        Date currentDate = new Date();

        this.Reservationdate= currentDate.toString();

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, 10);

        this.DueDate=c.getTime().toString();
    }

    public ReserveBook(int bookid, String username,String reservationdate,String Duedate) throws SQLException {

        this.Bookid=bookid;

        this.Username=username;

        this.Reservationdate=reservationdate;

        this.DueDate=Duedate;
    }

    public boolean ReserveABook(int bookid,String username) throws SQLException {

        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, 12);

        return reserveBooks.reserveBook(bookid,username,currentDate.toString(),c.getTime().toString());
    }

    public boolean returnReservedBook(int BOOKID,String USERNAME) throws SQLException {
        if (reserveBooks.removereservedbook(USERNAME,BOOKID))
        {
            for (int i = 0; i <ReservedBooks.size() ; i++)
            {

                if(ReservedBooks.get(i).getBookid()==BOOKID)
                {

                    ReservedBooks.remove(i);
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

    public boolean checkReservedBook(int Bookid)
    {
        for (ReserveBook r:ReservedBooks) {
            if (r.getBookid()==Bookid)
            {
                return true;
            }
        }
        return false;
    }

    public void autoRemoveReservedBook() throws ParseException {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");


        ArrayList<Integer> booksids= new ArrayList<>();

        for (ReserveBook rb:ReservedBooks) {

            calendar.setTime(sdf.parse(rb.getDueDate()));
            int Monthnumber = calendar.get(Calendar.MONTH) + 1;
            String MonthNumber = "0" + Monthnumber;
            String[] duedate = rb.getDueDate().split(" ");
            String duedateofbook = duedate[2] + " " +MonthNumber + " " + duedate[5];
            try {
                Date date1 = myFormat.parse(duedateofbook);
                Date date2 = new Date();
                long diff = date2.getTime() - date1.getTime();
                long value= TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
                if (value>0)
                {

                   if(reserveBooks.removereservedbook(rb.getUsername(),rb.getBookid()))
                    {
                        booksids.add(rb.getBookid());
                    }
                }

            } catch (ParseException | SQLException e) {
                System.out.println(e.toString());
            }

        }

        for (int i = 0; i <booksids.size() ; i++) {
            for (int j = 0; j <ReservedBooks.size() ; j++) {
                if(ReservedBooks.get(j).getBookid()==booksids.get(i))
                {
                    ReservedBooks.remove(j);
                    break;
                }
            }

        }
    }

}
