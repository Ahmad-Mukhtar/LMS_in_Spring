package com.LMS.LMS.Classes.BLL.Interfaces;

import com.LMS.LMS.Classes.BLL.BLLClasses.Favourites;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IFavourites
{
    boolean addToFavourites(int bookid,String Username) throws SQLException;

    boolean removeFromFavourites(int bookid,String Username) throws SQLException;

    ArrayList<Favourites> getFavouriteBooks(String Username) throws SQLException;
}
