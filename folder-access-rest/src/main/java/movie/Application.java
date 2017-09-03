package movie;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  public Application() {}
  
  public static void main(String[] args) {
    org.springframework.boot.SpringApplication.run(Application.class, args);
  }
}