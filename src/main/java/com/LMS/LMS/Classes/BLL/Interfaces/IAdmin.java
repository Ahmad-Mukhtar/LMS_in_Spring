package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;

public interface IAdmin
{
    boolean changePassword(String Username,String Password) throws SQLException;
    boolean addBook(int Bookid,String Bookname,String BookDescription,String Bookimglink,String Genre,int CurrentStock,String Authorname,String Publishername) throws SQLException;
    boolean removeBook(int Bookid) throws SQLException;
    boolean updateBook(int Bookid,String Bookname,String BookDescription,String Bookimglink,String Genre,int CurrentStock,String Authorname,String Publishername) throws SQLException;
}
