package movie.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import movie.beans.Movie;
import movie.beans.MoviesResponse;
import movie.beans.RelTag;
import movie.beans.Tag;
import movie.repository.MovieRepository;
import movie.repository.RelTagsRepository;
import movie.repository.TagsRepository;

@Service
public class MovieService {
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private TagsRepository tagsRepository;
	@Autowired
	private RelTagsRepository relTagsRepository;
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
	@Value("${tmpdirsave}")
	private String tmpdirsave;
	@Value("${app.rows}")
	private Long rows;
	@Value("${app.maxIterationsThumb}")
	private Integer maxIterationsThumb;
	@Value("${app.toMegas}")
	private Integer toMegas;

	public MoviesResponse addAllMoviesFromFolder() {
	    List<String> mov = new ArrayList<String>();
	    List<Tag> tags = new ArrayList<Tag>();
	    List<RelTag> relTags = new ArrayList<RelTag>();
	    List<Movie> ret = new ArrayList<Movie>();
	    int cont = 0;
	    try{
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
	                size = String.valueOf(Long.parseLong(m[0]) / toMegas).concat("MB");
	              }
	              if (searching){
	                ret.add(new Movie(duration, m[1], saveDirectory, size));
	                String[] tagsFormatted = m[1].substring(0, m[1].lastIndexOf(".")).replaceAll("-", " ").replaceAll("_", " ").replaceAll(".", " ").split(" ");
	                for(int i=0;i<tagsFormatted.length;i++){
	                	tags.add(new Tag(tagsFormatted[i]));
	                	relTags.add(new RelTag(tags.get(tags.size()-1).getCod(),ret.get(ret.size()-1).getId()));
	                	System.out.println((tags.size()-1)+" - "+(ret.size()-1)+"\n");
	                }
	              }
	            }
	          }
	          searching = true;
	        }
	      }
	    }catch (Exception e) {
	      e.printStackTrace();
	    }
	    
	    movieRepository.deleteByNameNotIn(mov);
	    ret = (List<Movie>) movieRepository.save(ret);
	    tags = (List<Tag>) tagsRepository.save(tags);
	    System.out.println("Procesados:\n");
	    for(int i=0;i<relTags.size();i++){
        	RelTag r = relTags.get(i);
        	r.setCodMovie(ret.get(r.getCodMovie()).getId());
        	r.setCodTag(tags.get(r.getCodTag()).getCod());
        	System.out.println(r.getCodMovie()+" - "+r.getCodTag()+"\n");
        	relTags.set(i, r);
        }
	    relTagsRepository.save(relTags);
	    
	    return new MoviesResponse(ret.size(), cont, ret, 0);
	  }
	
	public MoviesResponse listMovieByName(String name, Long page, Long limit){
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
	
	public MoviesResponse listMovieByDate(Long page, Long limit)
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
	
	public MoviesResponse indexAll()
	  {
	    List<Movie> mov = movieRepository.findMoviesForListing();
	    MoviesResponse ret = new MoviesResponse(mov.size(), mov.size(), mov, 0);
	    return ret;
	  }
	
	private String executeCommandThumb(String movName, String min, String tmpImg, boolean returnDuration) {
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
	
	public Movie generateThum(Integer id)
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
	        String rutaSaveImg = tmpdirsave.concat(mov.getId().toString()).concat("/");
	        String tmpImg = tmpRutaImg.concat("0.png");
	        Integer min=new Integer(0);
	        String mins="00";
	        //Capture second 40
	        String duration = executeCommandThumb(mov.getName(), mins, tmpImg, true);
	        String[] d = duration.split(":");
	        //get minutes, if longer than 1h get 60 minutes
	        if(d.length==2){
	        	min=Integer.parseInt(d[0]);
	        }else if(d.length==3){
	        	min=new Integer(60);
	        }
	        Integer inc = min/4;
	        //Capture minute 1/4 of minutes
	        min=inc.intValue();
	        mins=(min.toString().length()==1) ? "0".concat(min.toString()) : min.toString();
	        tmpImg = tmpRutaImg.concat("1.png");
	        executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        //Capture minute 2/4 of minutes
	        min=min+inc;
	        mins=(min.toString().length()==1) ? "0".concat(min.toString()) : min.toString();
	        tmpImg = tmpRutaImg.concat("2.png");
	        executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        //Capture minute 3/4 of minutes
	        min=min+inc;
	        mins=(min.toString().length()==1) ? "0".concat(min.toString()) : min.toString();
	        tmpImg = tmpRutaImg.concat("3.png");
	        executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        mov.setDuration(duration);
	        mov.setThumb(rutaSaveImg);
	        movieRepository.save(mov);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    
	    return mov;
	  }
	
	public MoviesResponse calculateDurationAndThumb()
	  {
	    List<Movie> mov = (List<Movie>)movieRepository.findAll();
	    int cont = 0;
	    for (int i = 0; (i < mov.size()) && (cont < maxIterationsThumb); i++) {
	      Movie m = (Movie)mov.get(i);
	      if ((m.getDuration().trim().length() == 0) || (m.getThumb() == null)) {
	        generateThum(m.getId());
	        cont++;
	      }
	    }
	    return new MoviesResponse(0, cont, null, 0);
	  }
	
	public Movie getMovie(Integer id)
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
