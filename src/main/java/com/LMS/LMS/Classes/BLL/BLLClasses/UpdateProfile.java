package com.LMS.LMS.Classes.BLL.BLLClasses;


import com.LMS.LMS.Classes.BLL.Interfaces.IUpdateProfile;

import java.sql.SQLException;

public class UpdateProfile
{
    private IUpdateProfile updateProfile;

    private String FirstName;
    private String LastName;

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    private String PassWord;
    private String Email;
    private String Dob;

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public String getEmail() {
        return Email;
    }

    public String getDob() {
        return Dob;
    }

    public UpdateProfile() throws SQLException {
        updateProfile=DataAccessFactory.getProfileUpdateDal();
    }

    public Boolean updateProfile(String Firstname, String Lastname, String Password, String Email, String Dob,String username) throws SQLException {

        return updateProfile.updateProfile(Firstname,Lastname,Password,Email,Dob,username);
    }
}
