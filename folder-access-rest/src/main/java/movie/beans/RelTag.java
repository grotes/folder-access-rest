package movie.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

/***********
 * 
 * 
 * @author grotes
 *
 **********/

@Entity
@Table(name="${app.tablename.reltags}")
public class RelTag implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private RelTagPrimaryKey primaryKey;
	private Date fecha;
	
	public RelTag(Integer codTag, Integer codMovie){
		this.primaryKey = new RelTagPrimaryKey(codTag, codMovie);
	}
	
	public RelTag(){ }
	
	public Integer getCodTag() {
		return primaryKey.getCodTag();
	}
	public void setCodTag(Integer codTag) {
		this.primaryKey.setCodTag(codTag);
	}
	public Integer getCodMovie() {
		return primaryKey.getCodMovie();
	}
	public void setCodMovie(Integer codMovie) {
		this.primaryKey.setCodMovie(codMovie);
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public RelTagPrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(RelTagPrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
}
