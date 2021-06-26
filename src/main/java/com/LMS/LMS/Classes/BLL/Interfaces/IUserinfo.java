package com.LMS.LMS.Classes.BLL.Interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IUserinfo
{
    ArrayList<String>setUserinfo(String Username) throws SQLException;
}
