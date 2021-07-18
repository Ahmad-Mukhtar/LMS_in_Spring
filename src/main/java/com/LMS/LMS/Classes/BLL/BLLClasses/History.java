package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IHistory;

import java.sql.SQLException;
import java.util.ArrayList;

public class History
{
    private int bookid;

    private String Username;

    private ArrayList<History> HistoryBooks;

    private IHistory iHistory;

    //Get All the Hisory Books
    public History(String username) throws SQLException
    {
        iHistory=DataAccessFactory.getHistoryBookDal();
        this.Username=username;
        HistoryBooks=iHistory.getHistoryBooks(username);

    }
    public History(int bid,String un)
    {
        this.bookid=bid;

        this.Username=un;
    }

    //Setters and Getters

    public ArrayList<History> getHistoryBooks() {
        return HistoryBooks;
    }

    public void setHistoryBooks(ArrayList<History> historyBooks) {
        HistoryBooks = historyBooks;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    //Add the Book to History which was Issued and Returned
    public boolean AddtoHistory(int Bookid,String Username) throws SQLException {

        return iHistory.AddtoHistory(Bookid,Username);
    }
}
