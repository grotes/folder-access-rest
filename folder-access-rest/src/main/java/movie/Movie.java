package movie;

import java.util.Date;
import javax.persistence.Column;
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
@Table(name="${app.tablename}")
public class Movie{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;
  private String name;
  private String directory;
  private String duration;
  private String size;
  @Column(name="thumb1", length=10000000)
  private String thumb1;
  @Column(name="thumb2", length=10000000)
  private String thumb2;
  @Column(name="thumb3", length=10000000)
  private String thumb3;
  @Column(name="thumb4", length=10000000)
  private String thumb4;
  private Date fecha;
  
  public Movie(Integer id, String duration, String name, String directory, String size, String thumb1)
  {
    this.id = id;
    this.duration = duration;
    this.name = name;
    this.directory = directory;
    this.size = size;
    this.thumb1 = thumb1;
  }
  
  public Movie(String duration, String name, String directory, String size, String thumb1) { 
	this.duration = duration;
    this.name = name;
    this.directory = directory;
    this.size = size;
    this.thumb1 = thumb1;
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
  
  public String getThumb1() {
	  return thumb1;
  }
  
  public String getThumb2() {
    return thumb2;
  }
  
  public String getThumb3() { 
	  return thumb3;
  }
  
  public String getThumb4() {
    return thumb4;
  }
  
  public Date getFecha() { 
	  return fecha; 
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
  
  public void setThumb1(String thumb1) { 
	  this.thumb1 = thumb1; 
  }
  
  public void setThumb2(String thumb2) {
    this.thumb2 = thumb2;
  }
  
  public void setThumb3(String thumb3) { 
	  this.thumb3 = thumb3; 
  }
  
  public void setThumb4(String thumb4) {
    this.thumb4 = thumb4;
  }
  
  public void setFecha(Date fecha) { 
	  this.fecha = fecha; 
  }
}