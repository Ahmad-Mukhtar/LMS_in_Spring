package com.LMS.LMS.Controller;

import com.LMS.LMS.Classes.BLL.BLLClasses.Login;
import com.LMS.LMS.Classes.BLL.BLLClasses.Register;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@Controller
public class RegisterController
{

    //Show register Page
    @GetMapping("/Register")
    public String showRegister(Model model) throws SQLException {

        model.addAttribute("Registerobj",new Register());

        return "Register";
    }

    //Register the user
    @PostMapping("/Register")
    public String validateAndRegisterUser(@ModelAttribute("Registerobj") Register register, @ModelAttribute("Errorvalue") String ev, HttpSession httpSession, Model model) throws SQLException, ParseException, IOException {

        String msg;
        if (register.checkusername(register.getUsername()))
        {
           if(register.registerUser(register.getPassword(),register.getDOB(),register.getEmail(),register.getFirstName(),register.getLastName(),register.getUsername(),register.getGender())) {

               model.addAttribute("flag",true);
               model.addAttribute("setmargin","margin-left:200px;");
               msg="Registered SuccessFully";

           }
           else
           {
               msg="Some Error Occured While Registering";
               model.addAttribute("setmargin","margin-left:160px;");
               model.addAttribute("flag",false);
           }

            model.addAttribute("regmssg",msg);
            return "Register";
        }
        else
        {
            msg="Username Already Taken";
            model.addAttribute("flag",false);
            model.addAttribute("setmargin","margin-left:200px;");
            model.addAttribute("regmssg",msg);
            return "Register";
        }

    }

}
