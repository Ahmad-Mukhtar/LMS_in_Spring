package com.LMS.LMS.Classes.BLL.BLLClasses;


import com.LMS.LMS.Classes.BLL.Interfaces.ILogin;

import java.sql.SQLException;

public class Login
{
    private ILogin login;

    private String username;

    private String password;

    //Getter Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login() throws SQLException
    {
        login=DataAccessFactory.getLogindal();
    }
    //Validate from Database if The Credentials are Correct and then Login
    public boolean Login(String Username, String Password,String usertype) throws SQLException {

        return login.validateLogin(Username,Password,usertype);

    }

}
