package com.apporelbotna.gameserver.persistencews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
    	PostgreDAO dao = new PostgreDAO();
    	dao.connect();
    }
}
