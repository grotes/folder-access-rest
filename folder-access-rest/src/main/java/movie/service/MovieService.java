package movie.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
	private CommandThread command;
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
	    List<Tag> tags = null;
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
	                String[] tagsFormatted = m[1].substring(0, m[1].lastIndexOf(".")).replaceAll("-", " ").replaceAll("_", " ").
	                		replaceAll("\\.", " ").replaceAll("\\(", " ").replaceAll("\\)", " ").replaceAll("\\[", " ").
	                		replaceAll("\\]", " ").split(" ");
	                for(int i=0;i<tagsFormatted.length;i++){
	                	tags = new ArrayList<Tag>();
	                	Tag t = tagsRepository.findByTag(tagsFormatted[i]);
	                	if(t==null){t=new Tag(tagsFormatted[i]);}
                		tags.add(t);
                		tags = (List<Tag>) tagsRepository.save(tags);
                		relTags.add(new RelTag(tags.get(tags.size()-1).getCod(),ret.size()-1));
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
	  //movieRepository.deleteByNameNotIn(mov);
	    ret = (List<Movie>) movieRepository.save(ret);
	    for(int i=0;i<relTags.size();i++){
        	RelTag r = relTags.get(i);
        	r.setCodMovie(ret.get(r.getCodMovie()).getId());
        	//List<RelTag> lr = relTagsRepository.findByCodTagAndCodMovie(r.getCodTag(), r.getCodMovie());
        	RelTag lr = null;
        	for(int h=0;h<relTags.size();h++){
        		if(h!=i && (relTags.get(i).getCodMovie() == relTags.get(h).getCodMovie() && relTags.get(i).getCodTag() == relTags.get(h).getCodTag())){
        			lr = relTags.get(h);
        			h=relTags.size();
        		}
        	}
        	if(lr != null){
        		relTags.remove(i);
        		i--;
        	}else{
        		relTags.set(i, r);
        	}        	
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
	        long start = System.currentTimeMillis();
	        System.out.println("Comienzo: "+Calendar.getInstance().getTime());
	        CompletableFuture<String> duration1 = command.executeCommandThumb(mov.getName(), mins, tmpImg, true);
	        String[] d = duration1.get().split(":");
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
	        CompletableFuture<String> duration2 = command.executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        //Capture minute 2/4 of minutes
	        min=min+inc;
	        mins=(min.toString().length()==1) ? "0".concat(min.toString()) : min.toString();
	        tmpImg = tmpRutaImg.concat("2.png");
	        CompletableFuture<String> duration3 = command.executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        //Capture minute 3/4 of minutes
	        min=min+inc;
	        mins=(min.toString().length()==1) ? "0".concat(min.toString()) : min.toString();
	        tmpImg = tmpRutaImg.concat("3.png");
	        CompletableFuture<String> duration4 = command.executeCommandThumb(mov.getName(), mins, tmpImg, false);
	        
	        //Wait until they are all done
	        CompletableFuture.allOf(duration1,duration2,duration3,duration4).join();
	        start = System.currentTimeMillis() - start;
	        System.out.println("Fin: "+Calendar.getInstance().getTime()+"\nTiempo: "+((float)(start/1000)/60)+" minutos");
	        mov.setDuration(duration1.get());
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
	
	public MoviesResponse findMoviesByTags(String name, Long page, Long limit){
		int p = 0;int l = rows.intValue();
	    if (page != null) p = page.intValue();
	    if ((limit != null) && 
	      (limit.longValue() <= rows.longValue())) { l = limit.intValue();
	    }
	    List<Integer> totalResults;
	    List<Movie> movies;
	    if(name.trim().length()==0){
	    	Pageable lim = new PageRequest(p, l, new Sort(new Sort.Order[] { new Sort.Order(Sort.Direction.ASC, "name") }));
	    	movies = movieRepository.findByNameContainingIgnoreCase(name.replaceAll(" ", "%"), lim);
	    	totalResults = Arrays.asList(new Integer[movieRepository.countByNameContainingIgnoreCase(name.replaceAll(" ", "%")).intValue()]);
	    }else{
	    	p=p*l;
	    	movies = movieRepository.findByTagIn(name.replaceAll(" ", "\\|"),p,l);
	    	totalResults = movieRepository.countByTagIn(name.replaceAll(" ", "\\|"));
	    }
		int tot = (totalResults==null) ? 0 : totalResults.size();
		return new MoviesResponse(movies.size(), tot, movies, l);
	}
}
