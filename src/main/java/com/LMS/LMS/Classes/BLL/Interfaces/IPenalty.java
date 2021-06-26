package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;

public interface IPenalty
{
    int getPenalty(String Username) throws SQLException;

    boolean setPenalty(int Price,String Username) throws SQLException;
}
