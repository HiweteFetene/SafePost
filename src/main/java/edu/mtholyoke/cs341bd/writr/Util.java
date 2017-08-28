package main.java.edu.mtholyoke.cs341bd.writr;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author jfoley
 */
public class Util {
  @Nullable
  public static String join(@Nullable String[] array) {
    if(array == null) return null;

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if(i > 0) sb.append(' ');
      sb.append(array[i]);
    }
    return sb.toString();
  }

  // just use EST rather than something fancier; TBD -- how might we configure this for different users.
  private static ZoneId EST = TimeZone.getTimeZone("EST").toZoneId();
  
  /**
   * convert time in millis to string representation
   * @param millis time in millis
   * @return a String representation of time
   */
  public static String dateToEST(long millis) {
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), EST);
    
    String month = Integer.toString(localDateTime.getMonth().getValue());
    String day = Integer.toString(localDateTime.getDayOfMonth());
    String hour = Integer.toString(localDateTime.getHour());
    String minute = Integer.toString(localDateTime.getMinute());
    
    if (localDateTime.getMonth().getValue() < 10)
    	month = "0" + localDateTime.getMonth().getValue();
    if (localDateTime.getDayOfMonth() < 10)
    	day = "0" + localDateTime.getDayOfMonth();
    if (localDateTime.getHour() < 10)
    	hour = "0" + localDateTime.getHour();
    if (localDateTime.getMinute() < 10)
    	minute = "0" + localDateTime.getMinute();
    
    String yyyymmdd = localDateTime.getYear()+"-"+month+"-"+ day;
    String hhmm = hour+":"+minute;

    return yyyymmdd+" "+hhmm;
  }
}
