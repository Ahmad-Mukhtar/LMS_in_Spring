package com.LMS.LMS.Classes.BLL.BLLClasses;



import com.LMS.LMS.Classes.BLL.Interfaces.*;
import com.LMS.LMS.Classes.DAL.DAL;

import java.sql.SQLException;

public class DataAccessFactory
{
    public static ILogin getLogindal() throws SQLException {

        return new DAL();
    }


    public static IRegisterUser getRegisterDal() throws SQLException {

        return new DAL();
    }

    public static IBook getBookDal() throws SQLException {

        return new DAL();
    }

    public static IUpdateProfile getProfileUpdateDal() throws SQLException {

        return new DAL();
    }

    public static IUserinfo getUserinfoDal() throws SQLException {

        return new DAL();
    }

    public static IBookissue getIssueBookDal() throws SQLException {

        return new DAL();
    }

    public static IReserveBooks getreserveBookDal() throws SQLException {

        return new DAL();
    }
    public static IHistory getHistoryBookDal() throws SQLException {

        return new DAL();
    }
    public static IFavourites getFavouritesDal() throws SQLException {

        return new DAL();
    }

    public static IPenalty getPenaltydal() throws SQLException {

        return new DAL();
    }

    public static IAdmin getAdmindal() throws SQLException {
        return new DAL();
    }
}
