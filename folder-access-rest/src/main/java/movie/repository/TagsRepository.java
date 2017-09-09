package movie.repository;

import org.springframework.data.repository.CrudRepository;
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
	
	  
}
