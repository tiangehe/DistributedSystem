package servlets;

import DAO.LiftRideDao;
import DAO.TotalVerticalDao;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.LiftRide;
import model.TotalVertical;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "servlets.SkierServlet")
public class SkierServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a url
    if (urlPath == null || urlPath.isEmpty()){
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      Integer resortID = Integer.parseInt(urlParts[1]);
      Integer seasonID = Integer.parseInt(urlParts[3]);
      Integer dayID = Integer.parseInt(urlParts[5]);
      Integer skierID = Integer.parseInt(urlParts[7]);
      res.setStatus(HttpServletResponse.SC_OK);
      BufferedReader readLiftRide = req.getReader();
      String line = readLiftRide.readLine();
      System.out.println(line);
      String[] time_id = line.split("[:,]");
      int time = Integer.parseInt(time_id[1]);
      int id = Integer.parseInt(time_id[3].substring(0,time_id[3].length()-1));
      String primaryKey = seasonID + "_" + dayID + "_" + time + "_" + skierID;
      String verticalPrimaryKey = seasonID + "_" + dayID + "_" + skierID;
      LiftRideDao liftDao = new LiftRideDao();
      LiftRide liftRide = new LiftRide(time,resortID,seasonID,dayID,skierID,primaryKey);
      liftDao.createLiftRide(liftRide);
      TotalVerticalDao totalVerticalDao = new TotalVerticalDao();
      totalVerticalDao.createTotalVertical(new TotalVertical(id*10, verticalPrimaryKey));
      // do any sophisticated processing with urlParts which contains all the url params
      res.getWriter().write(new Gson().toJson(liftRide.toString()));
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      for (String part : urlParts) {
        res.getWriter().write(part + "\r\n");
      }
      res.getWriter().write(urlParts.length + "\r\n");
      res.getWriter().write(urlParts[2] + "\r\n");
      res.getWriter().write("invalid url");
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      Integer resortID = Integer.parseInt(urlParts[1]);
      Integer seasonID = Integer.parseInt(urlParts[3]);
      Integer dayID = Integer.parseInt(urlParts[5]);
      Integer skierID = Integer.parseInt(urlParts[7]);
      String primaryKey = seasonID + "_" + dayID + "_" + skierID;
      TotalVerticalDao totalVerticalDao = new TotalVerticalDao();
      int totalVertical = totalVerticalDao.getTotalVertical(primaryKey);
      // do any sophisticated processing with urlParts which contains all the url params
      res.getWriter().write("" + totalVertical);
    }
  }

  private boolean isUrlValid(String[] urlPath) {
//    return true;
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlPath = "/1/vertical"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    // urlParts = [, 1, vertical]
    if (urlPath.length == 3 && urlPath[2].equals("vertical")) {return true;}
    if (urlPath.length == 8 && urlPath[2].equals("seasons") && urlPath[4].equals("day") &&
        urlPath[6].equals("skier")) {return true;}
    return true;
  }

//  public static JSONObject readJSONObject(HttpServletRequest request){
//    StringBuilder stringBuilder = new StringBuilder();
//    try(BufferedReader reader = request.getReader()){
//      String line = null;
//      while((line = reader.readLine()) != null){
//        stringBuilder.append(line);
//      }
//      JSONParser parser = new JSONParser();
//      JSONObject object = (JSONObject) parser.parse(stringBuilder.toString());
//      return object;
//    } catch (Exception e){
//      e.printStackTrace();
//    }
//    return new JSONObject();
//  }
}
