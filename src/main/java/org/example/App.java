package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "https://dbhander.github.io/midterm-frontend/")
@SpringBootApplication
public class App 
{
   public static void main(String[] args) {
       SpringApplication.run(App.class, args);
   }
}
