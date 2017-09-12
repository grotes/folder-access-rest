package movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import movie.beans.Tag;

/****************
 * 
 * @author grotes
 *
 ***************/

@Repository
@Transactional(readOnly=true)
public abstract interface TagsRepository extends CrudRepository<Tag, Integer>{
	
	
	public abstract Tag findByTag(String paramString);
	
	@Query(value="select t.cod, t.tag, t.fecha from data_movies_tags t where t.tag regexp :tag",nativeQuery=true)
	public abstract List<Tag> findByTagIn(@Param("tag") String paramString);
}
