package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IRegisterUser;
import org.assertj.core.internal.Classes;

import java.sql.SQLException;

public class Register
{
    String FirstName;
    String LastName;
    String Username;
    String Password;
    String Email;
    String DOB;
    String Gender;

    private IRegisterUser IRegisterUser;

    public Register() throws SQLException {
        IRegisterUser =DataAccessFactory.getRegisterDal();
    }

    public Boolean registerUser(String Password,String Dob,String Email,String Firstname,String Lastname,String Username,String Gender) throws SQLException {
        return  IRegisterUser.registerUser(Password,Dob,Email,Firstname,Lastname,Username,Gender);
    }

    public Boolean checkusername(String Username) throws SQLException
    {
        return  IRegisterUser.checkUsername(Username);
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

}
