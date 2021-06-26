package com.LMS.LMS.Controller;
import com.LMS.LMS.Classes.BLL.BLLClasses.Books;
import com.LMS.LMS.Classes.BLL.BLLClasses.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


@Controller
public class HomeandLoginController
{
    private Books books;

    //Show Homepage and load books from database and Display it
    @GetMapping("/")
    public String showHome(Model model) throws SQLException {
        books=new Books("#");
        model.addAttribute("AllBooks",books.getBooksArrayList());
        return "Homepage";
    }

    //Show Terms Page
    @GetMapping("/Terms")
    public String showTerms()
    {
        return "Terms";
    }

    //Show About Us page
    @GetMapping("/Aboutus")
    public String showAboutUs()
    {
        return "Aboutus";
    }

    //Show AdminLogin page
    @GetMapping("/AdminLogin")
    public String showAdminlogin(Model model) throws SQLException {
        model.addAttribute("adloginobj",new Login());
        model.addAttribute("Errorvalue",new String());
        return "Adminlogin";
    }

    //Validate and loginadmin
    @PostMapping("/AdminLogin")
    public String validateAdmin(@ModelAttribute("adloginobj") Login login, @ModelAttribute("Errorvalue") String ev, HttpSession httpSession, Model model) throws SQLException, ParseException, IOException {
        if (login.Login(login.getUsername(),login.getPassword(),"Admin"))
        {
            httpSession.setAttribute("admin",login.getUsername());
            return "redirect:/AdminPanel";
        }
        else
        {

            ev = "Invalid Credentials";
            model.addAttribute("Errorvalue",ev);
            return "Adminlogin";
        }

    }

    //Show User Login page
    @GetMapping("/Login")
    public String showLogin(Model model) throws SQLException {
        model.addAttribute("loginobj",new Login());
        model.addAttribute("Errorvalue",new String());
        return "Login";
    }

    //Validate and login User
    @PostMapping("/Login")
    public String validateUser(@ModelAttribute("loginobj") Login login, @ModelAttribute("Errorvalue") String ev, HttpSession httpSession, Model model) throws SQLException, ParseException, IOException {
        if (login.Login(login.getUsername(),login.getPassword(),"User"))
        {
            httpSession.setAttribute("username",login.getUsername());
            return "redirect:/UserPanel";
        }
        else
        {

            ev = "Invalid Credentials";
            model.addAttribute("Errorvalue",ev);
            return "Login";
        }

    }

    //Show Search Results
    @PostMapping("/SearchResults")
    public String SearchResults(@RequestParam("searchvalue") String searchval,Model model) throws SQLException {

            ArrayList<Books> SearchedBooks=getSearchedBook(searchval);

            if (SearchedBooks.size()>0)
            {
                model.addAttribute("AllBooks",SearchedBooks);
                return "Homepage";
            }
            else
            {
                String nores="No Results Found";
                model.addAttribute("noResults",nores);
                return "Homepage";
            }
    }

    //Get List of searched Books
    private ArrayList<Books> getSearchedBook(String Searchvalue)
    {
        ArrayList<Books> SearchResults=new ArrayList<>();

        for (Books b:books.getBooksArrayList()) {
            Books books=new Books(b);
            if (books.getBookname().contains(Searchvalue))
            {
                SearchResults.add(books);
            }

        }
        return SearchResults;
    }



}
