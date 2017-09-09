package movie.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/***********
 * 
 * 
 * @author grotes
 *
 **********/

@Entity @IdClass(RelTagPrimaryKey.class)
public class RelTagPrimaryKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	Integer codTag;
	@Id
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
