package com.LMS.LMS.Classes.BLL.Interfaces;

import com.LMS.LMS.Classes.BLL.BLLClasses.IssueBook;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IBookissue
{
   boolean issueBook(int Bookid,String Username,String issuedate,String duedate) throws SQLException;
   boolean RenewBook(int Bookid,String Username,String issuedate,String Duedate) throws SQLException;

   boolean ReturnBook(int Bookid,String Username) throws SQLException;

   ArrayList<IssueBook>  getIssuedBooks(String Username) throws SQLException;

}
