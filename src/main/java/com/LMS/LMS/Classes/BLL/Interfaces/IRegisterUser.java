package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;

public interface IRegisterUser
{
     Boolean checkUsername(String Username) throws SQLException;

     Boolean registerUser(String Password,String Dob,String Email,String Firstname,String Lastname,String Username,String Gender) throws SQLException;
}
