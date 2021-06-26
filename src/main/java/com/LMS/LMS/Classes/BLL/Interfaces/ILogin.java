package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;

public interface ILogin
{
    boolean validateLogin(String Username,String Password,String usertype) throws SQLException;
}
