package movie.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Entity;
import javax.persistence.Table;

/***********
 * 
 * 
 * @author grotes
 *
 **********/

@Entity @IdClass(RelTag.class)
@Table(name="${app.tablename.reltags}")
public class RelTag implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer codTag;
	@Id
	private Integer codMovie;
	private Date fecha;
	
	public RelTag(Integer codTag, Integer codMovie){
		this.codMovie = codMovie;
    	this.codTag = codTag;
	}
	
	public RelTag(){ }
	
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
