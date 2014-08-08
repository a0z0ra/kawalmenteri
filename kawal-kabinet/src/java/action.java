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
import com.google.appengine.api.users.User;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                session.setAttribute("userAccount", obj1);
                record.put("userAccount", obj1);
            } catch (Exception e) {
            }
            try {
                InputStream feedStream = new FileInputStream("data/dept.json");
                InputStreamReader is = new InputStreamReader(feedStream);
                StringBuilder sb1 = new StringBuilder();
                BufferedReader br = new BufferedReader(is);
                String read = br.readLine();
                while (read != null) {
                    sb1.append(read);
                    read = br.readLine();
                }
                Object obj0 = JSONValue.parse(sb1.toString());
                JSONArray obj11 = (JSONArray) obj0;
                record.put("dept", obj11);
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
                postData(name, dept, namaCalon, star, comment, id, "AlasanStar", dept + namaCalon, id);
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
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Object obj = JSONValue.parse(sb.toString());
                //JSONArray records = (JSONArray) obj;
                JSONObject obj1 = (JSONObject) obj;
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id_ = userAccount.get("id").toString();
                String dept = obj1.get("dept").toString();
                String namaCalon = obj1.get("namaCalon").toString();
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                Key guestbookKey = KeyFactory.createKey(dept + namaCalon, id_);
                // Run an ancestor query to ensure we see the most up-to-date
                // view of the Greetings belonging to the selected Guestbook.
                Query query = new Query("AlasanStar", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
                //List<Entity> AlasanStars = datastore.prepare(query);
                PreparedQuery pq = datastore.prepare(query);
                LinkedHashMap record1 = new LinkedHashMap();
                for (Entity AlasanStar : pq.asIterable()) {
                    String id = AlasanStar.getProperty("user").toString();
                    String date = AlasanStar.getProperty("date").toString();
                    String star = AlasanStar.getProperty("star").toString();
                    String comment = AlasanStar.getProperty("comment").toString();
                    String name = AlasanStar.getProperty("name").toString();
                    record1.put("id", id);
                    record1.put("date", date);
                    record1.put("star", star);
                    record1.put("comment", comment);
                    record1.put("name", name);
                }
                record.put("AlasanStar", record1);
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
                if (!found) {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    String id = userAccount.get("id").toString();
                    String nama = userAccount.get("name").toString();
                    String email = userAccount.get("email").toString();
                    String link = userAccount.get("link").toString();
                    Date date = new Date();
                    Entity psosisiEntity = new Entity("posisi", typeKey);
                    psosisiEntity.setProperty("user", id);
                    psosisiEntity.setProperty("link", link);
                    psosisiEntity.setProperty("nama", nama);
                    psosisiEntity.setProperty("email", email);
                    psosisiEntity.setProperty("date", date);
                    psosisiEntity.setProperty("posisi", value);
                    psosisiEntity.setProperty("detail", new Text(detail));
                    if (email.equalsIgnoreCase("khairul.anshar@gmail.com")) {
                        psosisiEntity.setProperty("reviewed", "Y");
                    } else {
                        psosisiEntity.setProperty("reviewed", "N");
                    }
                    datastore.put(psosisiEntity);
                }

                query = new Query("posisi", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id = userAccount.get("id").toString();
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
                try {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    id = userAccount.get("id").toString();
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
                        String nama = typeEntity.getProperty("nama").toString();
                        String link = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        record1.put("kandidat", kandidat);
                        record1.put("desc", desc);
                        record1.put("detail", detail);
                        record1.put("nama", nama);
                        record1.put("link", link);
                        record1.put("date", date);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id)) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String kandidat = typeEntity.getProperty("kandidat").toString();
                            String desc = typeEntity.getProperty("desc").toString();
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            String detail = detail0.getValue();
                            String nama = typeEntity.getProperty("nama").toString();
                            String link = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("kandidat", kandidat);
                            record1.put("desc", desc);
                            record1.put("detail", detail);
                            record1.put("nama", nama);
                            record1.put("link", link);
                            record1.put("date", date);
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
        if (form_action.equalsIgnoreCase("setSet2")) {
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
                if (!found) {
                    JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                    String id = userAccount.get("id").toString();
                    String nama = userAccount.get("name").toString();
                    String email = userAccount.get("email").toString();
                    String link = userAccount.get("link").toString();
                    Date date = new Date();
                    Entity psosisiEntity = new Entity("kandidat", typeKey);
                    psosisiEntity.setProperty("user", id);
                    psosisiEntity.setProperty("link", link);
                    psosisiEntity.setProperty("nama", nama);
                    psosisiEntity.setProperty("email", email);
                    psosisiEntity.setProperty("date", date);
                    psosisiEntity.setProperty("kandidat", value);
                    psosisiEntity.setProperty("desc", value1);
                    psosisiEntity.setProperty("menteri", menteri);
                    psosisiEntity.setProperty("detail", new Text(value2));
                    if (email.equalsIgnoreCase("khairul.anshar@gmail.com")) {
                        psosisiEntity.setProperty("reviewed", "Y");
                    } else {
                        psosisiEntity.setProperty("reviewed", "N");
                    }
                    datastore.put(psosisiEntity);
                }

                query = new Query("kandidat", typeKey).addSort("date", Query.SortDirection.ASCENDING);
                pq = datastore.prepare(query);
                JSONArray obj11 = new JSONArray();
                JSONObject userAccount = (JSONObject) session.getAttribute("userAccount");
                String id = userAccount.get("id").toString();
                for (Entity typeEntity : pq.asIterable()) {
                    String reviewed = typeEntity.getProperty("reviewed").toString();
                    if (reviewed.equalsIgnoreCase("Y")) {
                        LinkedHashMap record1 = new LinkedHashMap();
                        String kandidat = typeEntity.getProperty("kandidat").toString();
                        String desc = typeEntity.getProperty("desc").toString();
                        Text detail0 = (Text) typeEntity.getProperty("detail");
                        String detail = detail0.getValue();
                        String nama = typeEntity.getProperty("nama").toString();
                        String link = typeEntity.getProperty("link").toString();
                        String date = typeEntity.getProperty("date").toString();
                        record1.put("kandidat", kandidat);
                        record1.put("desc", desc);
                        record1.put("detail", detail);
                        record1.put("nama", nama);
                        record1.put("link", link);
                        record1.put("date", date);
                        obj11.add(record1);
                    } else {
                        String user = typeEntity.getProperty("user").toString();
                        if (user.equalsIgnoreCase(id)) {
                            LinkedHashMap record1 = new LinkedHashMap();
                            String kandidat = typeEntity.getProperty("kandidat").toString();
                            String desc = typeEntity.getProperty("desc").toString();
                            Text detail0 = (Text) typeEntity.getProperty("detail");
                            String detail = detail0.getValue();
                            String nama = typeEntity.getProperty("nama").toString();
                            String link = typeEntity.getProperty("link").toString();
                            String date = typeEntity.getProperty("date").toString();
                            record1.put("kandidat", kandidat);
                            record1.put("desc", desc);
                            record1.put("detail", detail);
                            record1.put("nama", nama);
                            record1.put("link", link);
                            record1.put("date", date);
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

    private void postData(String name, String dept, String namaCalon, String star, String comment, String id, String table, String key, String keyVal) {

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
            datastore.put(AlasanStar);
        } else {
            for (Entity AlasanStar : AlasanStars) {
                AlasanStar.setProperty("user", id);
                AlasanStar.setProperty("date", date);
                AlasanStar.setProperty("dept", dept);
                AlasanStar.setProperty("star", star);
                AlasanStar.setProperty("comment", comment);
                AlasanStar.setProperty("name", name);
                datastore.put(AlasanStar);
            }

        }
    }

    private void postData2(String name, String dept, String namaCalon, String star, String comment, String id, String table, String key, String keyVal, String link) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key guestbookKey = KeyFactory.createKey(key, keyVal);
        Date date = new Date();
        Entity AlasanStar = new Entity(table, guestbookKey);
        AlasanStar.setProperty("user", id);
        AlasanStar.setProperty("date", date);
        AlasanStar.setProperty("dept", dept);
        AlasanStar.setProperty("star", star);
        AlasanStar.setProperty("comment", comment);
        AlasanStar.setProperty("name", name);
        AlasanStar.setProperty("link", link);
        datastore.put(AlasanStar);
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
