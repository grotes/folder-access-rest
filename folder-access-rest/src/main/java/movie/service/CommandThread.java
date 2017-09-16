package movie.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommandThread {
	
	@Value("${command.first}")
	private String commandFirst;
	@Value("${command.first2}")
	private String commandFirst2;
	@Value("${command.second}")
	private String commandSecond;
	@Value("${command.third}")
	private String commandThird;
	@Value("${directory}")
	private String directory;
	
	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
	
	public CommandThread(){ }
	
	@Async
	public CompletableFuture<String> executeCommandThumb(String movName, String min, String tmpImg, boolean returnDuration) {
		  String duration = null;
		  try{
		  String[] cmd = new String[]{ commandFirst, commandFirst2, String.format(commandSecond,directory.concat(movName)).concat(String.format(commandThird,min,tmpImg)) };
		  logger.info("\nCOMMAND:" + commandFirst + " " + commandFirst2 + " " + String.format(commandSecond,directory.concat(movName)).concat(String.format(commandThird,min,tmpImg)) + "\n");
	      Process p = Runtime.getRuntime().exec(cmd);

	      BufferedReader stdError = new BufferedReader(
	        new InputStreamReader(p.getErrorStream()));
	      String s = null;
	      while ((s = stdError.readLine()) != null) {
	          if (s.contains("Duration:")) {
	        		System.out.println(s);
	        		if(returnDuration){
		            s = s.replaceFirst("Duration: ", "");
		            s = s.substring(0, s.indexOf(",")).trim();
		            duration = s.substring(0, s.lastIndexOf("."));
		            duration = duration.startsWith("00:") ? duration.substring(3) : duration;
	            }
	          }
	        }
		  }catch(Exception e){e.printStackTrace();}
		  return CompletableFuture.completedFuture(duration);
	  }

}
