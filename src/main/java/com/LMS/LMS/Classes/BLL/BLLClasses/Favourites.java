package com.LMS.LMS.Classes.BLL.BLLClasses;

import com.LMS.LMS.Classes.BLL.Interfaces.IFavourites;

import java.sql.SQLException;
import java.util.ArrayList;

public class Favourites
{
    private  int Bookid;

    private String Username;

    private ArrayList<Favourites> FavouriteBooks;


    private IFavourites favourites;

    public Favourites(String username) throws SQLException {

        favourites=DataAccessFactory.getFavouritesDal();
        this.Username=username;
        FavouriteBooks=favourites.getFavouriteBooks(username);
    }

    public Favourites(int bookid,String UserName) throws SQLException {
        favourites=DataAccessFactory.getFavouritesDal();
        this.Username=UserName;
        this.Bookid=bookid;
    }

    public ArrayList<Favourites> getFavouriteBooks() {
        return FavouriteBooks;
    }

    public void setFavouriteBooks(ArrayList<Favourites> favouriteBooks) {
        FavouriteBooks = favouriteBooks;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getBookid() {
        return Bookid;
    }

    public void setBookid(int bookid) {
        Bookid = bookid;
    }

    boolean addtoFavouries(int Bookid,String Username) throws SQLException {


        FavouriteBooks.add(new Favourites(Bookid,Username));
        return favourites.addToFavourites(Bookid,Username);
    }

    boolean removeFromFavourites(int bookid,String UserName) throws SQLException
    {

        for (int i = 0; i <FavouriteBooks.size() ; i++)
        {

            if(FavouriteBooks.get(i).getBookid()==bookid)
            {

                FavouriteBooks.remove(i);
                break;
            }

        }
        return favourites.removeFromFavourites(bookid,UserName);
    }

    public boolean checkFavouriteBook(int bookid)
    {
        for (Favourites f:FavouriteBooks) {
            if (f.getBookid()==bookid)
            {
                return true;
            }
        }
        return false;
    }

}
