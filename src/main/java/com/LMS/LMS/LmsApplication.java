/*
Header
Name: Ahmed Noor,Ahmad Mukhtar,Afaq,Sarab
Roll no: 18L-0950,18L-1079,18L-1035,18L-1058
email: l180950@lhr.nu.edu.pk , l181079@lhr.nu.edu.pk l181035@lhr.nu.edu.pk , l181058@lhr.nu.edu.pk
Section: 6A

This is a Libraray Managment System in Which there one is a User and one is Admin.
Admin can add delete and Update Books.
Where as user can issue, reserve,add/remove from favourites etc
User first needs to register to access all the above Feautures.
 */

package com.LMS.LMS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		System.out.println("This is a Libraray Managment System in Which there one is a User and one is Admin.\n" +
				"Admin can add delete and Update Books.\n" +
				"Where as user can issue, reserve,add/remove from favourites etc\n" +
				"User first needs to register to access all the above Feautures.");
		SpringApplication.run(LmsApplication.class, args);
	}

}
