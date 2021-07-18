package com.LMS.LMS.Controller;
import com.LMS.LMS.Classes.BLL.BLLClasses.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

@Controller
public class UserController {

    //Create a user Object For User opertions
    private User user;

    @GetMapping("/UserPanel")
    public String showUserPanel(Model model, HttpSession httpSession) throws SQLException, ParseException {
        if(user==null)
        {
            if (httpSession.getAttribute("username")!=null) {
                user = new User(httpSession.getAttribute("username").toString());
            }
            else
            {
                return "redirect:/Login";
            }
        }

        if(httpSession.getAttribute("username")!=null) {
            Books b=new Books("username");

         model.addAttribute("AllUserBooks",b.getBooksArrayList());

            return "UserPanel/afterlogin";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Filter Results
    @GetMapping("/Filter")
    public String filterResults(@CookieValue("gencookie") String filval, Model model,HttpSession httpSession) throws SQLException {


        if(httpSession.getAttribute("username")!=null) {
            model.addAttribute("AllUserBooks",user.filterbyGenre(filval));
            return "UserPanel/afterlogin";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Search The Required Book And Display results
    @PostMapping("/Search")
    public String searchResults(@RequestParam("searchbox") String searchval, Model model,HttpSession httpSession) throws SQLException {


        if(httpSession.getAttribute("username")!=null) {
            ArrayList<Books> SearchedBooks=user.searchBooks(searchval);
            if (SearchedBooks.size()>0) {
                model.addAttribute("AllUserBooks", SearchedBooks);

                return "UserPanel/afterlogin";
            }
            else
            {
                String nores="No Results Found";
                model.addAttribute("nobooksfound",nores);
                return "UserPanel/afterlogin";
            }
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //show Edit Profile Page
    @GetMapping("/EditProfile")
    public String editProfile(Model model,HttpSession httpSession) throws SQLException {
        if(httpSession.getAttribute("username")!=null)
        {
            model.addAttribute("profobject",new UpdateProfile());
            model.addAttribute("mssg",new String());
            return "UserPanel/Editprofile";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Edit the User profile and save the results in database
    @PostMapping("/EditProfile")
    public String validateAndEditProfile(@ModelAttribute("profobject") UpdateProfile updateProfile,@ModelAttribute("mssg") String msg, Model model, HttpSession httpSession) throws SQLException {
        if(httpSession.getAttribute("username")!=null)
        {
            if (user.updateProfile(updateProfile.getFirstName(),updateProfile.getLastName(),updateProfile.getPassWord(),updateProfile.getEmail(),updateProfile.getDob())) {

                msg = "Profile Updated SuccessFully";
            }
            else
            {
                msg="Failed To Update Profile due to Unknown Reason";
            }

            model.addAttribute("mssg",msg);

            return "UserPanel/Editprofile";

        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Books which the user had Issued
    @GetMapping("/IssuedBooks")
    public String viewIssuedBooks(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            ArrayList<BooksView> booksViewArrayList=new ArrayList<>();
            BooksView booksView;
            for (IssueBook issueBook:user.getIssuedBooks())
            {

                booksView=new BooksView();

                booksView.setBookimglink(user.getbook(issueBook.getBookid()).getBookImageLink());

                booksView.setBookid(String.valueOf(user.getbook(issueBook.getBookid()).getId()));

                String[] date = issueBook.getDueDate().split(" ");

                String duedate = date[1] + " " + date[2] + " " + date[5];

                booksView.setDuedate(duedate);

                booksViewArrayList.add(booksView);
            }


            model.addAttribute("IssuedBooks",booksViewArrayList);
            return "UserPanel/ViewIssued";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //show issue book Page
    @GetMapping("/IssueaBook")
    public String issueBook(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            model.addAttribute("issuemssg", "");
            return "UserPanel/IssueBooks";
        }
        else
        {
            return "redirect:/Login";
        }
    }


    //Issue the Book on clicking issue Book Button under the Book gets the Id and set it to issue boook field
    @PostMapping("/autoissuing")
    public String autoIssuing(@RequestParam("autobookid") String Bookid,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            model.addAttribute("issuemssg", "");
            model.addAttribute("autobookid",Bookid);
            return "UserPanel/IssueBooks";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Book Details Page
    @GetMapping("/ViewDetails")
    public String viewBookDetaiils(@RequestParam("viewdetailid") String Bookid,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            if(user.checkFavouriteBook(Integer.parseInt(Bookid)))
            {
                model.addAttribute("Buttonname","Remove from Favourites");
            }
            else {
                model.addAttribute("Buttonname", "Add to Favourites");
            }
            model.addAttribute("bookobj",user.getbook(Integer.parseInt(Bookid)));
            return "UserPanel/viewbookdetails";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Add or remove the Book From Favourites
    @PostMapping("/AddorRemoveFavourites")
    public String addRemovefavourites(@RequestParam("favbookid") String Bookid,Model model,HttpSession httpSession) throws SQLException {
        if(httpSession.getAttribute("username")!=null)
        {
            if(user.checkFavouriteBook(Integer.parseInt(Bookid)))
            {

                user.RemoveFromFavourites(Integer.parseInt(Bookid));
                model.addAttribute("Buttonname", "Add to Favourites");
            }
            else {
                user.AddtoFavourites(Integer.parseInt(Bookid));
                model.addAttribute("Buttonname","Remove from Favourites");
            }
            model.addAttribute("bookobj",user.getbook(Integer.parseInt(Bookid)));

            return "UserPanel/viewbookdetails";
        }
        else
        {
            return "redirect:/Login";
        }
    }


    //Issue the Book manually
    @PostMapping("/IssueaBook")
    public String issueABook(@RequestParam("issuebookid") String Bookid,@ModelAttribute("issuemssg") String msg, Model model,HttpSession httpSession) throws SQLException {


        if(httpSession.getAttribute("username")!=null) {

            int bid=Integer.parseInt(Bookid);
            int result= user.issueABook(bid);
            if (result==0) {

                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:345px;");
                msg = "You have already Issued this Book";

            }

            else if(result==1)
            {
                model.addAttribute("flag",true);

                model.addAttribute("setmargin","margin-left:370px;");
                msg="Book Issued SuccessFully";
            }

            else if(result==2)
            {
                model.addAttribute("setmargin","margin-left:330px;");
                msg="Some Error Occured During Issuing the Book";
            }

            else if(result==3)
            {
                model.addAttribute("setmargin","margin-left:400px;");
                msg="Invalid BookId";
            }


            else if(result==4)
            {
                model.addAttribute("setmargin","margin-left:270px;");
                msg="You Have already Issued 3 Books You cannot Issue More Books";
            }

            else if(result==5)
            {
                model.addAttribute("setmargin","margin-left:360px;");
                msg="Book Out of Stock";
            }

            model.addAttribute("issuemssg",msg);

            return "UserPanel/IssueBooks";
        }
        else
        {
            return "redirect:/Login";
        }
    }


    //Show Reservd Books by user
    @GetMapping("/ReservedBooks")
    public String viewReservedBooks(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            ArrayList<BooksView> booksViewArrayList=new ArrayList<>();
            BooksView booksView;

            for (ReserveBook reserveBook:user.getReservedBooks())
            {

                booksView=new BooksView();

                booksView.setBookimglink(user.getbook(reserveBook.getBookid()).getBookImageLink());


                String[] date = reserveBook.getDueDate().split(" ");

                String duedate = date[1] + " " + date[2] + " " + date[5];

                booksView.setDuedate(duedate);

                booksViewArrayList.add(booksView);
            }


            model.addAttribute("ReservedBooks",booksViewArrayList);
            return "UserPanel/ViewReserved";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Reserve Book Page
    @GetMapping("/ReserveaBook")
    public String reserveBook(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            return "UserPanel/ReserveBooks";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //validate and Reserve the Book
    @PostMapping("/ReserveaBook")
    public String reserveABook(@RequestParam("reservebookid") String Bookid,@ModelAttribute("reservemssg") String msg, Model model,HttpSession httpSession) throws SQLException {


        if(httpSession.getAttribute("username")!=null) {

            int bid=Integer.parseInt(Bookid);
            int result= user.reserveABook(bid);
            if (result==0) {

                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:345px;");
                msg = "You Have already Reserved this Book";

            }

            else if(result==1)
            {
                model.addAttribute("flag",true);

                model.addAttribute("setmargin","margin-left:370px;");
                msg="Book Reserved SuccessFully";
            }

            else if(result==2)
            {
                model.addAttribute("setmargin","margin-left:330px;");
                msg="Some Error Occured During Reserving the Book";
            }

            else if(result==3)
            {
                model.addAttribute("setmargin","margin-left:400px;");
                msg="Invalid BookId";
            }


            else if(result==4)
            {
                model.addAttribute("setmargin","margin-left:270px;");
                msg="You Have already Reserved 3 Books You cannot Reserve More Books";
            }

            else if(result==5)
            {
                model.addAttribute("setmargin","margin-left:400px;");
                msg="Book Out of Stock";
            }

            else if (result == 6) {

                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:345px;");
                msg="This Book is Already Issued";

            }

            model.addAttribute("reservemssg",msg);

            return "UserPanel/ReserveBooks";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Sort by ascending
    @GetMapping("/SortbyAsc")
    public String sortByAsc(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            model.addAttribute("AllUserBooks", user.sortByAsc());
            return "UserPanel/afterlogin";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Sort by descending
    @GetMapping("/Sortbydesc")
    public String sortByDesc(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            model.addAttribute("AllUserBooks", user.sortBydsc());
            return "UserPanel/afterlogin";

        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show renew Book Page
    @GetMapping("/RenewBook")
    public String renewBook(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            return "UserPanel/Renewbook";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Renew The Book with Given ID
    @PostMapping("/RenewBook")
    public String renewABook(@RequestParam("RenewBookid") String Bookid,@ModelAttribute("renewmssg") String msg, Model model,HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("username")!=null) {

            int bid=Integer.parseInt(Bookid);
            int result= user.renewBook(bid);
            if (result==0) {

                model.addAttribute("flag",true);

                model.addAttribute("setmargin","margin-left:370px;");
                msg="Book Renewed SuccessFully";

            }

            else if(result==1)
            {
                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:330px;");
                msg="Some Error Occured During Renewing the Book";
            }

            else if(result==2)
            {
                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:390px;");
                msg="Book is not Issued";
            }



            model.addAttribute("renewmssg",msg);

            return "UserPanel/Renewbook";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Return Book Page
    @GetMapping("/returnBook")
    public String returnBook(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            return "UserPanel/ReturnBook";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Return book with given Id
    @PostMapping("/returnBook")
    public String returnABook(@RequestParam("returnbookid") String Bookid,@ModelAttribute("returnmssg") String msg, Model model,HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("username")!=null) {

            int bid=Integer.parseInt(Bookid);
            if (user.returnABook(bid)) {

                model.addAttribute("flag",true);

                model.addAttribute("setmargin","margin-left:370px;");

                msg="Book Returned SuccessFully";

            }

            else
            {
                model.addAttribute("flag",false);
                model.addAttribute("setmargin","margin-left:330px;");
                msg="Book Does not Exist  or is not Issued";
            }
            model.addAttribute("returnmssg",msg);

            return "UserPanel/ReturnBook";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Books Which were Issued and then Returned
    @GetMapping("/History")
    public String showHistory(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            ArrayList<BooksView> booksViewArrayList=new ArrayList<>();
            BooksView booksView;
            for (History history:user.getHistoryBooks())
            {

                booksView=new BooksView();

                booksView.setBookimglink(user.getbook(history.getBookid()).getBookImageLink());

                booksViewArrayList.add(booksView);
            }


            model.addAttribute("HistoryBooks",booksViewArrayList);

            return "UserPanel/History";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Show Favourites Book
    @GetMapping("/Favourites")
    public String showFavouriteBooks(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            ArrayList<BooksView> booksViewArrayList=new ArrayList<>();
            BooksView booksView;
            for (Favourites favourites:user.getFavouriteBooks())
            {

                booksView=new BooksView();

                booksView.setBookimglink(user.getbook(favourites.getBookid()).getBookImageLink());

                booksView.setBookid(String.valueOf(favourites.getBookid()));

                booksViewArrayList.add(booksView);
            }

            model.addAttribute("FavouriteBooks",booksViewArrayList);

            return "UserPanel/Favourites";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Remove Books From Favourites
    @PostMapping("/Favourites")
    public String removeFavourites(@RequestParam("favouritebookid") String Bookid,Model model,HttpSession httpSession) throws SQLException {
        if(httpSession.getAttribute("username")!=null)
        {

            user.RemoveFromFavourites(Integer.parseInt(Bookid));

            ArrayList<BooksView> booksViewArrayList=new ArrayList<>();

            BooksView booksView;

            for (Favourites favourites:user.getFavouriteBooks())
            {

                booksView=new BooksView();

                booksView.setBookimglink(user.getbook(favourites.getBookid()).getBookImageLink());

                booksView.setBookid(String.valueOf(favourites.getBookid()));

                booksViewArrayList.add(booksView);
            }

            model.addAttribute("FavouriteBooks",booksViewArrayList);

            return "UserPanel/Favourites";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Calculate and Show tHe penalty of this user
    @GetMapping("/Penalty")
    public String showPenalty(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            int penalty=user.getPenaltyPrice();

            String penaltymessage;

            if(penalty>0)
            {
                model.addAttribute("flag",false);
                penaltymessage="Please Pay Penalty of "+penalty+" Rs";
            }
            else
            {
                model.addAttribute("flag",true);
                penaltymessage="No Penalty to Pay";
            }
            model.addAttribute("penmssg",penaltymessage);

            return "UserPanel/Penalty";
        }
        else
        {
            return "redirect:/Login";
        }
    }

    //Read the Book By Given ID
    @PostMapping("/ReadBook")
    public String readBook(@RequestParam("readbookid") int Bookid ,Model model, HttpSession httpSession, HttpServletResponse response) throws IOException {

        if(httpSession.getAttribute("username")!=null)
        {

            String pathtopdf="src/Books/"+Bookid+".pdf";
            String filename=Bookid+".pdf";
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"filename\"");
            if(new File(pathtopdf).exists()) {
                InputStream inputStream = new FileInputStream(new File(pathtopdf));
                int nRead;
                while ((nRead = inputStream.read()) != -1) {
                    response.getWriter().write(nRead);
                }
            }
            else
            {
                model.addAttribute("statusmssg","Http Error Code: 404");

                model.addAttribute("errormssg","Message: Resource not found");

                return "error";
            }
        }
        return "";
    }

    //Sign Out User by removing the Session Id and Setting User to null
    @GetMapping("/Logout")
    public String logout(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("username")!=null)
        {
            httpSession.removeAttribute("username");
            user=null;
            return "redirect:/Login";
        }
        else
        {
            return "redirect:/Login";
        }
    }



}
