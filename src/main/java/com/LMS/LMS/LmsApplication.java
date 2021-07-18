/*
Header
Name: Ahmed Noor,Ahmad Mukhtar,Afaq,Sarab
Roll no: 18L-0950,18L-1079,18L-1035,18L-1058
email: l180950@lhr.nu.edu.pk , l181079@lhr.nu.edu.pk l181035@lhr.nu.edu.pk , l181058@lhr.nu.edu.pk
Section: 6A

This is a Library Management System in Which there one is a User and one is Admin.
Admin can add delete and Update Books.
Where as user can issue, reserve,add/remove from favourites ,edit his profile, sort Books and Seaerch the By Name and genre
User first needs to register to access all the above Features.
 */

package com.LMS.LMS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LmsApplication {
	//Main Method For Starting the Application
	public static void main(String[] args) {
		System.out.println("This is a Library Management System in Which there one is a User and one is Admin.\n" +
				"Admin can add delete and Update Books.\n" +
				"Where as user can issue, reserve,add/remove from favourites edit his profile, sort Books and Seaerch the By Name and genre\n" +
				"User first needs to register to access all the above Features.");
		SpringApplication.run(LmsApplication.class, args);
	}

}
