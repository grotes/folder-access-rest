package movie;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*****************
 * 
 * @author grotes
 *
 ***************/

@RestController
public class MovieController{
  @Autowired
  private MovieRepository movieRepository;
  @Value("${command.first}")
  private String commandFirst;
  @Value("${command.first2}")
  private String commandFirst2;
  @Value("${command.second}")
  private String commandSecond;
  @Value("${command.third}")
  private String commandThird;
  @Value("${command.ls.text}")
  private String commandls;
  @Value("${command.ls.wait}")
  private Long commandLsWait;
  @Value("${command.end}")
  private String commandEnd;
  @Value("${directory}")
  private String directory;
  @Value("${savedirectory}")
  private String saveDirectory;
  @Value("${tmpdir}")
  private String tmpdir;
  @Value("${app.rows}")
  private Long rows;
  
  public MovieController() {}
  
  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/addAllMoviesFromFolder"})
  public MoviesResponse addAllMoviesFromFolder() {
    List<String> mov = new ArrayList<String>();
    List<Movie> ret = new ArrayList<Movie>();
    int cont = 0;
    try
    {
      String[] cmd = { commandFirst, commandFirst2, String.format(commandls,directory) };
      System.out.println("COMMAND:" + commandFirst + " " + commandFirst2 + " " + String.format(commandls,directory));
      Process p = Runtime.getRuntime().exec(cmd);
      BufferedReader stdInput = new BufferedReader(
        new InputStreamReader(p.getInputStream()));
      
      Thread.sleep(commandLsWait.longValue());

      String s = null;
      String[] m = null;
      String size = null;
      String duration = "";
      boolean searching = true;
      String ext = "";
      if (stdInput.ready()) {
        while ((s = stdInput.readLine()) != null) {
          m = s.trim().split(" ", 2);
          if (m.length == 2) {
            ext = m[1].substring(m[1].lastIndexOf(".") + 1).toLowerCase();
            if ((ext.equals("mp4")) || (ext.equals("mov")) || (ext.equals("avi")) || (ext.equals("wmv")) || 
              (ext.equals("mkv")) || (ext.equals("flv"))) {
              cont++;
              mov.add(m[1]);
              List<Movie> mv = movieRepository.findByName(m[1]);
              if (mv.size() > 0) {
                String nameInDDBB = ((Movie)mv.get(0)).getName().trim();
                if (nameInDDBB.equals(m[1])){
                  searching = false;
                }
              } else {
                size = String.valueOf(Long.parseLong(m[0]) / 1024 / 1024).concat("MB");
              }
              if (searching){
                ret.add(new Movie(duration, m[1], saveDirectory, size));
              }
            }
          }
          searching = true;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    movieRepository.deleteByNameNotIn(mov);
    movieRepository.save(ret);
    return new MoviesResponse(ret.size(), cont, ret, 0);
  }
  


  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/listMovieByName"})
  public MoviesResponse listMovieByName(@RequestParam(name="name", required=true) String name, @RequestParam(name="page", required=false) Long page, @RequestParam(name="limit", required=false) Long limit)
  {
    int p = 0;int l = rows.intValue();
    if (page != null) p = page.intValue();
    if ((limit != null) && 
      (limit.longValue() <= rows.longValue())) { l = limit.intValue();
    }
    Pageable lim = new PageRequest(p, l, new Sort(new Sort.Order[] { new Sort.Order(Sort.Direction.ASC, "name") }));
    List<Movie> mov = movieRepository.findByNameContainingIgnoreCase(name.replaceAll(" ", "%"), lim);
    int totalResults = movieRepository.countByNameContainingIgnoreCase(name.replaceAll(" ", "%")).intValue();
    MoviesResponse ret = new MoviesResponse(mov.size(), totalResults, mov, l);
    return ret;
  }
  


  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/listMovieByDate"})
  public MoviesResponse listMovieByDate(@RequestParam(name="page", required=false) Long page, @RequestParam(name="limit", required=false) Long limit)
  {
    int p = 0;int l = rows.intValue();
    if (page != null) p = page.intValue();
    if ((limit != null) && 
      (limit.longValue() <= rows.longValue())) { l = limit.intValue();
    }
    Pageable lim = new PageRequest(p, l, new Sort(new Sort.Order[] { new Sort.Order(Sort.Direction.DESC, "fecha") }));
    List<Movie> mov = movieRepository.findNews(lim);
    MoviesResponse ret = new MoviesResponse(mov.size(), mov.size(), mov, l);
    return ret;
  }
  

  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/indexAll"})
  public MoviesResponse indexAll()
  {
    List<Movie> mov = movieRepository.findMoviesForListing();
    MoviesResponse ret = new MoviesResponse(mov.size(), mov.size(), mov, 0L);
    return ret;
  }
  

  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/generateThum"})
  public Movie generateThum(@RequestParam(name="id", required=true) Integer id)
  {
    Movie mov = (Movie)movieRepository.findOne(id);
    if ((mov != null) && 
      (mov.getThumb() == null)) {
      try {
        String tmpImg = tmpdir.concat(String.valueOf(Calendar.getInstance().getTimeInMillis())).concat("p.png");
        String[] cmd = { commandFirst, commandFirst2, String.format(commandSecond,directory.concat(mov.getName())).concat(String.format(commandThird,"0",tmpImg)) };
        System.out.println("COMMAND:" + commandFirst + " " + commandFirst2 + " " + String.format(commandSecond,directory.concat(mov.getName())).concat(String.format(commandThird,"0",tmpImg)) + "\n");
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader stdInput = new BufferedReader(
          new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(
          new InputStreamReader(p.getErrorStream()));
        

        String s = null,duration=null;
        boolean error = false;
        if (mov.getDuration().trim().length() == 0) {
          if (stdInput.ready()) {
            while ((s = stdInput.readLine()) != null) {
              if (s.contains("Duration:")) {
            	System.out.println(s);
                s = s.replaceFirst("Duration: ", "");
                s = s.substring(0, s.indexOf(",")).trim();
                duration = s.substring(0, s.lastIndexOf("."));
                duration = duration.startsWith("00:") ? duration.substring(3) : duration;
                mov.setDuration(duration);
              }else if(s.contains("No such file or directory")){
              	error=true;
              }
            }
          }
          

          while ((s = stdError.readLine()) != null) {
            if (s.contains("Duration:")) {
              System.out.println(s);
              s = s.replaceFirst("Duration: ", "");
              s = s.substring(0, s.indexOf(",")).trim();
              duration = s.substring(0, s.lastIndexOf("."));
              duration = duration.startsWith("00:") ? duration.substring(3) : duration;
              mov.setDuration(duration);
            }else if(s.contains("No such file or directory")){
            	error=true;
            }
          }
        }
        String[] th = new String[4];
        for (int h = 1; h < 2; h++) {
          Path path = Paths.get(tmpImg);
          boolean conti = false;
          int i = 0;
          while ((!Files.exists(path)) && (!conti) &&(!error)){
            Thread.sleep(5000);
            i++;
            if (i == 15) {
              conti = true;
            }
          }
          byte[] binaryData = Files.readAllBytes(path);
          th[(h - 1)] = Base64.encodeBase64String(binaryData);
          Files.delete(path);
        }
        mov.setThumb(th[0]);
        


        movieRepository.save(mov);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    return mov;
  }
  
  public String executeCommandThumb(String movName, String min, String tmpImg, boolean returnDuration) {
	  String duration = null;
	  try{
	  String[] cmd = new String[]{ commandFirst, commandFirst2, String.format(commandSecond,directory.concat(movName)).concat(String.format(commandThird,min,tmpImg)) };
      System.out.println("COMMAND:" + commandFirst + " " + commandFirst2 + " " + String.format(commandSecond,directory.concat(movName)).concat(String.format(commandThird,min,tmpImg)) + "\n");
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
	  return duration;
  }
  
  
  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/generateThum2"})
  public Movie generateThum2(@RequestParam(name="id", required=true) Integer id)
  {
    Movie mov = (Movie)movieRepository.findOne(id);
    if ((mov != null) && 
      (mov.getThumb() == null)) {
      try {
    	File dr = new File(tmpdir.concat(mov.getId().toString()));
    	if(!dr.exists()){
    		dr.mkdirs();
    	}
        String tmpRutaImg = tmpdir.concat(mov.getId().toString()).concat("/");
        String tmpImg = tmpRutaImg.concat("0.png");
        Integer min=new Integer(0);
        String mins="00";
        //Capture second 40
        String duration = executeCommandThumb(mov.getName(), mins, tmpImg, true);
        String[] d = duration.split(":");
        if(d.length==2){
        	min=Integer.parseInt(d[0]);
        }else if(d.length==3){
        	min=Integer.parseInt(d[1]);
        }
        Integer inc = min/4;
        //Capture minute 2 second 40
        min=inc.intValue();
        mins=min.toString().length()==1 ? "0".concat(mins.toString()) : mins.toString();
        tmpImg = tmpRutaImg.concat("1.png");
        executeCommandThumb(mov.getName(), mins, tmpImg, false);
        //Capture minute 4 second 40
        min=min+inc;
        mins=min.toString().length()==1 ? "0".concat(mins.toString()) : mins.toString();
        tmpImg = tmpRutaImg.concat("2.png");
        executeCommandThumb(mov.getName(), mins, tmpImg, false);
        //Capture minute 6 second 40
        min=min+inc;
        mins=min.toString().length()==1 ? "0".concat(mins.toString()) : mins.toString();
        tmpImg = tmpRutaImg.concat("3.png");
        executeCommandThumb(mov.getName(), mins, tmpImg, false);

        mov.setDuration(duration);
        mov.setThumb(tmpRutaImg);
        movieRepository.save(mov);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    return mov;
  }

  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/calculateDurationAndThumb"})
  public MoviesResponse calculateDurationAndThumb()
  {
    List<Movie> mov = (List<Movie>)movieRepository.findAll();
    int cont = 0;
    for (int i = 0; (i < mov.size()) && (cont < 1); i++) {
      Movie m = (Movie)mov.get(i);
      if ((m.getDuration().trim().length() == 0) || (m.getThumb() == null)) {
        generateThum2(m.getId());
        cont++;
      }
    }
    return new MoviesResponse(0, cont, null, 0);
  }
  

  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/getMovie"})
  public Movie getMovie(@RequestParam(name="id", required=true) Integer id)
  {
    Movie mov = (Movie)movieRepository.findOne(id);
    if ((mov != null) && 
      (mov.getDuration().trim().length() == 0)) {
      try
      {
        String[] cmd = { commandFirst, commandFirst2, commandSecond.concat(" \"").concat(directory.concat(mov.getName().toString())).concat("\" ").concat(commandEnd) };
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader stdInput = new BufferedReader(
          new InputStreamReader(p.getInputStream()));
        
        BufferedReader stdError = new BufferedReader(
          new InputStreamReader(p.getErrorStream()));
        
        String s = null;
        String duration = "";
        while ((s = stdInput.readLine()) != null) {
          if (s.contains("Duration:")) {
            s = s.replaceFirst("Duration: ", "");
            s = s.substring(0, s.indexOf(",")).trim();
            duration = s.substring(0, s.lastIndexOf("."));
            duration = duration.startsWith("00:") ? duration.substring(3) : duration;
          }
        }
        

        while ((s = stdError.readLine()) != null) {
          if (s.contains("Duration:")) {
            s = s.replaceFirst("Duration: ", "");
            s = s.substring(0, s.indexOf(",")).trim();
            duration = s.substring(0, s.lastIndexOf("."));
            duration = duration.startsWith("00:") ? duration.substring(3) : duration;
          }
        }
        mov.setDuration(duration);
        movieRepository.save(mov);
      } catch (Exception e) { e.printStackTrace();
      }
    }
    return mov;
  }
  
}