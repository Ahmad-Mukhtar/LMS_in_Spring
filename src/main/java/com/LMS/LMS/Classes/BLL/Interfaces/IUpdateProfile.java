package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;

public interface IUpdateProfile
{
    Boolean updateProfile(String Firstname,String Lastname,String Password,String Email,String Dob,String username) throws SQLException;
}
