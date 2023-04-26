package com.vmo.apartment_manager;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.User;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApartmentManagerApplication implements CommandLineRunner{

  @Autowired
  public PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  ApartmentRepository apartmentRepo;


  public static void main(String[] args) {
    SpringApplication.run(ApartmentManagerApplication.class, args);
  }
  @Override
  public void run(String... args) throws Exception {

//    User user = new User();
//    user.setUsername("123456789123");
//    user.setPassword(passwordEncoder.encode("12345678"));
//    userRepository.save(user);
//
//    for(int i = 1; i <= 10; i++){
//      for(int j = 1; j <= 10; j++){
//        Apartment apartment = new Apartment();
//        if(i < 10 && j == 10){
//          apartment.setName("0"+i+""+j + "H");
//          apartment.setCode("0"+i+""+j + "H");
//        } else if (i == 10 && j < 10) {
//          apartment.setName(i+"0"+j + "H");
//          apartment.setCode(i+"0"+j + "H");
//        } else if (i == 10 && j == 10) {
//          apartment.setName(i+""+j + "H");
//          apartment.setCode(i+""+j + "H");
//        } else{
//          apartment.setName("0"+i+"0"+j + "H");
//          apartment.setCode("0"+i+"0"+j + "H");
//        }
//
//        apartment.setStatus(false);
//        apartment.setArea(1000.0);
//        apartment.setDescription("good");
//        apartmentRepo.save(apartment);
//      }
//
//    }


  }
}
