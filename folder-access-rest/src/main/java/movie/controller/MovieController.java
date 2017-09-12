package movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import movie.beans.Movie;
import movie.beans.MoviesResponse;
import movie.service.MovieService;


/*****************
 * 
 * @author grotes
 *
 ***************/

@RestController
public class MovieController{
  @Autowired
  private MovieService movieService;
  
  public MovieController() {}
  
  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/addAllMoviesFromFolder"})
  public MoviesResponse addAllMoviesFromFolder() {
    return movieService.addAllMoviesFromFolder();
  }
  


  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/listMovieByName"})
  public MoviesResponse listMovieByName(@RequestParam(name="name", required=true) String name, @RequestParam(name="page", required=false) Long page, @RequestParam(name="limit", required=false) Long limit)
  {
   return movieService.listMovieByName(name,page,limit);
  }
  


  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/listMovieByDate"})
  public MoviesResponse listMovieByDate(@RequestParam(name="page", required=false) Long page, @RequestParam(name="limit", required=false) Long limit)
  {
    return movieService.listMovieByDate(page, limit);
  }
  

  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/indexAll"})
  public MoviesResponse indexAll()
  {
    return movieService.indexAll();
  }
  
  
  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/generateThum"})
  public Movie generateThum(@RequestParam(name="id", required=true) Integer id)
  {
    return movieService.generateThum(id);
  }

  @CrossOrigin(origins={"${app.crossorigin.intra}"})
  @PostMapping({"/calculateDurationAndThumb"})
  public MoviesResponse calculateDurationAndThumb()
  {
    return movieService.calculateDurationAndThumb();
  }
  

  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/getMovie"})
  public Movie getMovie(@RequestParam(name="id", required=true) Integer id)
  {
    return movieService.getMovie(id);
  }
  
  @CrossOrigin(origins={"${app.crossorigin.all}"})
  @PostMapping({"/findMoviesByTags"})
  public MoviesResponse findMoviesByTags(@RequestParam(name="tag", required=true) String tag, @RequestParam(name="page", required=false) Long page, @RequestParam(name="limit", required=false) Long limit){
	  return movieService.findMoviesByTags(tag,page,limit);
  }
  
}
