package movie.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import movie.beans.Movie;


/****************
 * 
 * @author grotes
 *
 ***************/

@Repository
@Transactional(readOnly=true)
public abstract interface MovieRepository extends CrudRepository<Movie, Integer>{
	
	public abstract List<Movie> findByNameContainingIgnoreCase(String paramString, Pageable paramPageable);
  
	public abstract List<Movie> findByName(String paramString);
	
	@Query(value="select m.id,m.name,m.directory,m.duration,m.size,m.thumb,m.fecha,count(*) from movies as m, data_movies_tags as t, rel_movies_tags as r where m.id=r.cod_movie and r.cod_tag=t.cod and t.tag regexp ?1 group by m.name order by count(*) desc, m.name limit ?2,?3",nativeQuery=true)
	public abstract List<Movie> findByTagIn(String paramString, Integer limit, Integer offset);
	
	@Query(value="select count(*) from movies as m, data_movies_tags as t, rel_movies_tags as r where m.id=r.cod_movie and r.cod_tag=t.cod and t.tag regexp ?1 group by m.name",nativeQuery=true)
	public abstract List<Integer> countByTagIn(String paramString);
  
	@Query("select new movie.beans.Movie(m.id, m.duration, m.name, m.directory, m.size, m.thumb) from Movie m order by m.fecha desc")
	public abstract List<Movie> findNews(Pageable paramPageable);
  
	@Query("select count(m.id) from Movie m where lower(m.name) like lower(concat('%', ?1,'%'))")
	public abstract Integer countByNameContainingIgnoreCase(String paramString);
  
	@Query("select new movie.beans.Movie(m.id, m.duration, m.name, m.directory, m.size, m.thumb) from Movie m order by m.name")
	public abstract List<Movie> findMoviesForListing();
  
	@Transactional
	@Modifying
	public abstract List<Movie> deleteByNameNotIn(Collection<String> paramCollection);
  
}