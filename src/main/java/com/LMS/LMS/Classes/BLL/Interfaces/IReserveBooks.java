package com.LMS.LMS.Classes.BLL.Interfaces;

import com.LMS.LMS.Classes.BLL.BLLClasses.ReserveBook;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IReserveBooks
{

    boolean reserveBook(int Bookid,String Username,String Reservationdate,String duedate) throws SQLException;

    ArrayList<ReserveBook> getReservededBooks(String Username) throws SQLException;

    boolean removereservedbook(String username,int bookid) throws SQLException;
}
