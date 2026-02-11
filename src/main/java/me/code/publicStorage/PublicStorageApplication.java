package me.code.publicStorage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PublicStorageApplication {
	public static void main(String[] args) {
		if(System.getenv("SALT")==null||System.getenv("SALT").isBlank()){
            log.error("ERROR you need to declare the variable SALT for System.getenv()");
            return;
        }
        SpringApplication.run(PublicStorageApplication.class, args);
	}

}
