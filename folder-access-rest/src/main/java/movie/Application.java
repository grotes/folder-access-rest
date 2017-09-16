package movie;

import java.util.concurrent.Executor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class Application {
  public Application() {}
  
  public static void main(String[] args) {
    org.springframework.boot.SpringApplication.run(Application.class, args);
  }
  
  @Bean
  public Executor asyncExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(4);
      executor.setMaxPoolSize(4);
      executor.setThreadNamePrefix("Thumb-");
      executor.initialize();
      return executor;
  }
}