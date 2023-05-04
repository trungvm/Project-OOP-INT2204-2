import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.simple.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

@WebServlet(name = "fetch", urlPatterns = "/getData")
public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_KEY = "bf0fca40ef5c3565bb0d7b06f9952fae";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin về thành phố từ request parameter
        String city = request.getParameter("city");

        if (city == null || city.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/farmer_time/weather.jsp");
            return;
        }

        // Gửi yêu cầu tới API và lấy thông tin thời tiết
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = String.format(API_URL, URLEncoder.encode(city, StandardCharsets.UTF_8), API_KEY);

        HttpGet httpGet = new HttpGet(url);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != HttpServletResponse.SC_OK) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/farmer_time/weather.jsp");
            return;
        }

        HttpEntity httpEntity = httpResponse.getEntity();
        String json = EntityUtils.toString(httpEntity);

        Object obj = JSONValue.parse(json);
        JSONObject jsonObject = (JSONObject) obj;

        city = (String) jsonObject.get("name");

        JSONObject main = (JSONObject) jsonObject.get("main");

        double temperature = 27.5;

        if (main.get("temp") instanceof Long) {
            long t = (Long) main.get("temp");
            temperature = (double) t;
        } else if (main.get("temp") instanceof Double) {
            temperature = (Double) main.get("temp");
        }

        long humidity = (Long) main.get("humidity");

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");

        JSONObject firstWeather = (JSONObject) weatherArray.get(0);
        String des = (String) firstWeather.get("description");

        if (httpEntity != null) {
            // Lưu thông tin thời tiết vào request attribute và forward sang JSP
            Weather w = new Weather(city, temperature, des, humidity);
            response.setContentType("application/json");
            request.setAttribute("weather", w);
            request.getRequestDispatcher("/weather.jsp").forward(request, response);
        } else {
            EntityUtils.consume(httpEntity);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            response.sendRedirect("/farmer_time/weather.jsp");
        }
    }
}