package movie.beans;

import javax.persistence.Entity;
import javax.persistence.IdClass;

/***********
 * 
 * 
 * @author grotes
 *
 **********/

@Entity @IdClass(RelTagPrimaryKey.class)
public class RelTagPrimaryKey {
	Integer codTag;
    Integer codMovie;
    
    public RelTagPrimaryKey(Integer codTag, Integer codMovie){
    	this.codMovie = codMovie;
    	this.codTag = codTag;
    }
    
    public RelTagPrimaryKey(){ }

	public Integer getCodTag() {
		return codTag;
	}

	public void setCodTag(Integer codTag) {
		this.codTag = codTag;
	}

	public Integer getCodMovie() {
		return codMovie;
	}

	public void setCodMovie(Integer codMovie) {
		this.codMovie = codMovie;
	}
    
    
}
