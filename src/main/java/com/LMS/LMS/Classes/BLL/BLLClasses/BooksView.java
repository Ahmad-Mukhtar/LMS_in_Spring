package com.LMS.LMS.Classes.BLL.BLLClasses;

public class BooksView
{
    String Bookimglink;
    String Duedate;
    String Bookid;



    public String getBookid() {
        return Bookid;
    }

    public void setBookid(String bookid) {
        Bookid = bookid;
    }

    public String getBookimglink() {
        return Bookimglink;
    }

    public void setBookimglink(String bookimglink) {
        Bookimglink = bookimglink;
    }

    public String getDuedate() {
        return Duedate;
    }

    public void setDuedate(String duedate) {
        Duedate = "Due Date: "+duedate;
    }
}
