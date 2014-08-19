/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author 403036
 */
public class admin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json;charset=UTF-8");
        String id = "";
        String nama = "";
        String email = "";
        String link = "";
        String admin = "";
        String icw = "";
        try {
            JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
            id = userAccount.get("id").toString();
            nama = userAccount.get("name").toString();
            email = userAccount.get("email").toString();
            link = userAccount.get("link").toString();
            admin = userAccount.get("admin").toString();
            icw = userAccount.get("icw").toString();
        } catch (Exception ex1) {
        }
        if (admin.equalsIgnoreCase("Y")) {
            try (PrintWriter out = response.getWriter()) {
                Vector dataPosisi = new Vector();
                dataPosisi.addElement("Menteri");
                dataPosisi.addElement("Pejabat Setingkat Menteri");
                dataPosisi.addElement("Duta Besar");
                JSONArray obj1 = new JSONArray();
                for (int i = 0; i < dataPosisi.size(); i++) {
                    String FilterKey = "", FilterValue = "";
                    String key = "posisi", keyValue = dataPosisi.get(i).toString().toLowerCase().replaceAll(" ", "");
                    String table = "posisi";
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                    //Filter linkFilter = new FilterPredicate(FilterKey, FilterOperator.EQUAL, FilterValue);
                    Key AlasanStarCalonKey = KeyFactory.createKey(key, keyValue);
                    Query query = new Query(table, AlasanStarCalonKey);
                    PreparedQuery pq = datastore.prepare(query);
                    JSONArray obj11 = new JSONArray();
                    getData(obj11, table, key, keyValue, dataPosisi.get(i).toString(), Query.SortDirection.ASCENDING);
                    LinkedHashMap record = new LinkedHashMap();
                    record.put("posisi", keyValue);
                    record.put("nama", dataPosisi.get(i).toString());
                    record.put("child", obj11);
                    obj1.add(record);
                }
                out.print(JSONValue.toJSONString(obj1));
                out.flush();
            }
        }
    }

    private void getData(JSONArray obj11, String table, String key, String keyValue, String Parent, SortDirection sortDirection) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        //Filter linkFilter = new FilterPredicate(FilterKey, FilterOperator.EQUAL, FilterValue);
        Key AlasanStarCalonKey = KeyFactory.createKey(key, keyValue);
        Query query = new Query(table, AlasanStarCalonKey).addSort("date", sortDirection);
        PreparedQuery pq = datastore.prepare(query);
        for (Entity typeEntity : pq.asIterable()) {
            LinkedHashMap record1 = new LinkedHashMap();
            if (table.equalsIgnoreCase("posisi")) {
                String posisi = typeEntity.getProperty("posisi").toString();
                String nama = typeEntity.getProperty("nama").toString();
                String link = typeEntity.getProperty("link").toString();
                Date time = (Date) typeEntity.getProperty("date");
                String date = typeEntity.getProperty("date").toString();//getGMT7(time);
                String reviewed = typeEntity.getProperty("reviewed").toString();
                record1.put("key", key);
                record1.put("val", keyValue);
                record1.put("posisi", posisi);
                record1.put("nama", nama);
                record1.put("link", link);
                record1.put("date", date);
                record1.put("reviewed", reviewed);
                String detail1 = "";
                try {
                    Text detail0 = (Text) typeEntity.getProperty("detail");
                    detail1 = detail0.getValue();
                } catch (Exception e) {
                    detail1 = "";
                }
                record1.put("detail", detail1);
                JSONArray obj11_ = new JSONArray();
                getData(obj11_, "AlasanStarCalonPosisi", "dept", replace(posisi), "", Query.SortDirection.ASCENDING);
                record1.put("childKomentar", obj11_);

                obj11_ = new JSONArray();
                getData(obj11_, "kandidat", "kandidat" + Parent, replace(posisi), "", Query.SortDirection.ASCENDING);
                record1.put("child", obj11_);

            }
            if (table.equalsIgnoreCase("kandidat")) {
                String reviewed = typeEntity.getProperty("reviewed").toString();
                String kandidat = typeEntity.getProperty("kandidat").toString();
                String desc = typeEntity.getProperty("desc").toString();
                Text detail0 = (Text) typeEntity.getProperty("detail");
                String detail = detail0.getValue();
                String nama1 = typeEntity.getProperty("nama").toString();
                String link1 = typeEntity.getProperty("link").toString();
                Date time = (Date) typeEntity.getProperty("date");
                String date = typeEntity.getProperty("date").toString();//getGMT7(time);
                String icwcomment = "";
                try {
                    icwcomment = typeEntity.getProperty("icwcomment").toString();
                } catch (Exception e) {
                    icwcomment = "";
                }
                record1.put("key", key);
                record1.put("val", keyValue);
                record1.put("kandidat", kandidat);
                record1.put("desc", desc);
                record1.put("detail", detail);
                record1.put("nama", nama1);
                record1.put("link", link1);
                record1.put("date", date);
                record1.put("icwcomment", icwcomment);
                record1.put("reviewed", reviewed);

                JSONArray obj11_ = new JSONArray();
                getData(obj11_, "AlasanStarCalon", keyValue, replace(kandidat), "", Query.SortDirection.ASCENDING);
                record1.put("childKomentar", obj11_);

            }
            if (table.equalsIgnoreCase("AlasanStarCalonPosisi") || table.equalsIgnoreCase("AlasanStarCalon")) {
                String user = typeEntity.getProperty("user").toString();
                //DateTime dateTime = AlasanStar.getProperties().getDateTimeValue();
                Date time = (Date) typeEntity.getProperty("date");
                String date = typeEntity.getProperty("date").toString();//getGMT7(time);
                String star = typeEntity.getProperty("star").toString();
                String comment = typeEntity.getProperty("comment").toString();
                String name = typeEntity.getProperty("name").toString();
                String link = typeEntity.getProperty("link").toString();
                record1.put("key", key);
                record1.put("val", keyValue);
                record1.put("date", date);
                record1.put("star", star);
                record1.put("comment", comment);
                record1.put("name", name);
                record1.put("link", link);
            }
            obj11.add(record1);
            typeEntity.setProperty("imported", "Y");
            datastore.put(typeEntity);
        }
    }

    private String getGMT7(Date date) {
        DateFormat gmtFormat = new SimpleDateFormat();
        TimeZone gmtTime = TimeZone.getTimeZone("GMT+7");
        gmtFormat.setTimeZone(gmtTime);
        return date.toString();
    }

    private String replace(String inp) {
        String val = inp.replace(" ", "").replace(",", "").replace(".", "").replace("`", "").replace("~", "").replace("!", "").replace("@", "").replace("#", "").replace("$", "").replace("%", "").replace("^", "").replace("&", "").replace("*", "").replace("(", "").replace(")", "").replace("+", "").replace("|", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace(":", "").replace(";", "").replace("\"", "").replace("'", "").replace("?", "").replace("/", "");
        return val;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
