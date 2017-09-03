package movie;

import java.util.List;

/*******************
 * 
 * @author grotes
 *
 ******************/

public class MoviesResponse
{
  private final long size;
  private final long totalSize;
  private final List<Movie> movies;
  private final long rowsPerPage;
  
  public MoviesResponse(long size, long totalSize, List<Movie> movies, long rowsPerPage) {
    this.size = size;
    this.totalSize = totalSize;
    this.movies = movies;
    this.rowsPerPage = rowsPerPage;
  }
  
  public long getSize() {
    return size;
  }
  
  public long getTotalSize() {
    return totalSize;
  }
  
  public List<Movie> getMovies() {
    return movies;
  }
  
  public long getRowsPerPage() {
    return rowsPerPage;
  }
}