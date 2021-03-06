package movie;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


/****************
 * 
 * @author grotes
 *
 ***************/

@Transactional(readOnly=true)
public abstract interface MovieRepository extends CrudRepository<Movie, Integer>{
	
	public abstract List<Movie> findByNameContainingIgnoreCase(String paramString, Pageable paramPageable);
  
	public abstract List<Movie> findByName(String paramString);
  
	@Query("select new movie.Movie(m.id, m.duration, m.name, m.directory, m.size, m.thumb1) from Movie m order by m.fecha desc")
	public abstract List<Movie> findNews(Pageable paramPageable);
  
	@Query("select count(m.id) from Movie m where lower(m.name) like lower(concat('%', ?1,'%'))")
	public abstract Integer countByNameContainingIgnoreCase(String paramString);
  
	@Query("select new movie.Movie(m.id, m.duration, m.name, m.directory, m.size) from Movie m order by m.name")
	public abstract List<Movie> findMoviesForListing();
  
	@Transactional
	@Modifying
	public abstract List<Movie> deleteByNameNotIn(Collection<String> paramCollection);
  
}