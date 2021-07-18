package com.LMS.LMS.Classes.DAL;
import com.LMS.LMS.Classes.BLL.BLLClasses.*;
import com.LMS.LMS.Classes.BLL.Interfaces.*;
import java.sql.*;
import java.util.ArrayList;

//Implements The Required Interfaces to add factory Pattern
public class DAL implements ILogin, IRegisterUser, IBook, IUpdateProfile, IUserinfo,IBookissue,IReserveBooks,IHistory,IFavourites,IPenalty,IAdmin {

    //For DB Connection
    private Connection connection;

    public DAL() throws SQLException {

        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
        }
        catch (SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }
    }

    //Call The Stored Procedure and Validate User or Admin Login
    @Override
    public boolean validateLogin(String Username, String Password, String Usertype) throws SQLException {

        try {

            if (Usertype.equals("User")) {
                if (connection == null) {
                    return false;
                }
                CallableStatement callableStatement = connection.prepareCall("{call signin(?,?,?)}");

                callableStatement.setString(1, Username);

                callableStatement.setString(2, Password);

                callableStatement.registerOutParameter(3, Types.INTEGER);

                callableStatement.execute();

                int Result = callableStatement.getInt(3);

                callableStatement.close();

                if (Result == 1) {
                    connection.close();
                }

                return Result == 1;

            } else {
                CallableStatement callableStatement = connection.prepareCall("{call adminsignin(?,?,?)}");

                callableStatement.setString(1, Username);

                callableStatement.setString(2, Password);

                callableStatement.registerOutParameter(3, Types.INTEGER);

                callableStatement.execute();

                int Result = callableStatement.getInt(3);

                callableStatement.close();

                if (Result == 1) {
                    connection.close();
                }

                return Result == 1;
            }
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }

        return false;
    }

    //Check if the Entered Username Exist in DB or Not
    @Override
    public Boolean checkUsername(String Username) throws SQLException {

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select *from Member where username=?");

            preparedStatement.setString(1, Username);

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                return false;
            }
            preparedStatement.close();

            return true;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }
        return false;
    }

    //Register A User and add to DB
    @Override
    public Boolean registerUser(String Password, String Dob, String Email, String Firstname, String Lastname, String Username, String Gender) throws SQLException {

        try {
            CallableStatement callableStatement = connection.prepareCall("{call signup(?,?,?,?,?,?,?)}");

            callableStatement.setString(1, Password);

            callableStatement.setString(2, Dob);

            callableStatement.setString(3, Email);

            callableStatement.setString(4, Firstname);

            callableStatement.setString(5, Lastname);

            callableStatement.setString(6, Username);

            callableStatement.setString(7, Gender);

            callableStatement.execute();

            callableStatement.close();

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into Penalty (username,penaltyprice) values(?,0)");

            preparedStatement.setString(1,Username );

            preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return true;

        }
        catch (SQLException exception) {

            System.out.println(exception.toString());

            return false;
        }
        finally {

            if (connection != null)
                connection.close();
        }

    }

    //Get All he Books Present in DB
    @Override
    public ArrayList<Books> getBooks() throws SQLException
    {
        try {

            ArrayList<Books> booksArrayList = new ArrayList<>();

            Books books;

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from bookinfo");

            ResultSet resultSet = preparedStatement.executeQuery();

            int index = 0;

            while (resultSet.next()) {

                books = new Books();

                books.setId(resultSet.getInt("bookid"));

                books.setGenre(resultSet.getString("Genre"));

                books.setBookname(resultSet.getString("Bookname"));

                books.setBookDescription(resultSet.getString("BookDescription"));

                books.setCurrentStock(resultSet.getInt("CurrentStock"));

                books.setBookImageLink(resultSet.getString("Bookimagelink"));

                books.setAuthorname(resultSet.getString("Authorname"));

                books.setPublishername(resultSet.getString("Publishername"));

                booksArrayList.add(index, books);

                index++;
            }

            preparedStatement.close();

            connection.close();


            return booksArrayList;
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.toString());
        }

        return null;
    }

    //Update A User Profile and Make Change in DB
    @Override
    public Boolean updateProfile(String Firstname, String Lastname, String Password, String Email, String Dob,String username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Update  Member set Firstname=?,Lastname=?,memberpassword=?,email=?,Dob=? where username=?");

            preparedStatement.setString(1, Firstname);

            preparedStatement.setString(2, Lastname);

            preparedStatement.setString(3, Password);

            preparedStatement.setString(4, Email);

            preparedStatement.setString(5, Dob);

            preparedStatement.setString(6, username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }
        return false;
    }

    //Get all the info Abl=out the User and Store it
    @Override
    public ArrayList<String> setUserinfo(String Username) {

        try {

            ArrayList<String> Userinfo = new ArrayList<>();

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Member where username=?");

            preparedStatement.setString(1, Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Userinfo.add(0, resultSet.getString("Firstname"));
                Userinfo.add(1, resultSet.getString("Lastname"));
                Userinfo.add(2, resultSet.getString("Dob"));
                Userinfo.add(3, resultSet.getString("memberpassword"));
                Userinfo.add(4, resultSet.getString("email"));
                int noofBooksIssued = resultSet.getInt("NoofBooksIssued");
                int noofBooksReserved = resultSet.getInt("NoofBooksReserved");
                Userinfo.add(5, String.valueOf(noofBooksIssued));
                Userinfo.add(6, String.valueOf(noofBooksReserved));
            }

            preparedStatement.close();

            connection.close();


            return Userinfo;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }
        return null;
    }

    //Get All the Issued Books
    @Override
    public ArrayList<IssueBook> getIssuedBooks(String username) throws SQLException {

        try {

            ArrayList<IssueBook> IssuedBooks = new ArrayList<>();

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Bookissue where username=?");

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                IssueBook issuedBooks = new IssueBook(resultSet.getInt("Bookid"), resultSet.getString("username"), resultSet.getString("Issuedate"), resultSet.getString("duedate"));

                IssuedBooks.add(issuedBooks);
            }

            preparedStatement.close();

            connection.close();

            return IssuedBooks;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }
        return null;
    }

    //Get All the Reserved Books
    @Override
    public ArrayList<ReserveBook> getReservededBooks(String username) throws SQLException {

        try {

            ArrayList<ReserveBook> reserveBooks = new ArrayList<>();

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Bookreservation where username=?");

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReserveBook reserveBook = new ReserveBook(resultSet.getInt("Bookid"), resultSet.getString("username"), resultSet.getString("Reservationdate"), resultSet.getString("DueDate"));

                reserveBooks.add(reserveBook);
            }

            preparedStatement.close();

            connection.close();

            return reserveBooks;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }

        return null;

    }

    //Remove A Reserved Book From Database and set its coreespondig values
    @Override
    public boolean removereservedbook(String username, int bookid) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Delete from Bookreservation where username=? and Bookid=?");

            preparedStatement.setInt(2, bookid);

            preparedStatement.setString(1, username);

            int Result = preparedStatement.executeUpdate();

            if (Result > 0) {

                preparedStatement = connection.prepareStatement("update Member set NoofBooksReserved=NoofBooksReserved-1 where username=?");

                preparedStatement.setString(1, username);

                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("update bookinfo set CurrentStock=CurrentStock+1 where bookinfo.Bookid=?");

                preparedStatement.setInt(1, bookid);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                connection.close();

                return true;
            }

            connection.close();

            return false;
        }
        catch (SQLException sqlException) {

            System.out.println(sqlException.toString());
        }

        return false;
    }

    //Get All History Books
    @Override
    public ArrayList<History> getHistoryBooks(String Username) throws SQLException {

        try {

            ArrayList<History> HistoryBooks = new ArrayList<>();

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from History where username=?");

            preparedStatement.setString(1, Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                History hb = new History(resultSet.getInt("Bookid"), resultSet.getString("username"));

                HistoryBooks.add(hb);
            }

            preparedStatement.close();

            connection.close();

            return HistoryBooks;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return null;
    }

    //Get All Favourite Books
    @Override
    public ArrayList<Favourites> getFavouriteBooks(String Username) throws SQLException {

        try {

            ArrayList<Favourites> Favbooks = new ArrayList<>();

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Favourites where username=?");

            preparedStatement.setString(1, Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Favourites f = new Favourites(resultSet.getInt("Bookid"), resultSet.getString("username"));

                Favbooks.add(f);
            }

            preparedStatement.close();

            connection.close();

            return Favbooks;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return null;
    }

    //Renew A Book
    @Override
    public boolean RenewBook(int Bookid, String Username,String issuedate,String Duedate) throws SQLException {

        try {

            if (connection.isClosed())
            {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Update Bookissue set Issuedate=?,duedate=? where Bookid=? and username=?");

            preparedStatement.setString(1, issuedate);

            preparedStatement.setString(2, Duedate);

            preparedStatement.setInt(3, Bookid);

            preparedStatement.setString(4, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return false;

    }

    //Issue A Book
    @Override
    public boolean issueBook(int Bookid, String Username,String issuedate,String duedate) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into Bookissue(Bookid,username,Issuedate,duedate) values(?,?,?,?)");

            preparedStatement.setInt(1, Bookid);

            preparedStatement.setString(2, Username);

            preparedStatement.setString(3, issuedate);

            preparedStatement.setString(4, duedate);

            int Result = preparedStatement.executeUpdate();

            if (Result > 0) {

                preparedStatement = connection.prepareStatement("update Member set NoofBooksIssued=NoofBooksIssued+1 where username=?");

                preparedStatement.setString(1, Username);

                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("update bookinfo set CurrentStock=CurrentStock-1 where bookinfo.Bookid=?");

                preparedStatement.setInt(1, Bookid);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                connection.close();

                return true;
            }
            connection.close();
            return false;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }

        return false;
    }

    // Reserve A book
    @Override
    public boolean reserveBook(int Bookid, String Username, String Reservationdate, String duedate) throws SQLException
    {

        try {

            if (connection.isClosed())
            {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into Bookreservation(Bookid,username,Reservationdate,DueDate) values(?,?,?,?)");

            preparedStatement.setInt(1, Bookid);

            preparedStatement.setString(2, Username);

            preparedStatement.setString(3, Reservationdate);

            preparedStatement.setString(4, duedate);

            int Result = preparedStatement.executeUpdate();

            if (Result > 0) {

                preparedStatement = connection.prepareStatement("update Member set NoofBooksReserved=NoofBooksReserved+1 where username=?");

                preparedStatement.setString(1, Username);

                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("update bookinfo set CurrentStock=CurrentStock-1 where bookinfo.Bookid=?");

                preparedStatement.setInt(1, Bookid);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                connection.close();

                return true;
            }

            connection.close();

            return false;
        }

        catch (Exception exception) {

            System.out.println(exception.toString());
        }

        return false;
    }

    //Return A Book
    @Override
    public boolean ReturnBook(int Bookid,String Username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Delete from Bookissue where username=? and Bookid=?");

            preparedStatement.setInt(2, Bookid);

            preparedStatement.setString(1, Username);

            int Result = preparedStatement.executeUpdate();

            if (Result > 0) {

                preparedStatement = connection.prepareStatement("update Member set NoofBooksIssued=NoofBooksIssued-1 where username=?");

                preparedStatement.setString(1, Username);

                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("update bookinfo set CurrentStock=CurrentStock+1 where bookinfo.Bookid=?");

                preparedStatement.setInt(1, Bookid);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                connection.close();

                return true;
            }

            connection.close();

            return false;
        }

        catch (Exception exception) {

            System.out.println(exception.toString());
        }

        return false;
    }

   // Add the Returned Book to History in  DB
    @Override
    public boolean AddtoHistory(int Bookid,String Username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into History (Bookid,username) values(?,?)");

            preparedStatement.setInt(1, Bookid);

            preparedStatement.setString(2, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }

        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return false;
    }

    //Add to Favourites Table in DB
    @Override
    public boolean addToFavourites(int bookid, String Username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into Favourites(Bookid,username) values(?,?)");

            preparedStatement.setInt(1, bookid);

            preparedStatement.setString(2, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return false;

    }

    //Remove From Favourites Table in DB
    @Override
    public boolean removeFromFavourites(int bookid, String Username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Delete from Favourites where username=? and Bookid=?");

            preparedStatement.setInt(2, bookid);

            preparedStatement.setString(1, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }
        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return false;
    }

    //Get pealty value of Current User
    @Override
    public int getPenalty(String Username) throws SQLException {

        int price = 0;

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Penalty where username=?");

            preparedStatement.setString(1, Username);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                price = resultSet.getInt("penaltyprice");

            }

            preparedStatement.close();

            connection.close();

            return price;
        }

        catch (Exception exception) {

            System.out.println(exception.toString());
        }
        return price;

    }

    //Update penalty Value of A User
    @Override
    public boolean setPenalty(int Price, String Username) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Update Penalty set penaltyprice=? where username=?");

            preparedStatement.setInt(1, Price);

            preparedStatement.setString(2, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return false;
    }

    //Change Admin Password
    @Override
    public boolean changePassword(String Username, String Password) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Update  admininfo set adminpassword=? where username=?");

            preparedStatement.setString(1, Password);

            preparedStatement.setString(2, Username);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;
    }

    //Add The Book to DB
    @Override
    public boolean addBook(int Bookid, String Bookname, String BookDescription, String Bookimglink, String Genre, int CurrentStock, String Authorname, String Publishername) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into bookinfo(bookid,Genre,Bookname,BookDescription,CurrentStock,Bookimagelink,Authorname,Publishername) values(?,?,?,?,?,?,?,?)");

            preparedStatement.setInt(1, Bookid);

            preparedStatement.setString(2, Genre);

            preparedStatement.setString(3, Bookname);

            preparedStatement.setString(4, BookDescription);

            preparedStatement.setInt(5, CurrentStock);

            preparedStatement.setString(6, Bookimglink);

            preparedStatement.setString(7, Authorname);

            preparedStatement.setString(8, Publishername);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;
    }

    //Remove A Book From DB
    @Override
    public boolean removeBook(int Bookid) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Select * from Bookissue where Bookid=?");

            preparedStatement.setInt(1, Bookid);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<String> IssuedUsers = new ArrayList<>();

            ArrayList<String> ReservedUsers = new ArrayList<>();

            while (resultSet.next()) {
                IssuedUsers.add(resultSet.getString("username"));
            }

            preparedStatement = connection.prepareStatement("Select * from Bookreservation where Bookid=?");

            preparedStatement.setInt(1, Bookid);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReservedUsers.add(resultSet.getString("username"));
            }


            preparedStatement = connection.prepareStatement("Delete from bookinfo where Bookid=?");

            preparedStatement.setInt(1, Bookid);

            int Result = preparedStatement.executeUpdate();

            if (Result > 0) {
                preparedStatement = connection.prepareStatement("update Member set NoofBooksIssued=NoofBooksIssued-1 where username=?");

                for (String user : IssuedUsers) {
                    preparedStatement.setString(1, user);

                    preparedStatement.executeUpdate();
                }

                preparedStatement = connection.prepareStatement("update Member set NoofBooksReserved=NoofBooksReserved-1 where username=?");

                for (String user : ReservedUsers) {
                    preparedStatement.setString(1, user);

                    preparedStatement.executeUpdate();
                }

                preparedStatement.close();

                connection.close();

                return true;
            }
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;

    }

    //Update The Book Details in DB
    @Override
    public boolean updateBook(int Bookid, String Bookname, String BookDescription, String Bookimglink, String Genre, int CurrentStock, String Authorname, String Publishername) throws SQLException {

        try {

            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LibraryManagmentSystem;integratedSecurity=true");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("Update bookinfo set Genre=?,Bookname=?,BookDescription=?,CurrentStock=?,Bookimagelink=?,Authorname=?,Publishername=? where bookid=?");


            preparedStatement.setString(1, Genre);

            preparedStatement.setString(2, Bookname);

            preparedStatement.setString(3, BookDescription);

            preparedStatement.setInt(4, CurrentStock);

            preparedStatement.setString(5, Bookimglink);

            preparedStatement.setString(6, Authorname);

            preparedStatement.setString(7, Publishername);

            preparedStatement.setInt(8, Bookid);

            int Result = preparedStatement.executeUpdate();

            preparedStatement.close();

            connection.close();

            return Result > 0;
        }

        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return false;
    }

}