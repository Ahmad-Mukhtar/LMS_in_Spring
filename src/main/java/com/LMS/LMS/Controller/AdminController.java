package com.LMS.LMS.Controller;
import com.LMS.LMS.Classes.BLL.BLLClasses.Admin;
import com.LMS.LMS.Classes.BLL.BLLClasses.Books;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


//Admin Controller Here Admin Operation Will be Performed
@Controller
public class AdminController
{
    private Admin admin;

    //Show Admin Panel and load books from Database
    @GetMapping("/AdminPanel")
    public String showAdminpanel(Model model, HttpSession httpSession) throws SQLException, ParseException {
        if(admin==null)
        {
            if(httpSession.getAttribute("admin")!=null) {
                admin = new Admin(httpSession.getAttribute("admin").toString());
            }
            else
            {
                return "redirect:/AdminLogin";
            }
        }

        if(httpSession.getAttribute("admin")!=null) {
            Books b=new Books("admin");

            model.addAttribute("AllAdminBooks",b.getBooksArrayList());

            return "AdminPanel/adminHome";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Open Edit Profile Window here
    @GetMapping("/Editadprofile")
    public String editAdminprofile(Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null) {

            model.addAttribute("profmssg", "");
            return "AdminPanel/editadprofile";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //On clicking Edit profile Edit the Profile and Return the Response
    @PostMapping("/Editadprofile")
    public String validateAndEditProfile(@RequestParam("changepass") String newpass,Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null) {

            if(admin.UpdateProfile(newpass)) {
                model.addAttribute("profmssg","Profile Updated SuccessFully");
            }
            else
            {
                model.addAttribute("profmssg","Some Error Occurred");
            }


            return "AdminPanel/editadprofile";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Show add books window here
    @GetMapping("/addBooks")
    public String addBooks(Model model, HttpSession httpSession) throws SQLException, ParseException {
        if(httpSession.getAttribute("admin")!=null) {


            model.addAttribute("bookobj",new Books());
            return "AdminPanel/addbooksadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Validate and Add the Book if all the info is correct
    @PostMapping("/addBooks")
    public String validateandaddBooks(@RequestParam("addedfile") MultipartFile bookimg, @ModelAttribute("bookobj") Books books, Model model, HttpSession httpSession) throws SQLException, ParseException, IOException {

        if(httpSession.getAttribute("admin")!=null) {

            String path="src/Images/";
            String savepath="src/main/webapp/"+path;
            path=path+bookimg.getOriginalFilename();
            books.setBookImageLink(path);

            if (admin.AddBooks(books.getId(), books.getBookname(),books.getBookDescription(),books.getBookImageLink(),books.getGenre(),books.getCurrentStock(),books.getAuthorname(),books.getPublishername()))
            {
                Path filepath = Paths.get(savepath, bookimg.getOriginalFilename());
                bookimg.transferTo(filepath);
                model.addAttribute("successmsg","Book Added SuccessFully");
            }
            else
            {
                model.addAttribute("boookidmssg","Book id Already taken");
            }
            return "AdminPanel/addbooksadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Show add books window here
    @GetMapping("/RemoveBooks")
    public String removeBooks(Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null) {

            return "AdminPanel/Removebooksadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }
    //Validate and delete the Book entered by Admin
    @PostMapping("/RemoveBooks")
    public String validateAndRemoveBooks(@RequestParam("delidfield") String Bookid, Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null) {

            if (admin.DeleteBook(Integer.parseInt(Bookid)))
            {
                model.addAttribute("flag",true);
                model.addAttribute("delmssg","Book Deleted SuccessFully");
            }
            else
            {
                model.addAttribute("flag",false);
                model.addAttribute("delmssg","Book id Does not Exist or Some Error Occurred");
            }

            return "AdminPanel/Removebooksadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Show Update books window here
    @GetMapping("/UpdateBook")
    public String updateBooks(Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null) {
            model.addAttribute("bookobj",new Books());
            return "AdminPanel/updatebookadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //validate and Update the Book if Exists
    @PostMapping("/UpdateBook")
    public String validateandUpdateBooks(@RequestParam("addedfile") MultipartFile bookimg, @ModelAttribute("bookobj") Books books, Model model, HttpSession httpSession) throws SQLException, ParseException, IOException {

        if(httpSession.getAttribute("admin")!=null) {

            String path="";
            String savepath="";
            //if book image is updated
            if(!bookimg.isEmpty()) {
                path = "src/Images/";
                 savepath= "src/main/resources/static/" + path;
                path = path + bookimg.getOriginalFilename();
                books.setBookImageLink(path);
                Path filepath = Paths.get(savepath, bookimg.getOriginalFilename());
                bookimg.transferTo(filepath);
            }

            if (admin.UpdateBooks(books.getId(), books.getBookname(),books.getBookDescription(),path,books.getGenre(),books.getCurrentStock(),books.getAuthorname(),books.getPublishername()))
            {

                model.addAttribute("successmsg","Book Updated SuccessFully");
            }
            else
            {
                model.addAttribute("boookidmssg","Book id Does not Exist");
            }
            return "AdminPanel/updatebookadmin";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Signout admin by Removing the Session Attribute
    @GetMapping("/SignoutAdmin")
    public String signoutAdmin(Model model, HttpSession httpSession) throws SQLException, ParseException {

        if(httpSession.getAttribute("admin")!=null)
        {
            httpSession.removeAttribute("admin");
            admin=null;
        }
        return "redirect:/AdminLogin";
    }

    //Search the Book By its name
    @PostMapping("/SearchBooks")
    public String searchResults(@RequestParam("searchbox") String searchval, Model model, HttpSession httpSession) throws SQLException {


        if(httpSession.getAttribute("admin")!=null) {
            ArrayList<Books> SearchedBooks=admin.getsearchResults(searchval);
            if (SearchedBooks.size()>0) {
                model.addAttribute("AllAdminBooks", SearchedBooks);

                return "AdminPanel/adminHome";
            }
            else
            {
                String nores="No Results Found";
                model.addAttribute("nobooksfound",nores);
                return "AdminPanel/adminHome";
            }
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }

    //Show Book Details its id ,name etc
    @GetMapping("/ViewBookDetails")
    public String viewBookDetaiils(@RequestParam("viewdetailid") String Bookid,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("admin")!=null)
        {
            model.addAttribute("bookobj",admin.getBook(Integer.parseInt(Bookid)));
            return "AdminPanel/adbookdet";
        }
        else
        {
            return "redirect:/AdminLogin";
        }
    }
}
