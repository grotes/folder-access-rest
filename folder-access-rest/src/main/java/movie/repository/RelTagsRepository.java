package movie.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import movie.beans.RelTag;

/****************
 * 
 * @author grotes
 *
 ***************/

@Repository
@Transactional(readOnly=true)
public abstract interface RelTagsRepository extends CrudRepository<RelTag, Integer>{
	
	public abstract List<RelTag> findByCodTagIn(Collection<Integer> paramInteger);
	
	public abstract List<RelTag> findByCodTagAndCodMovie(Integer codTag, Integer codMovie);
	
}
