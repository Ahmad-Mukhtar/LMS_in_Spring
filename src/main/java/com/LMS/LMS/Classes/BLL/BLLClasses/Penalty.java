package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IPenalty;

import java.sql.SQLException;

public class Penalty
{
    private int price;

    private  String username;

    private IPenalty iPenalty;

    //Get the Penalty Value From DataBase
    public Penalty(String Username) throws SQLException {
        iPenalty=DataAccessFactory.getPenaltydal();
        this.price=iPenalty.getPenalty(Username);
    }

    //Getters and Setters
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //Calculate the Price For Penlaty ie 10 RS per day and Store it in db
    public boolean SetPenalty(int noofdays,String Username) throws SQLException {

            int penanltyprice = noofdays * 10;
            if(this.price<penanltyprice) {
                this.setPrice(penanltyprice);
                return iPenalty.setPenalty(penanltyprice, Username);
            }
            return true;

    }

}
