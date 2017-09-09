package movie.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/***********
 * 
 * 
 * @author grotes
 *
 **********/

@Entity
@Table(name="${app.tablename.movies}")
public class Movie implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;
  private String name;
  private String directory;
  private String duration;
  private String size;
  private String thumb;
  private Date fecha;
  
  
  public Movie(Integer id, String duration, String name, String directory, String size, String thumb)
  {
    this.id = id;
    this.duration = duration;
    this.name = name;
    this.directory = directory;
    this.size = size;
    this.thumb = thumb;
  }
  
  
  public Movie(int id, String duration, String name, String directory, String size) { 
	this.id = Integer.valueOf(id);
    this.duration = duration;
    this.name = name;
    this.directory = directory;
    this.size = size;
  }
  
  public Movie(String duration, String name, String directory, String size) { 
	this.duration = duration;
    this.name = name;
    this.directory = directory;
    this.size = size;
  }
  
  public Movie(int id) { 
	  this.id = Integer.valueOf(id);
  }
  
  public Movie() {}
  
  
  
  public String getDuration(){
    return duration;
  }
  
  public String getName() { 
	  return name; 
  }
  
  public String getDirectory() {
    return directory;
  }
  
  public String getSize() {
	  return size;
  }
  
  public Integer getId() {
    return id;
  }
  
  public Date getFecha() { 
	  return fecha; 
  }
  public String getThumb() { 
	  return thumb; 
  }
  
  
  

  public void setId(Integer id){
    this.id = id;
  }
  
  public void setDuration(String duration) {
	  this.duration = duration; 
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setDirectory(String directory) { 
	  this.directory = directory; 
  }
  
  public void setSize(String size) {
    this.size = size;
  }
  
  public void setFecha(Date fecha) { 
	  this.fecha = fecha; 
  }
  public void setThumb(String thumb) {
	    this.thumb = thumb;
  }
  
}