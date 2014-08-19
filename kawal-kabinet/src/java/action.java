/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
 * @author khairulanshar
 */
public class action extends HttpServlet {

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
        String form_action = (String) request.getParameter("form_action");
        if (form_action == null) {
            form_action = "";
        }
        PrintWriter out = response.getWriter();
        if (form_action.equalsIgnoreCase("cekauth")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                JSONObject obj1 = (JSONObject) obj;
                String admin = "N";
                String icw = "N";
                String email = obj1.get("email").toString();
                String first_name = obj1.get("first_name").toString();
                String gender = obj1.get("gender").toString();
                String id = obj1.get("id").toString();
                String last_name = obj1.get("last_name").toString();
                String link = obj1.get("link").toString();
                String name = obj1.get("name").toString();
                String verified = obj1.get("verified").toString();

                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key linkKey = KeyFactory.createKey("userTable", "user");
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.

                Filter posisinama = new FilterPredicate("link", FilterOperator.EQUAL, link.toLowerCase());
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("userTable", linkKey).setFilter(posisinama);
                List<Entity> userTables = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
                Date date = new Date();

                if (userTables.isEmpty()) {
                    Entity userTable = new Entity("userTable", linkKey);
                    userTable.setProperty("email", email);
                    userTable.setProperty("first_name", first_name);
                    userTable.setProperty("gender", gender);
                    userTable.setProperty("id", id);
                    userTable.setProperty("last_name", last_name);
                    userTable.setProperty("link", link.toLowerCase());
                    userTable.setProperty("name", name);
                    userTable.setProperty("verified", verified);
                    userTable.setProperty("lastLogin", date);
                    if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                            || id.equalsIgnoreCase("112525777678499279265")
                            || id.equalsIgnoreCase("10152397276159760")
                            || name.equalsIgnoreCase("Khairul Anshar")) {
                        userTable.setProperty("admin", "Y");
                        userTable.setProperty("icw", "Y");
                    } else {
                        userTable.setProperty("admin", admin);
                        userTable.setProperty("icw", "N");
                    }
                    datastore.put(userTable);
                } else {
                    for (Entity userTable : userTables) {
                        admin = userTable.getProperty("admin").toString();
                        try {
                            icw = userTable.getProperty("icw").toString();
                        } catch (Exception e) {
                            userTable.setProperty("icw", "N");
                            icw = "N";
                        }
                        userTable.setProperty("lastLogin", date);
                        datastore.put(userTable);
                    }
                }
                if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                        || id.equalsIgnoreCase("112525777678499279265")
                        || id.equalsIgnoreCase("10152397276159760")
                        || name.equalsIgnoreCase("Khairul Anshar")) {
                    admin = "Y";
                    icw = "Y";
                }
                obj1.put("admin", admin);
                obj1.put("icw", icw);
                session.setAttribute("userAccount", obj1);
                record.put("userAccount", obj1);
            } catch (Exception e) {
            }

            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();

        }
        if (form_action.equalsIgnoreCase("getiframeData")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String src = obj1.get("src").toString();

                final URL url = new URL(src);
                final URLConnection urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.connect();
                final InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader is = new InputStreamReader(inputStream);
                StringBuilder sb1 = new StringBuilder();
                BufferedReader br = new BufferedReader(is);
                String read = br.readLine();
                while (read != null) {
                    sb1.append(read);
                    read = br.readLine();
                }
                record.put("data", sb1.toString());
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("postCommentPosisi")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String dept = obj1.get("dept").toString();
                String star = obj1.get("star").toString();
                String comment = obj1.get("comment").toString();
                String id = userAccount.get("id").toString();
                String name = userAccount.get("name").toString();
                String link = userAccount.get("link").toString();
                postData2(name, dept, "", star, comment, id, "AlasanStarCalonPosisi", "dept", dept, link);

                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("postLikeComment")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String id = obj1.get("id").toString();
                String star = obj1.get("star").toString();

                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String name = userAccount.get("name").toString();
                String link = userAccount.get("link").toString();
                postData11("AlasanStarLike", "id", id, star, link, name);

                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("getLikeComment")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            String idx_ = "";
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                idx_ = obj1.get("id").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                LinkedHashMap record11 = new LinkedHashMap();
                String link_ = "";
                try {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    link_ = userAccount.get("link").toString();
                } catch (Exception e) {
                }
                Key guestbookKey = KeyFactory.createKey("id", idx_);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("AlasanStarLike", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                JSONArray obj11p = new JSONArray();
                JSONArray obj11n = new JSONArray();
                int i = 0;
                int ip = 0;
                int in = 0;
                double total = 0;
                double totalp = 0;
                double totaln = 0;
                for (Entity AlasanStar : pq.asIterable()) {
                    LinkedHashMap record1 = new LinkedHashMap();
                    String date = AlasanStar.getProperty("date").toString();
                    String star = AlasanStar.getProperty("star").toString();
                    String name = AlasanStar.getProperty("user").toString();
                    String link = AlasanStar.getProperty("link").toString();
                    record1.put("date", date);
                    record1.put("star", star);
                    record1.put("name", name);
                    record1.put("link", link);
                    obj11.add(record1);
                    i++;
                    double d = Double.parseDouble(star);
                    total = total + d;
                    if (d >= 0) {
                        obj11p.add(record1);
                        ip++;
                        totalp = totalp + d;
                    } else {
                        obj11n.add(record1);
                        in++;
                        totaln = totaln + d;
                    }
                    if (link_.equalsIgnoreCase(link)) {
                        record11.put("date", date);
                        record11.put("star", star);
                        record11.put("name", name);
                        record11.put("link", link);
                    }
                }
                double avg = total / i;
                if (i == 0) {
                    avg = 0;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                record.put("total", total);
                record.put("totalp", totalp);
                record.put("totaln", totaln);
                record.put("avg", df.format(avg));
                //record.put("AlasanStars", obj11);
                record.put("AlasanStarsp", obj11p);
                record.put("AlasanStarsn", obj11n);
                record.put("AlasanStar", record11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("getMyCommentPosisi")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            String dept = "";
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            LinkedHashMap record1 = new LinkedHashMap();

            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                dept = obj1.get("dept").toString();
                String link_ = "";
                try {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    link_ = userAccount.get("link").toString();
                } catch (Exception e) {
                }
                LinkedHashMap record11 = new LinkedHashMap();
                Key guestbookKey = KeyFactory.createKey("dept", dept);
                Query query = new Query("AlasanStarCalonPosisi", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                JSONArray obj11p = new JSONArray();
                JSONArray obj11n = new JSONArray();
                int i = 0;
                int ip = 0;
                int in = 0;
                double total = 0;
                double totalp = 0;
                double totaln = 0;
                for (Entity AlasanStar : pq.asIterable()) {
                    record1 = new LinkedHashMap();
                    String id = AlasanStar.getProperty("user").toString();
                    //DateTime dateTime = AlasanStar.getProperties().getDateTimeValue();
                    Date time = (Date) AlasanStar.getProperty("date");
                    String date = time.toLocaleString();//AlasanStar.getProperty("date").toString();
                    String star = AlasanStar.getProperty("star").toString();
                    String comment = AlasanStar.getProperty("comment").toString();
                    comment = comment.replaceAll("\n", "<br/>");
                    String name = AlasanStar.getProperty("name").toString();
                    String link = AlasanStar.getProperty("link").toString();
                    String id__ = AlasanStar.getKey().toString();
                    record1.put("id_", id__);
                    record1.put("id", id);
                    record1.put("date", date);
                    record1.put("star", star);
                    record1.put("comment", comment);
                    record1.put("name", name);
                    record1.put("link", link);
                    obj11.add(record1);
                    i++;
                    double d = Double.parseDouble(star);
                    total = total + d;
                    if (d >= 0) {
                        obj11p.add(record1);
                        ip++;
                        totalp = totalp + d;
                    } else {
                        obj11n.add(record1);
                        in++;
                        totaln = totaln + d;
                    }
                    if (link_.equalsIgnoreCase(link)) {
                        record11.put("id_", id__);
                        record11.put("id", id);
                        record11.put("date", date);
                        record11.put("star", star);
                        record11.put("comment", comment);
                        record11.put("name", name);
                        record11.put("link", link);
                    }
                }
                double avg = total / i;
                if (i == 0) {
                    avg = 0;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                record.put("total", total);
                record.put("totalp", totalp);
                record.put("totaln", totaln);
                record.put("avg", df.format(avg));
                //record.put("AlasanStars", obj11);
                record.put("AlasanStarsp", obj11p);
                record.put("AlasanStarsn", obj11n);
                record.put("AlasanStar", record11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("postComment")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String dept = obj1.get("dept").toString();
                String namaCalon = obj1.get("namaCalon").toString();
                String star = obj1.get("star").toString();
                String comment = obj1.get("comment").toString();
                String id = userAccount.get("id").toString();
                String name = userAccount.get("name").toString();
                String link = userAccount.get("link").toString();
                postData2(name, dept, namaCalon, star, comment, id, "AlasanStarCalon", dept, namaCalon, link);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("getMyComment")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            String dept = "";
            String namaCalon = "";
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                dept = obj1.get("dept").toString();
                namaCalon = obj1.get("namaCalon").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                String link_ = "";
                LinkedHashMap record11 = new LinkedHashMap();
                try {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    link_ = userAccount.get("link").toString();
                } catch (Exception e) {
                }
                Key guestbookKey = KeyFactory.createKey(dept, namaCalon);
                Query query = new Query("AlasanStarCalon", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                JSONArray obj11p = new JSONArray();
                JSONArray obj11n = new JSONArray();
                int i = 0;
                int ip = 0;
                int in = 0;
                double total = 0;
                double totalp = 0;
                double totaln = 0;
                for (Entity AlasanStar : pq.asIterable()) {
                    LinkedHashMap record1 = new LinkedHashMap();
                    String id = AlasanStar.getProperty("user").toString();
                    Date time = (Date) AlasanStar.getProperty("date");
                    String date = time.toLocaleString();//AlasanStar.getProperty("date").toString();
                    String star = AlasanStar.getProperty("star").toString();
                    String comment = AlasanStar.getProperty("comment").toString();
                    comment = comment.replaceAll("\n", "<br/>");
                    String name = AlasanStar.getProperty("name").toString();
                    String link = AlasanStar.getProperty("link").toString();
                    String id__ = AlasanStar.getKey().toString();
                    record1.put("id_", id__);
                    record1.put("id", id);
                    record1.put("date", date);
                    record1.put("star", star);
                    record1.put("comment", comment);
                    record1.put("name", name);
                    record1.put("link", link);
                    obj11.add(record1);
                    i++;
                    double d = Double.parseDouble(star);
                    total = total + d;
                    if (d >= 0) {
                        obj11p.add(record1);
                        ip++;
                        totalp = totalp + d;
                    } else {
                        obj11n.add(record1);
                        in++;
                        totaln = totaln + d;
                    }
                    if (link_.equalsIgnoreCase(link)) {
                        record11.put("id_", id__);
                        record11.put("id", id);
                        record11.put("date", date);
                        record11.put("star", star);
                        record11.put("comment", comment);
                        record11.put("name", name);
                        record11.put("link", link);
                    }
                }
                double avg = total / i;
                if (i == 0) {
                    avg = 0;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                record.put("total", total);
                record.put("totalp", totalp);
                record.put("totaln", totaln);
                record.put("avg", df.format(avg));
                record.put("AlasanStarsp", obj11p);
                record.put("AlasanStarsn", obj11n);
                record.put("AlasanStar", record11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("getAlasanStarCalon")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String dept = obj1.get("dept").toString();
                String namaCalon = obj1.get("namaCalon").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key guestbookKey = KeyFactory.createKey(dept, namaCalon);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("AlasanStarCalon", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                int i = 0;
                double total = 0;
                for (Entity AlasanStar : pq.asIterable()) {
                    LinkedHashMap record1 = new LinkedHashMap();
                    String id = AlasanStar.getProperty("user").toString();
                    String date = AlasanStar.getProperty("date").toString();
                    String star = AlasanStar.getProperty("star").toString();
                    String comment = AlasanStar.getProperty("comment").toString();
                    comment = comment.replaceAll("\n", "<br/>");
                    String name = AlasanStar.getProperty("name").toString();
                    String link = AlasanStar.getProperty("link").toString();
                    String id__ = AlasanStar.getKey().toString();
                    record1.put("id_", id__);
                    record1.put("id", id);
                    record1.put("date", date);
                    record1.put("star", star);
                    record1.put("comment", comment);
                    record1.put("name", name);
                    record1.put("link", link);
                    obj11.add(record1);
                    i++;
                    double d = Double.parseDouble(star);
                    total = total + d;
                }
                double avg = total / i;
                if (i == 0) {
                    avg = 0;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                record.put("total", total);
                record.put("avg", df.format(avg));
                record.put("AlasanStars", obj11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }

        if (form_action.equalsIgnoreCase("postUsulan")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String dept = obj1.get("dept").toString();
                String usulan = obj1.get("usulan").toString();

                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id = userAccount.get("id").toString();
                String name = userAccount.get("name").toString();
                String email = userAccount.get("email").toString();
                String link = userAccount.get("link").toString();
                Key usulanCalonKey = KeyFactory.createKey("dept", dept);
                Date date = new Date();
                Entity usulanCalon = new Entity("usulanCalon", usulanCalonKey);
                usulanCalon.setProperty("user", id);
                usulanCalon.setProperty("name", name);
                usulanCalon.setProperty("email", email);
                usulanCalon.setProperty("link", link);
                usulanCalon.setProperty("date", date);
                usulanCalon.setProperty("usulan", usulan);
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                datastore.put(usulanCalon);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }

        if (form_action.equalsIgnoreCase("getSet1")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String input = obj1.get("input").toString();
                String type = obj1.get("type").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key typeKey = KeyFactory.createKey("posisi", type.toLowerCase().replaceAll(" ", ""));
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("posisi", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                String id = "";
                try {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    id = userAccount.get("id").toString();
                } catch (Exception ex1) {
                }
                for (Entity typeEntity : pq.asIterable()) {
                    String reviewed = typeEntity.getProperty("reviewed").toString();
                    if (reviewed.equalsIgnoreCase("Y")) {
                        LinkedHashMap record1 = new LinkedHashMap();
                        String posisi = typeEntity.getProperty("posisi").toString();
                        String nama = typeEntity.getProperty("nama").toString();
                        String link = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        record1.put("posisi", posisi);
                        record1.put("nama", nama);
                        record1.put("link", link);
                        record1.put("date", date);
                        String detail1 = "";
                        try {
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            detail1 = detail0.getValue();
                        } catch (Exception e) {
                            detail1 = "";
                        }
                        record1.put("detail", detail1);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id)) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String posisi = typeEntity.getProperty("posisi").toString();
                            String nama = typeEntity.getProperty("nama").toString();
                            String link = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("posisi", posisi);
                            record1.put("nama", nama);
                            record1.put("link", link);
                            record1.put("date", date);
                            String detail1 = "";
                            try {
                                Text detail0 = (Text) typeEntity.getProperty("detail");
                                detail1 = detail0.getValue();
                            } catch (Exception e) {
                                detail1 = "";
                            }
                            record1.put("detail", detail1);
                            obj11.add(record1);
                        }
                    }
                }
                record.put("records", obj11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("setSet1")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {

                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id = userAccount.get("id").toString();
                String nama = userAccount.get("name").toString();
                String email = userAccount.get("email").toString();
                String link = userAccount.get("link").toString();
                String admin = userAccount.get("admin").toString();

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String input = obj1.get("input").toString();
                String type = obj1.get("type").toString();
                String value = obj1.get("value").toString();
                String detail = obj1.get("value1").toString();
                Key typeKey = KeyFactory.createKey("posisi", type.toLowerCase().replaceAll(" ", ""));
                Filter posisinama = new FilterPredicate("posisi", FilterOperator.EQUAL, value);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("posisi", typeKey).setFilter(posisinama);
                //Query query = new Query("posisi", typeKey);//.addSort("date", Query.SortDirection.DESCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                PreparedQuery pq = datastore.prepare(query);
                boolean found = pq.asIterable().iterator().hasNext();

                if (found) {
                    if (admin.equalsIgnoreCase("Y")) {
                        for (Entity psosisiEntity : pq.asList(FetchOptions.Builder.withLimit(1))) {
                            Date date = new Date();
                            psosisiEntity.setProperty("date", date);
                            psosisiEntity.setProperty("detail", new Text(detail));
                            datastore.put(psosisiEntity);
                        }
                    }
                }

                if (!found) {

                    Date date = new Date();
                    Entity psosisiEntity = new Entity("posisi", typeKey);
                    psosisiEntity.setProperty("user", id);
                    psosisiEntity.setProperty("link", link);
                    psosisiEntity.setProperty("nama", nama);
                    psosisiEntity.setProperty("email", email);
                    psosisiEntity.setProperty("date", date);
                    psosisiEntity.setProperty("posisi", value);
                    psosisiEntity.setProperty("detail", new Text(detail));
                    if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                            || id.equalsIgnoreCase("112525777678499279265")
                            || id.equalsIgnoreCase("10152397276159760")
                            || nama.equalsIgnoreCase("Khairul Anshar")
                            || admin.equalsIgnoreCase("Y")) {
                        psosisiEntity.setProperty("reviewed", "Y");
                        psosisiEntity.setProperty("nama", "Kawal Menteri");
                        psosisiEntity.setProperty("link", "https://www.facebook.com/KawalMenteri");
                    } else {
                        psosisiEntity.setProperty("reviewed", "N");
                    }
                    datastore.put(psosisiEntity);
                }

                query = new Query("posisi", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();

                for (Entity typeEntity : pq.asIterable()) {
                    String reviewed = typeEntity.getProperty("reviewed").toString();
                    if (reviewed.equalsIgnoreCase("Y")) {
                        LinkedHashMap record1 = new LinkedHashMap();
                        String posisi = typeEntity.getProperty("posisi").toString();
                        String nama1 = typeEntity.getProperty("nama").toString();
                        String link1 = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        record1.put("posisi", posisi);
                        record1.put("nama", nama1);
                        record1.put("link", link1);
                        record1.put("date", date);
                        String detail1 = "";
                        try {
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            detail1 = detail0.getValue();
                        } catch (Exception e) {
                            detail1 = "";
                        }
                        record1.put("detail", detail1);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id) || (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                                || id.equalsIgnoreCase("112525777678499279265")
                                || id.equalsIgnoreCase("10152397276159760")
                                || nama.equalsIgnoreCase("Khairul Anshar")
                                || admin.equalsIgnoreCase("Y"))) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String posisi = typeEntity.getProperty("posisi").toString();
                            String nama1 = typeEntity.getProperty("nama").toString();
                            String link1 = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("posisi", posisi);
                            record1.put("nama", nama1);
                            record1.put("link", link1);
                            record1.put("date", date);
                            String detail1 = "";
                            try {
                                Text detail0 = (Text) typeEntity.getProperty("detail");
                                detail1 = detail0.getValue();
                            } catch (Exception e) {
                                detail1 = "";
                            }
                            record1.put("detail", detail1);
                            obj11.add(record1);
                        }
                    }
                }

                record.put("records", obj11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }

        if (form_action.equalsIgnoreCase("getSet2")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                String input = obj1.get("input").toString();
                String input0 = obj1.get("input0").toString();
                String type0 = obj1.get("type0").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key typeKey = KeyFactory.createKey("kandidat" + type0, input);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("kandidat", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                PreparedQuery pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
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
                for (Entity typeEntity : pq.asIterable()) {
                    String reviewed = typeEntity.getProperty("reviewed").toString();
                    if (reviewed.equalsIgnoreCase("Y")) {
                        LinkedHashMap record1 = new LinkedHashMap();
                        String kandidat = typeEntity.getProperty("kandidat").toString();
                        String desc = typeEntity.getProperty("desc").toString();
                        Text detail0 = (Text) typeEntity.getProperty("detail");
                        String detail = detail0.getValue();
                        String nama1 = typeEntity.getProperty("nama").toString();
                        String link1 = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        String icwcomment = "";
                        try {
                            icwcomment = typeEntity.getProperty("icwcomment").toString();
                        } catch (Exception e) {
                            icwcomment = "";
                        }
                        record1.put("key", "kandidat" + type0);
                        record1.put("val", input);
                        record1.put("kandidat", kandidat);
                        record1.put("desc", desc);
                        record1.put("detail", detail);
                        record1.put("nama", nama1);
                        record1.put("link", link1);
                        record1.put("date", date);
                        record1.put("icwcomment", icwcomment);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id) || (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                                || id.equalsIgnoreCase("112525777678499279265")
                                || id.equalsIgnoreCase("10152397276159760")
                                || nama.equalsIgnoreCase("Khairul Anshar")
                                || admin.equalsIgnoreCase("Y"))) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String kandidat = typeEntity.getProperty("kandidat").toString();
                            String desc = typeEntity.getProperty("desc").toString();
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            String detail = detail0.getValue();
                            String nama1 = typeEntity.getProperty("nama").toString();
                            String link1 = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("key", "kandidat" + type0);
                            record1.put("val", input);
                            record1.put("kandidat", kandidat);
                            record1.put("desc", desc);
                            record1.put("detail", detail);
                            record1.put("nama", nama1);
                            record1.put("link", link1);
                            record1.put("date", date);
                            String icwcomment = "";

                            if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                                    || id.equalsIgnoreCase("112525777678499279265")
                                    || id.equalsIgnoreCase("10152397276159760")
                                    || nama.equalsIgnoreCase("Khairul Anshar")
                                    || admin.equalsIgnoreCase("Y")
                                    || icw.equalsIgnoreCase("Y")) {
                                try {
                                    icwcomment = typeEntity.getProperty("icwcomment").toString();
                                } catch (Exception e) {
                                    icwcomment = "";
                                }
                            }
                            record1.put("icwcomment", icwcomment);
                            obj11.add(record1);
                        }
                    }
                }
                record.put("records", obj11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("setIcwComment")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String icw = userAccount.get("icw").toString();
                if (icw.equalsIgnoreCase("N")) {

                } else {
                    BufferedReader reader = request.getReader();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    Object obj = JSONValue.parse(sb.toString());
                    //JSONArray records = (JSONArray) obj;
                    JSONObject obj1 = (JSONObject) obj;

                    String input = obj1.get("input").toString();
                    String input0 = obj1.get("input0").toString();
                    String type0 = obj1.get("type0").toString();
                    String value = obj1.get("value").toString();
                    String menteri = obj1.get("menteri").toString();
                    String kandidat = obj1.get("kandidat").toString();
                    Key typeKey = KeyFactory.createKey("kandidat" + type0, input);
                    // Run an ancestor query to ensure we see the most up-to-date
                    // view of the Greetings belonging to the selected Guestbook.

                    Filter namaKandidat = new FilterPredicate("kandidat", FilterOperator.EQUAL, kandidat);
                    // Run an ancestor query to ensure we see the most up-to-date
                    // view of the Greetings belonging to the selected Guestbook.
                    Query query = new Query("kandidat", typeKey).setFilter(namaKandidat);
                    //Query query = new Query("posisi", typeKey);//.addSort("date", Query.SortDirection.DESCENDING);
                    //List<Entity> AlasanStars = datastore.prepare(query);
                    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

                    for (Entity psosisiEntity : datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1))) {
                        psosisiEntity.setProperty("icwcomment", value);
                        datastore.put(psosisiEntity);
                    }

                }
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
        if (form_action.equalsIgnoreCase("setSet2")) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            LinkedHashMap record = new LinkedHashMap();
            try {
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id = userAccount.get("id").toString();
                String nama = userAccount.get("name").toString();
                String email = userAccount.get("email").toString();
                String link = userAccount.get("link").toString();
                String admin = userAccount.get("admin").toString();
                String icw = userAccount.get("icw").toString();

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;

                String input = obj1.get("input").toString();
                String input0 = obj1.get("input0").toString();
                String type0 = obj1.get("type0").toString();
                String value = obj1.get("value").toString();
                String value1 = obj1.get("value1").toString();
                String value2 = obj1.get("value2").toString();
                String menteri = obj1.get("menteri").toString();
                Key typeKey = KeyFactory.createKey("kandidat" + type0, input);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.

                Filter namaKandidat = new FilterPredicate("kandidat", FilterOperator.EQUAL, value);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("kandidat", typeKey).setFilter(namaKandidat);
                //Query query = new Query("posisi", typeKey);//.addSort("date", Query.SortDirection.DESCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                PreparedQuery pq = datastore.prepare(query);
                boolean found = pq.asIterable().iterator().hasNext();

                if (found) {
                    if (admin.equalsIgnoreCase("Y")) {
                        for (Entity psosisiEntity : pq.asList(FetchOptions.Builder.withLimit(1))) {
                            Date date = new Date();
                            psosisiEntity.setProperty("date", date);
                            psosisiEntity.setProperty("detail", new Text(value2));
                            datastore.put(psosisiEntity);
                        }
                    }
                }

                if (!found) {

                    Date date = new Date();
                    Entity psosisiEntity = new Entity("kandidat", typeKey);
                    psosisiEntity.setProperty("user", id);
                    psosisiEntity.setProperty("link", link);
                    psosisiEntity.setProperty("nama", nama);
                    psosisiEntity.setProperty("email", email);
                    psosisiEntity.setProperty("date", date);
                    psosisiEntity.setProperty("kandidat", value);
                    psosisiEntity.setProperty("desc", value1);
                    psosisiEntity.setProperty("posisi", menteri);
                    psosisiEntity.setProperty("detail", new Text(value2));
                    psosisiEntity.setProperty("icwcomment", "");

                    if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                            || id.equalsIgnoreCase("112525777678499279265")
                            || id.equalsIgnoreCase("10152397276159760")
                            || nama.equalsIgnoreCase("Khairul Anshar")
                            || admin.equalsIgnoreCase("Y")) {
                        psosisiEntity.setProperty("reviewed", "Y");
                        psosisiEntity.setProperty("nama", "Kawal Menteri");
                        psosisiEntity.setProperty("link", "https://www.facebook.com/KawalMenteri");
                    } else {
                        psosisiEntity.setProperty("reviewed", "N");
                    }
                    datastore.put(psosisiEntity);
                }

                query = new Query("kandidat", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();

                for (Entity typeEntity : pq.asIterable()) {
                    String reviewed = typeEntity.getProperty("reviewed").toString();
                    if (reviewed.equalsIgnoreCase("Y")) {
                        LinkedHashMap record1 = new LinkedHashMap();
                        String kandidat = typeEntity.getProperty("kandidat").toString();
                        String desc = typeEntity.getProperty("desc").toString();
                        Text detail0 = (Text) typeEntity.getProperty("detail");
                        String detail = detail0.getValue();
                        String nama1 = typeEntity.getProperty("nama").toString();
                        String link1 = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        String icwcomment = "";
                        try {
                            icwcomment = typeEntity.getProperty("icwcomment").toString();
                        } catch (Exception e) {
                            icwcomment = "";
                        }
                        record1.put("key", "kandidat" + type0);
                        record1.put("val", input);
                        record1.put("kandidat", kandidat);
                        record1.put("desc", desc);
                        record1.put("detail", detail);
                        record1.put("nama", nama1);
                        record1.put("link", link1);
                        record1.put("date", date);
                        record1.put("icwcomment", icwcomment);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id) || (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                                || id.equalsIgnoreCase("112525777678499279265")
                                || id.equalsIgnoreCase("10152397276159760")
                                || nama.equalsIgnoreCase("Khairul Anshar")
                                || admin.equalsIgnoreCase("Y"))) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String kandidat = typeEntity.getProperty("kandidat").toString();
                            String desc = typeEntity.getProperty("desc").toString();
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            String detail = detail0.getValue();
                            String nama1 = typeEntity.getProperty("nama").toString();
                            String link1 = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("key", "kandidat" + type0);
                            record1.put("val", input);
                            record1.put("kandidat", kandidat);
                            record1.put("desc", desc);
                            record1.put("detail", detail);
                            record1.put("nama", nama1);
                            record1.put("link", link1);
                            record1.put("date", date);
                            String icwcomment = "";

                            if (email.equalsIgnoreCase("khairul.anshar@gmail.com")
                                    || id.equalsIgnoreCase("112525777678499279265")
                                    || id.equalsIgnoreCase("10152397276159760")
                                    || nama.equalsIgnoreCase("Khairul Anshar")
                                    || admin.equalsIgnoreCase("Y")
                                    || icw.equalsIgnoreCase("Y")) {
                                try {
                                    icwcomment = typeEntity.getProperty("icwcomment").toString();
                                } catch (Exception e) {
                                    icwcomment = "";
                                }
                            }
                            record1.put("icwcomment", icwcomment);
                            obj11.add(record1);
                        }
                    }
                }

                record.put("records", obj11);
                record.put("status", "OK");
            } catch (Exception e) {
                record.put("status", "error");
                record.put("errormsg", e.toString());
            }
            response.setContentType("text/html;charset=UTF-8");
            out.print(JSONValue.toJSONString(record));
            out.flush();
        }
    }

    /*private void postData(String name, String dept, String namaCalon, String star, String comment, String id, String table, String key, String keyVal, String link) {

     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Key guestbookKey = KeyFactory.createKey(key, keyVal);
     // Run an ancestor query to ensure we see the most up-to-date
     // view of the Greetings belonging to the selected Guestbook.
     Query query = new Query(table, guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
     List<Entity> AlasanStars = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
     Date date = new Date();

     if (AlasanStars.isEmpty()) {
     Entity AlasanStar = new Entity(table, guestbookKey);
     AlasanStar.setProperty("user", id);
     AlasanStar.setProperty("date", date);
     AlasanStar.setProperty("dept", dept);
     AlasanStar.setProperty("star", star);
     AlasanStar.setProperty("comment", comment);
     AlasanStar.setProperty("name", name);
     AlasanStar.setProperty("namaCalon", namaCalon);
     AlasanStar.setProperty("link", link);
     datastore.put(AlasanStar);
     } else {
     for (Entity AlasanStar : AlasanStars) {
     AlasanStar.setProperty("user", id);
     AlasanStar.setProperty("date", date);
     AlasanStar.setProperty("dept", dept);
     AlasanStar.setProperty("star", star);
     AlasanStar.setProperty("comment", comment);
     AlasanStar.setProperty("name", name);
     AlasanStar.setProperty("namaCalon", namaCalon);
     AlasanStar.setProperty("link", link);
     datastore.put(AlasanStar);
     }

     }
     }*/
    //("AlasanStarLike", "id", id, star, link, name)
    private void postData1(String table, String key, String keyVal, String val, String link, String user) {
        System.out.println(keyVal);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key guestbookKey = KeyFactory.createKey(key, keyVal);
        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        Query query = new Query(table, guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> AlasanStars = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
        Date date = new Date();

        if (AlasanStars.isEmpty()) {
            Entity AlasanStar = new Entity(table, guestbookKey);
            AlasanStar.setProperty("user", user);
            AlasanStar.setProperty("date", date);
            AlasanStar.setProperty("star", val);
            AlasanStar.setProperty("link", link);
            datastore.put(AlasanStar);
        } /*else {
         for (Entity AlasanStar : AlasanStars) {
         AlasanStar.setProperty("user", user);
         AlasanStar.setProperty("date", date);
         AlasanStar.setProperty("star", val);
         AlasanStar.setProperty("link", link);
         datastore.put(AlasanStar);
         }

         }*/

    }
    
     private void postData11(String table, String key, String keyVal, String val, String link, String user) {
        System.out.println(keyVal);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key guestbookKey = KeyFactory.createKey(key, keyVal);
        Filter linkFilter = new FilterPredicate("link", FilterOperator.EQUAL, link);
        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
        Query query = new Query(table, guestbookKey).setFilter(linkFilter).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> AlasanStars = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
        Date date = new Date();

        if (AlasanStars.isEmpty()) {
            Entity AlasanStar = new Entity(table, guestbookKey);
            AlasanStar.setProperty("user", user);
            AlasanStar.setProperty("date", date);
            AlasanStar.setProperty("star", val);
            AlasanStar.setProperty("link", link);
            datastore.put(AlasanStar);
        } /*else {
         for (Entity AlasanStar : AlasanStars) {
         AlasanStar.setProperty("user", user);
         AlasanStar.setProperty("date", date);
         AlasanStar.setProperty("star", val);
         AlasanStar.setProperty("link", link);
         datastore.put(AlasanStar);
         }

         }*/

    }

    private void postData2(String name, String dept, String namaCalon, String star, String comment, String id, String table, String key, String keyVal, String link) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter linkFilter = new FilterPredicate("link", FilterOperator.EQUAL, link);
        Key AlasanStarCalonKey = KeyFactory.createKey(key, keyVal);
        Query query = new Query(table, AlasanStarCalonKey).setFilter(linkFilter);
        List<Entity> AlasanStars = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
        if (AlasanStars.isEmpty()) {
            Date date = new Date();
            Entity AlasanStar = new Entity(table, AlasanStarCalonKey);
            AlasanStar.setProperty("user", id);
            AlasanStar.setProperty("date", date);
            AlasanStar.setProperty("dept", dept);
            AlasanStar.setProperty("star", star);
            AlasanStar.setProperty("comment", comment);
            AlasanStar.setProperty("name", name);
            AlasanStar.setProperty("namaCalon", namaCalon);
            AlasanStar.setProperty("link", link);
            datastore.put(AlasanStar);
        }
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
