package com.LMS.LMS.Classes.BLL.Interfaces;

import com.LMS.LMS.Classes.BLL.BLLClasses.Books;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IBook
{

    ArrayList<Books> getBooks() throws SQLException;
}
