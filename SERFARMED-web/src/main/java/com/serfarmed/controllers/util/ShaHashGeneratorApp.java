package com.serfarmed.controllers.util;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This app will generate a salted, SHA-512 hashed password based on the 
 * username and password field values in the users table in the db. Use this 
 * to initially seed your database with the hashed passwords.
 * 
 * @author jlombardo
 */
public class ShaHashGeneratorApp {

    /**
     * @param args the command line arguments - not used.
     */
    public static void main(String[] args) {
        String salt = "romanidev@serfarmed.com"; // username field in db
        String password = "p"; // password field in db
        System.out.println(password + ": " + sha512(password,salt));
        
        salt = "romanidev@serfarmed.com"; // username field in db
        password = "password1"; // password field in db
        System.out.println(password + ": " + sha512(password,salt));
        
        salt = "sally@isp.com"; // username field in db
        password = "password2"; // password field in db
        System.out.println(password + ": " + sha512(password,salt));
        
        salt = "tom@isp.com"; // username field in db
        password = "password3"; // password field in db
        System.out.println(password + ": " + sha512(password,salt));
        
        
        
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String existingPassword = "password1"; // Password entered by user
//        String dbPassword = "bfff7a2fd68c19738012752f9d023712fd2d7201c6f771da910946f41171e4df8d2921c5bdd4d9189c0a6f162e61aec570c0d228df5ddfd4fb0634e32f0320d5";
//
//        if (passwordEncoder.matches(existingPassword, dbPassword)) {
//            // Encode new password and store it
//          System.out.println("Ok");
//        } else {
//          System.out.println("ERROR");
//            // Report error 
//        }
    }

    public static String sha512(String pwd, String salt) {

            ShaPasswordEncoder pe = new ShaPasswordEncoder(512);
            pe.setIterations(1024);
            String hash = pe.encodePassword(pwd, salt);

            return hash;
     
    }
}
