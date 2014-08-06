<%-- 
    Document   : index
    Created on : Jul 26, 2014, 6:32:43 PM
    Author     : khairulanshar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kawal Menteri</title>
        <style type="text/css">
            table {
                border:1px solid white;
                font-size:14px;
                font-family: Helvetica, Arial, 'lucida grande', tahoma, verdana, arial, sans-serif;
            }
            td {
                color:#000000;
                vertical-align: top;
                padding:2px;
                white-space:initial;
            }
            th {
                padding:2px;
                background-color:#CFE0F1;
                white-space:nowrap;
                cursor: pointer;
                padding:10px;
            }
            tr:nth-child(even) { background: #EEE;}
            tr:nth-child(odd) { background:  #fbfbfb;}

            .classa {
                font-family: Helvetica, Arial, 'lucida grande', tahoma, verdana, arial, sans-serif;
                font-size:12px;
                text-decoration:none solid rgb(59, 89, 152);
                color: rgb(59, 89, 152);
                height: 14px;
            }
            .classa:hover {
                text-decoration: underline;
            }

        </style>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <link href="rateit/rateit.css" rel="stylesheet" type="text/css">
        <link href="rateit/content/bigstars.css" rel="stylesheet" type="text/css">
        <script src="rateit/jquery.rateit.js" type="text/javascript"></script>
        <script type="text/javascript" src="jQuery-TE_v.1.4.0/jquery-te-1.4.0.min.js" charset="utf-8"></script>
        <link type="text/css" rel="stylesheet" href="jQuery-TE_v.1.4.0/jquery-te-1.4.0.css">
        <script>
            (function(i, s, o, g, r, a, m) {
                i['GoogleAnalyticsObject'] = r;
                i[r] = i[r] || function() {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
                a = s.createElement(o),
                        m = s.getElementsByTagName(o)[0];
                a.async = 1;
                a.src = g;
                m.parentNode.insertBefore(a, m)
            })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
            ga('create', 'UA-53226163-1', 'auto');
            ga('send', 'pageview');
        </script>
        <script>
            var sortState = [1, 1];
            var userAccount = {};
            var fb_cache = {};
            function sort(i) {
                var trs = $('#tbodySort').children("tr");
                var tds = $(trs[0]).find('td');
                var idx = 0;
                for (var j = 0; j < tds.length; j++) {
                    if ($(tds[j]).data('sort-index') == i) {
                        idx = j + 1;
                        break;
                    }
                }
                var mul = 1;
                if (sortState[0] === idx) {
                    mul = sortState[1] * -1;
                }
                sortState = [idx, mul];
                trs.sort(function(a, b) {
                    var ka = $(a).find('td:nth-child(' + idx + ')').data('sort');
                    var kb = $(b).find('td:nth-child(' + idx + ')').data('sort');
                    if (!ka && !kb)
                        return 0 * mul;
                    if (!ka && !!kb)
                        return -1 * mul;
                    if (!!ka && !kb)
                        return 1 * mul;
                    if (!!ka.localeCompare)
                        return ka.localeCompare(kb) * mul;
                    return (ka - kb) * mul;
                });
                $(trs).detach();
                $('#tbodySort').append(trs);
            }
            var timeout_;
            // This is called with the results from from FB.getLoginStatus().
            function statusChangeCallback(response) {
                if (response.status === 'connected') {
                    try{
                        clearTimeout(timeout_);
                    }catch(e){}
                    get_fb('me', getChild);
                } else if (response.status === 'not_authorized') {

                } else {

                }
            }
            
            function checkLoginState() {
                FB.getLoginStatus(function(response) {
                    statusChangeCallback(response);
                });

                timeout_ = setTimeout(function() {
                    getHash();
                }, 3500);
            }
            $(document).ready(function() {
                $('#content').html("Sedang memproses data...");
                window.fbAsyncInit = function() {
                    FB.init({
                        appId: '1519572904939148',
                        xfbml: true,
                        cookie: true,
                        version: 'v2.0',
                        status: true
                    });
                    checkLoginState();
                };
                // Load the SDK asynchronously
                (function(d, s, id) {
                    var js, fjs = d.getElementsByTagName(s)[0];
                    if (d.getElementById(id))
                        return;
                    js = d.createElement(s);
                    js.id = id;
                    js.src = "//connect.facebook.net/en_US/sdk.js";
                    fjs.parentNode.insertBefore(js, fjs);
                }(document, 'script', 'facebook-jssdk'));
            <%--userAccount = {email: "khairul.anshar@gmail.com"
                , first_name: "Khairul"
                , gender: "male"
                , id: "10152397276159760"
                , last_name: "Anshar"
                , link: "https://www.facebook.com/app_scoped_user_id/10152397276159760/"
                , locale: "en_US"
                , name: "Khairul Anshar"
                , timezone: 8
                , updated_time: "2014-07-23T06:12:43+0000"
                , verified: true};
            fb_cache["me"] = userAccount;
            get_fb('me', getChild);--%>
            });

            var getChild = function(id, res) {
                $("#loginFBbutton").hide();
                fb_cache[id] = res;
                $.ajax({
                    url: "/action?form_action=cekauth&session=" + Math.random()
                    , type: "POST"
                    , data: JSON.stringify(res)
                    , dataType: 'json'
                    , mimeType: "application/json"
                    , contentType: "application/json"
                    , cache: false
                    , success: function(result) {
                        $('#content').html("");
                        $('#headerdiv').show();
                        try {
                            if (result.userAccount) {
                                userAccount = result.userAccount;
                            }
                            getHash();
                        } catch (e) {
                        }
                    }
                    , error: function() {
                        $('#content').html("Ada yang bermasalah dengan koneksi internet...");
                    }
                    , complete: function() {
                    }
                });
            };
            function getHash() {
                var arr = window.location.hash.substr(1).split('.');
                if (arr[0] == "") {
                    window.location = "/#0.0";
                }
                if ((arr.length == 1 && (arr[0] == "0"))) {
                    set0();
                }
                if (arr.length == 2) {
                    set1(arr[1], dataPosisi[parseInt(arr[1])]);
                }
                if (arr.length >= 3) {
                    //set1(arr[1], dataPosisi[parseInt(arr[1])]);
                    set2(arr[1], arr[2], replaceSpecial(dataPosisi[parseInt(arr[1])]), arr[arr.length - 1]);
                }
            }
            function replaceSpecial(inp) {
                var val = inp.replace(/\ /g, "")
                        .replace(/\,/g, "")
                        .replace(/\./g, "")
                        .replace(/\`/g, "")
                        .replace(/\~/g, "")
                        .replace(/\!/g, "")
                        .replace(/\@/g, "")
                        .replace(/\#/g, "")
                        .replace(/\$/g, "")
                        .replace(/\%/g, "")
                        .replace(/\^/g, "")
                        .replace(/\&/g, "")
                        .replace(/\*/g, "")
                        .replace(/\(/g, "")
                        .replace(/\)/g, "")
                        .replace(/\+/g, "")
                        .replace(/\|/g, "")
                        .replace(/\{/g, "")
                        .replace(/\}/g, "")
                        .replace(/\[/g, "")
                        .replace(/\]/g, "")
                        .replace(/\:/g, "")
                        .replace(/\;/g, "")
                        .replace(/\"/g, "")
                        .replace(/\'/g, "")
                        .replace(/\?/g, "")
                        .replace(/\//g, "");
                return val;
            }
            window.onhashchange = getHash;
            var dataPosisi = ["Menteri", "Pejabat Setingkat Menteri", "Duta Besar"];
            var dataPosisi2 = [];
            function set0() {
                $('#content').html("");
                $('#navTop').html("");
                $('#navBottom').html("");
                var tblb = $("<table>").appendTo($('#content'));
                var thead = $("<thead>").appendTo(tblb);
                var tbody = $("<tbody>").attr("id", "tbodySort").appendTo(tblb);
                var tblhtr = $("<tr>").appendTo(thead);
                $("<th>").text("No.").appendTo(tblhtr).click(function() {
                    sort(0);
                });
                $("<th>").text("Posisi").appendTo(tblhtr).click(function() {
                    sort(1);
                });
                var i = 0;

                $.each(dataPosisi, function(k, c) {
                    i++;
                    tblhtr = $("<tr>").appendTo(tbody);
                    $("<td>").attr("data-sort-index", "0").attr("data-sort", i + "").css("text-align", "center").text(i).appendTo(tblhtr);
                    var a = $("<a>").css("color", "rgb(109, 132, 180)").attr("href", "#0." + (i - 1)).text(c);
                    $("<td>").attr("data-sort-index", "1").attr("data-sort", c).append(a).appendTo(tblhtr);
                })
            }
            function set1(input_, type_) {
                $('#content').html("Sedang memproses data...");
                $('#navTop').html("");
                $('#navBottom').html("");
                $('#navTop').css("padding", "5px").append($("<a>").attr("href", "#0").attr("class", "classa").html("&laquo; Halaman Utama"));
                $('#navBottom').css("padding", "5px").append($("<a>").attr("href", "#0").attr("class", "classa").html("&laquo; Halaman Utama"));
                $.ajax({
                    url: "/action?form_action=getSet1&session=" + Math.random()
                    , type: "POST"
                    , data: JSON.stringify({input: input_, type: type_})
                    , dataType: 'json'
                    , mimeType: "application/json"
                    , contentType: "application/json"
                    , cache: false
                    , success: function(result) {
                        set1Result(result, input_, type_);
                    }
                    , error: function() {
                        $('#content').html("Ada yang bermasalah dengan koneksi internet...");
                    }
                    , complete: function() {

                    }
                });
            }
            function set1Result(result, input_, type_) {
                try {
                    $('#content').html("");
                    $('#content').show();
                    var tblb = $("<table>").appendTo($('#content'));
                    var thead = $("<thead>").appendTo(tblb);
                    var tbody = $("<tbody>").attr("id", "tbodySort").appendTo(tblb);
                    var tfoot = $("<tfoot>").appendTo(tblb).css("background-color","#CFE0F1");
                    var tblhtr = $("<tr>").appendTo(thead);
                    $("<th>").text("No.").appendTo(tblhtr).click(function() {
                        sort(0);
                    });
                    $("<th>").text("Posisi").appendTo(tblhtr).click(function() {
                        sort(1);
                    });

                    var i = 0;
                    function createCol(tbody, data, i, type) {

                        var a;
                        if (type == "read") {
                            tblhtr = $("<tr>").css("vertical-align", "top").appendTo(tbody);
                            //line-height: 15.359999656677246px
                            $("<td>").attr("data-sort-index", "0").attr("data-sort", i + "").css("text-align", "center").text(i).appendTo(tblhtr);
                            a = $("<b>").text(data.posisi);
                            var divx0 = $("<div>").html("");
                            if (data.detail) {
                                var divx = $("<div>").html(data.detail).appendTo(divx0);
                                divx.jqte();
                                divx0.find('div.jqte_editor')[0].setAttribute("contenteditable", false);
                                //divx0.find('div.jqte_editor')[0].style.width = "600px";
                                divx0.find('div.jqte_toolbar')[0].innerHTML = "";
                                //divx0.find('div.jqte_toolbar')[0].style.width = "600px";
                                divx0.find('div.jqte_toolbar')[0].style.display = "none";
                            }
                            var divDet = $("<div>").hide();
                            var tddet = $("<td>").appendTo(tblhtr).attr("data-sort-index", "1").attr("data-sort", data.posisi).append(a).append($("<br>"));
                            var pdet = $("<p>").appendTo(tddet);
                            pdet.append($("<a>").attr("class", "classa").attr("href", "javascript:").text("Detail").click(function() {
                                if (divDet.css("display") == "none") {
                                    divDet.show();
                                    divDet.html("");
                                    divDet.append($("<span>").text("Diusulkan Oleh: ")).append($("<a>").css("font-size", "14px").attr("class", "classa").attr("href", data.link).attr("target", data.nama).text(data.nama))
                                            .append($("<br>")).append($("<span>").text("Detail:")).append(divx0);
                                    $(this).html("Ringkas");
                                } else {
                                    divDet.html("");
                                    divDet.hide();
                                    $(this).html("Detail");
                                }
                            }));

                            pdet.append($("<b>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;"));
                            pdet.append($("<a>").attr("class", "classa").attr("href", "#0." + input_ + "." + replaceSpecial(data.posisi) + "." + data.posisi).text("Lihat Kandidat"));
                            tddet.append(divDet);
                        }
                        if (type == "write") {
                            tblhtr = $("<tr>").css("background-color","#CFE0F1").appendTo(tfoot);
                            var tddet0 = $("<td>").appendTo(tblhtr).attr("colspan", 3);
                            var tddet = $("<p>").hide().appendTo(tddet0);
                            var pdiv = $("<span>").hide();
                            var p = $("<p>").appendTo(tddet0);
                            p.append($("<a>").attr("class", "classa").attr("href", "javascript:").text("Usulkan Posisi Baru").click(function() {
                                if (tddet.css("display") == "none") {
                                    tddet.show();
                                    $(this).html("Batalkan");
                                    tddet.html("");
                                    var input = $("<input>").css("width", "500px").css("font-size", "14px").val(data.posisi);
                                    var input2x = $('<textarea><b>Alasan: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Target Tahun I: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Target Tahun II: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Target Tahun III: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Target Tahun IV: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Target Tahun V: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul></textarea>').css("width", "300px");
                                    tddet.append($("<b>").text("Nama Jabatan: ")).append($("<br>")).append(input)
                                            .append($("<br>")).append($("<b>").text("Detail: ")).append(input2x);
                                    input2x.jqte();
                                    pdiv.html("");
                                    pdiv.show();
                                    pdiv.append($("<span>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;"));
                                    pdiv.append($("<a>").attr("class", "classa").attr("href", "javascript:").text("Simpan").click(function() {
                                        if (input.val().length == 0
                                                || input2x.val().length == 0) {
                                            alert("Silahkan Tuliskan Usulan Anda.")
                                        } else {
                                            $('#content').hide();
                                            $.ajax({
                                                url: "/action?form_action=setSet1&session=" + Math.random()
                                                , type: "POST"
                                                , data: JSON.stringify({input: input_, type: type_, value: input.val(), value1: input2x.val()})
                                                , dataType: 'json'
                                                , mimeType: "application/json"
                                                , contentType: "application/json"
                                                , cache: false
                                                , success: function(result) {
                                                    set1Result(result, input_, type_);
                                                }
                                                , error: function() {
                                                    $('#content').html("Ada yang bermasalah dengan koneksi internet...");
                                                }
                                                , complete: function() {
                                                }
                                            });
                                        }
                                    }));

                                } else {
                                    tddet.hide();
                                    pdiv.html("");
                                    pdiv.hide();
                                    $(this).html("Usulkan Posisi Baru");
                                }
                            }));
                            pdiv.appendTo(p);
                        }

                    }
                    $.each(result.records, function(k, c) {
                        i++;
                        createCol(tbody, c, i, "read")
                    });
                    if (userAccount.name) {
                        i++;
                        createCol(tbody, {posisi: "", nama: userAccount.name, link: userAccount.link}, i, "write")
                    }
                } catch (e) {

                }
            }
            function set2(input0_, input_, type0_, menteri) {
                $('#content').html("Sedang memproses data...");
                $('#navTop').html("");
                $('#navBottom').html("");
                $('#navTop').css("padding", "5px").append($("<a>").attr("href", "#0").attr("class", "classa").html("&laquo; Halaman Utama"));
                $('#navBottom').css("padding", "5px").append($("<a>").attr("href", "#0").attr("class", "classa").html("&laquo; Halaman Utama"));
                $('#navTop').append($("<span>").html("&nbsp;&nbsp;&nbsp;")).append($("<a>").attr("class", "classa").attr("href", "#0." + input0_).html("&laquo; Kembali ke List " + type0_));
                $('#navBottom').append($("<span>").html("&nbsp;&nbsp;&nbsp;")).append($("<a>").attr("class", "classa").attr("href", "#0." + input0_).html("&laquo; Kembali ke List " + type0_));
                $.ajax({
                    url: "/action?form_action=getSet2&session=" + Math.random()
                    , type: "POST"
                    , data: JSON.stringify({input0: input0_, type0: type0_, input: input_})
                    , dataType: 'json'
                    , mimeType: "application/json"
                    , contentType: "application/json"
                    , cache: false
                    , success: function(result) {
                        set2Result(result, input0_, input_, type0_, menteri);
                    }
                    , error: function() {
                        $('#content').html("Ada yang bermasalah dengan koneksi internet...");
                    }
                    , complete: function() {

                    }
                });
            }
            function set2Result(result, input0_, input_, type0_, menteri) {
                try {
                    $('#content').html("");
                    $('#content').show();
                    var tblb = $("<table>").appendTo($('#content'));
                    var thead = $("<thead>").appendTo(tblb);
                    var tbody = $("<tbody>").attr("id", "tbodySort").appendTo(tblb);
                    var tfoot = $("<tfoot>").appendTo(tblb).css("background-color","#CFE0F1");
                    var tblhtr = $("<tr>").appendTo(thead);
                    $("<th>").text("No.").appendTo(tblhtr).click(function() {
                        sort(0);
                    });
                    $("<th>").width("620px").text("Kandidat " + menteri).appendTo(tblhtr).click(function() {
                        sort(1);
                    });
                    $("<th>").html("&nbsp;").appendTo(tblhtr);
                    var i = 0;

                    function createCol(tbody, data, i, type) {
                        var a;
                        if (type == "read") {
                            tblhtr = $("<tr>").appendTo(tbody);
                            $("<td>").attr("data-sort-index", "0").attr("data-sort", i + "").css("text-align", "center").text(i).appendTo(tblhtr);
                            var tdx = $("<td>").width("620px").appendTo(tblhtr)
                            var divx0 = $("<div>").html("").hide();
                            var divx1 = $("<div>").html("+").hide();
                            var divx2 = $("<div>").html("-").hide();
                            var ax = $("<a>").attr("href", "javascript:").attr("class", "classa").text("Detail").click(function() {
                                if (divx0.css("display") == "none") {
                                    divx1.html("");
                                    divx2.html("");
                                    divx1.hide();
                                    divx2.hide();
                                    ax1.text("Komentar +");
                                    ax2.text("Komentar -");
                                    divx0.html("");
                                    divx0.show();
                                    divx0.append($("<span>").html("<b>Diusulkan oleh</b>: ")).append($("<a>").attr("class", "classa").css("font-size", "14px").attr("href", data.link).attr("target", data.nama).text(data.nama)).append($("<br>"))
                                    var divx = $("<div>").html(data.detail).appendTo(divx0);
                                    divx.jqte();
                                    divx0.find('div.jqte_editor')[0].setAttribute("contenteditable", false);
                                    //divx0.find('div.jqte_editor')[0].style.width = "600px";
                                    divx0.find('div.jqte_toolbar')[0].innerHTML = "";
                                    //divx0.find('div.jqte_toolbar')[0].style.width = "600px";
                                    divx0.find('div.jqte_toolbar')[0].style.display = "none";
                                    //divx0.find('div.jqte')[0].style.width = "600px";
                                    $(this).text("Ringkas");
                                } else {
                                    divx0.html("");
                                    divx0.hide();
                                    $(this).text("Detail");
                                }

                            });
                            var ax1 = $("<a>").attr("href", "javascript:").attr("class", "classa").text("Komentar +").click(function() {
                                if (divx1.css("display") == "none") {
                                    divx0.html("");
                                    divx2.html("");
                                    divx0.hide();
                                    divx2.hide();

                                    ax.text("Detail");
                                    ax2.text("Komentar -");

                                    divx1.show();
                                    $(this).text("Komentar +");
                                    divx1.html("+");
                                } else {
                                    divx1.hide();
                                    $(this).text("Komentar +");
                                }

                            });
                            var ax2 = $("<a>").attr("href", "javascript:").attr("class", "classa").text("Komentar -").click(function() {
                                if (divx2.css("display") == "none") {
                                    divx0.html("");
                                    divx1.html("");
                                    divx0.hide();
                                    divx1.hide();

                                    ax.text("Detail");
                                    ax1.text("Komentar +");


                                    divx2.show();
                                    $(this).text("Komentar -");
                                    divx2.html("-");
                                } else {
                                    divx2.hide();
                                    $(this).text("Komentar -");
                                }

                            });
                            var tbld = $("<table>");
                            var trd = $("<tr>").css("background-color", "transparent").appendTo(tbld);
                            $("<td>").append(ax).appendTo(trd);
                            $("<td>").append($("<span>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;")).appendTo(trd);
                            $("<td>").append(ax1).appendTo(trd);
                            $("<td>").append($("<span>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;")).appendTo(trd);
                            $("<td>").append(ax2).appendTo(trd);
                            tdx.append($("<span>").html("<b>Nama</b>: " + data.kandidat)).append($("<br>"))
                                    .append($("<span>").html("<b>Deskripsi Umum</b>:" + data.desc)).append($("<br>"))
                                    .append(tbld)
                                    .append(divx0)
                                    .append(divx1)
                                    .append(divx2);

                            var td__ = $("<td>").html("&nbsp;").appendTo(tblhtr);

                            td__.html("")
                            var div1 = $("<div>").appendTo(td__);
                            var div2 = $("<div>").appendTo(td__);
                            getMyComment(div1, input_, replaceSpecial(data.kandidat), div2, input0_, i);

                        }

                        if (type == "write") {
                            tblhtr = $("<tr>").css("background-color","#CFE0F1").appendTo(tfoot);
                            var td = $("<td>").appendTo(tblhtr).attr("colspan", "3");
                            var div = $("<p>").hide().appendTo(td);
                            var div0 = $("<p>").appendTo(td);
                            var div1 = $("<span>").css("display", "inline").html("");
                            div0.append($("<a>").attr("href", "javascript:").attr("class", "classa").text("Usulkan Kandidat Baru").click(function() {
                                if (div.css("display") == "none") {
                                    div.show();
                                    $(this).html("Batalkan");
                                    div.html("");
                                    var input = $("<input>").css("width", "250px").css("font-size", "14px");
                                    var input1 = $("<input>").css("width", "500px").css("font-size", "14px");
                                    var input2 = $('<textarea><img src="http://fc06.deviantart.net/fs70/i/2014/150/c/0/jokowi_jk_wpap_by_vector10-d7kb493.jpg" style="max-width:150px;"><b>Tempat & Tanggal Lahir: </b><span>???</span><br><b>Riwayat Pendidikan: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Riwayat Kerja Profesional: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Riwayat Organisasi: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Keanggotaan/Asosiasi: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Prestasi: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Penghargaan: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Kekayaan: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Indikator Positif: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul><br><b>Indikator Negatif: </b><ul style="margin: 0px;"><li style="margin: 0px;">???</li></ul></textarea>').css("width", "500px");
                                    div.append($("<span>").html("<b>Nama</b>:<br>")).append(input).append($("<br>"))
                                            .append($("<span>").html("<b>Deskripsi Umum</b>:<br>")).append(input1).append($("<br>"))
                                            .append($("<span>").html("<b>Detail</b>:")).append(input2);
                                    input2.jqte();
                                    div1.append($("<span>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;"));
                                    div1.append($("<a>").attr("class", "classa").text("Simpan...").click(function() {
                                        if (input.val().length == 0
                                                || input1.val().length == 0
                                                || input2.val().length == 0) {
                                            alert("Silahkan Tuliskan Usulan Anda.")
                                        } else {
                                            $.ajax({
                                                url: "/action?form_action=setSet2&session=" + Math.random()
                                                , type: "POST"
                                                , data: JSON.stringify({input0: input0_, type0: type0_, input: input_, value: input.val(), value1: input1.val(), value2: input2.val()})
                                                , dataType: 'json'
                                                , mimeType: "application/json"
                                                , contentType: "application/json"
                                                , cache: false
                                                , success: function(result) {
                                                    set2Result(result, input0_, input_, type0_, menteri);
                                                }
                                                , error: function() {
                                                    $('#content').html("Ada yang bermasalah dengan koneksi internet...");
                                                }
                                                , complete: function() {
                                                }
                                            });
                                        }
                                    }));
                                } else {
                                    div.hide();
                                    div.html("");
                                    div1.html("");
                                    $(this).html("Usulkan Kandidat Baru");
                                }
                            }));
                            div1.appendTo(div0);



                        }
                    }
                    $.each(result.records, function(k, c) {
                        i++;
                        createCol(tbody, c, i, "read")
                    });
                    if (userAccount.name) {
                        i++;
                        createCol(tbody, {kandidat: "", desc: "", detail: "", nama: userAccount.name, link: userAccount.link}, i, "write");
                    }
                } catch (e) {
                }
            }
            function get_fb(id, getChild) {
                if (fb_cache[id]) {
                    return getChild(id, fb_cache[id]);
                }
                FB.api('/' + id, function(res) {
                    getChild(id, res);
                });
            }
            function getMyComment(td, dept, namaCalon, td_, i, ii) {
                td.html("Sedang Mengambil Data...");
                $.ajax({
                    url: "/action?form_action=getMyComment&session=" + Math.random()
                    , type: "POST"
                    , data: JSON.stringify({dept: dept, namaCalon: namaCalon})
                    , dataType: 'json'
                    , mimeType: "application/json"
                    , contentType: "application/json"
                    , cache: false
                    , success: function(result) {
                        try {
                            if (userAccount.name) {
                                if (result.AlasanStar.name == userAccount.name) {
                                    td.html("");
                                    var tblb = $("<table>").css("border", "0px").appendTo(td);
                                    var trb = $("<tr>").css("background-color", "transparent").appendTo(tblb);
                                    $("<td>").append($("<b>").text("-").css("font-size", "12px")).css("border", "0px").appendTo(trb);
                                    var tdb1 = $("<td>").css("border", "0px").appendTo(trb);
                                    $("<td>").append($("<span>").text("0").css("font-size", "12px")).css("border", "0px").appendTo(trb);
                                    var tdb2 = $("<td>").css("border", "0px").appendTo(trb);
                                    $("<td>").append($("<b>").text("+").css("font-size", "12px")).css("border", "0px").appendTo(trb);
                                    var div__ = $("<div>").css("direction", "rtl").appendTo(tdb1);
                                    var div_ = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(div__);
                                    var div = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(tdb2);
                                    if (parseFloat(result.AlasanStar.star) <= 0) {
                                        div_.rateit({max: parseInt((parseFloat(result.AlasanStar.star) * -1) + 1), step: 1, min: 0, value: (parseFloat(result.AlasanStar.star) * -1)});
                                        div.rateit({max: 1, step: 1, min: 0});
                                    } else {
                                        div.rateit({max: parseInt(parseFloat(result.AlasanStar.star) + 1), step: 1, min: 0, value: (parseFloat(result.AlasanStar.star))});
                                        div_.rateit({max: 1, step: 1, min: 0});
                                    }
                                    td.append($("<span>").css("font-size", "9px").text(result.AlasanStar.date)).append($("<br>"));
                                    td.append($("<span>").css("font-size", "12px").text(result.AlasanStar.comment))
                                } else {
                                    td.html("");
                                    setCommentCol(td, dept, namaCalon, td_, i, ii);
                                }
                            } else {
                                td.html("");
                            }
                        } catch (e) {

                        }
                    }
                    , error: function() {
                    }
                    , complete: function() {

                    }
                });
            }
            function setCommentCol(td, dept, namaCalon, td_, i, ii) {
                var tblb = $("<table>").css("border", "0px").appendTo(td);
                var trb = $("<tr>").css("background-color", "transparent").appendTo(tblb);
                $("<td>").append($("<span>").text("-")).css("border", "0px").appendTo(trb);
                var tdb1 = $("<td>").css("border", "0px").appendTo(trb);
                var tdb12 = $("<td>").css("border", "0px").appendTo(trb);
                var tdb2 = $("<td>").css("border", "0px").appendTo(trb);
                var div__ = $("<div>").css("direction", "rtl").appendTo(tdb1);
                $("<td>").append($("<span>").text("+")).css("border", "0px").appendTo(trb);
                var div_ = $("<div>").attr("class", "rateit").attr("data-rateit-resetable", "false").data("val", "0").appendTo(div__);
                var div = $("<div>").attr("class", "rateit").attr("data-rateit-resetable", "false").data("val", "0").appendTo(tdb2);
                div_.bind('rated', function(event, value) {
                    div.rateit('reset');
                    div.data("val", (value * -1));
                });
                div_.rateit({max: 2, step: 1, min: 0});
                div.bind('rated', function(event, value) {
                    div_.rateit('reset');
                    div.data("val", value);
                });
                div.rateit({max: 2, step: 1, min: 0});
                $("<a>").attr("href", "javascript:").text("0").appendTo(tdb12).click(function() {
                    div_.rateit('reset');
                    div_.data("val", "0");
                    div.rateit('reset');
                    div.data("val", "0");
                    return false;
                });
                var textarea = $("<textarea>").attr("rows", "2").attr("cols", "38").appendTo(td);
                td.append($("<br>"));
                var divp = $("<div>");
                $("<a>").attr("class", "classa").attr("href", "javascript:").text("Petunjuk").appendTo(td).click(function() {
                    if (divp.css("display") == "none") {
                        divp.show();
                        //$(this).html("Sembunyikan Petunjuk");
                        divp.html("");
                        divp.append($("<span>").html("<b>-2</b> bintang artinya Sangat Tidak Setuju"))
                        divp.append($("<br>")).append($("<span>").html("<b>-1</b> bintang artinya Tidak Setuju"))
                        divp.append($("<br>")).append($("<span>").html("<b>0</b> artinya Netral"))
                        divp.append($("<br>")).append($("<span>").html("<b>1</b> bintang artinya Setuju"))
                        divp.append($("<br>")).append($("<span>").html("<b>2</b> bintang artinya Sangat Setuju"))
                    } else {
                        divp.hide();
                        //$(this).html("Petunjuk:");
                    }
                });
                td.append($("<span>").css("color", "#9197a3").css("font-size","10px").html("&nbsp;-&nbsp;"));
                td.append($("<a>").attr("class", "classa").attr("href", "javascript:").text("Simpan").click(function() {
                    if (textarea.val().length == 0) {
                        alert("Silahkan sertakan Alasan Anda!");
                    } else {
                        $.ajax({
                            url: "/action?form_action=postComment&session=" + Math.random()
                            , type: "POST"
                            , data: JSON.stringify({dept: dept, star: div.data("val"), comment: textarea.val(), namaCalon: namaCalon})
                            , dataType: 'json'
                            , mimeType: "application/json"
                            , contentType: "application/json"
                            , cache: false
                            , success: function(result) {
                                try {
                                    getMyComment(td, dept, namaCalon, td_, i, ii);
                                } catch (e) {

                                }
                            }
                            , error: function() {

                            }
                            , complete: function() {

                            }
                        });

                    }
                    //return false;
                }));
                td.append($("<br>")).append(divp);
                divp.hide();

            }
            function getlink(dept, namaCalon, td_, i, ii) {
                var ax = (dept + namaCalon).replace(/\ /g, "");
                ax = ax.replace(/\./g, "");
                ax = ax.replace(/\,/g, "");
                $("<a>").attr("id", "ida-" + i + "-" + ii).attr("href", "#id-" + i + "-" + ii).text("Perlihatkan Semua Penilaian dan Komentar").appendTo(td_).click(function() {
                    getAllComment(dept, namaCalon, td_, i, ii);
                });
            }
            function getAllComment(dept, namaCalon, td_, i, ii) {
                td_.html("");
                td_.html("Sedang Mengambil Data...");
                $.ajax({
                    url: "/action?form_action=getAlasanStarCalon&session=" + Math.random()
                    , type: "POST"
                    , data: JSON.stringify({dept: dept, namaCalon: namaCalon})
                    , dataType: 'json'
                    , mimeType: "application/json"
                    , contentType: "application/json"
                    , cache: false
                    , success: function(result) {
                        try {
                            if (result.total == null) {
                                result.total = 0;
                            }
                            td_.html("");
                            var divallComment = $("<div>").width("300px").css("overflow", "auto").css("max-height", "600px");
                            td_.append($("<b>").text("Total: " + result.total + " bintang"));
                            td_.append($("<br>"));
                            var tblb = $("<table>").css("border", "0px").appendTo(td_);
                            var trb = $("<tr>").css("background-color", "transparent").appendTo(tblb);
                            $("<td>").append($("<b>").text("-").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                            var tdb1 = $("<td>").css("border", "0px").appendTo(trb);
                            $("<td>").append($("<span>").text("0").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                            var tdb2 = $("<td>").css("border", "0px").appendTo(trb);
                            $("<td>").append($("<b>").text("+").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                            var div__ = $("<div>").css("direction", "rtl").appendTo(tdb1);
                            var div_ = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(div__);
                            var div = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(tdb2);
                            if (parseFloat(result.total) <= 0) {
                                div_.rateit({max: parseInt((parseFloat(result.total) * -1) + 1), step: 1, min: 0, value: (parseFloat(result.total) * -1)});
                                div.rateit({max: 1, step: 1, min: 0});
                            } else {
                                div.rateit({max: parseInt(parseFloat(result.total) + 1), step: 1, min: 0, value: (parseFloat(result.total))});
                                div_.rateit({max: 1, step: 1, min: 0});
                            }
                            td_.append($("<br>"));
                            divallComment.appendTo(td_);
                            $.each(result.AlasanStars, function(k, c) {
                                var tblh__ = $("<table>").css("border-spacing", "0px").css("padding", "0px").css("border", "0px").css("margin", "0px").appendTo(divallComment);
                                var tblhtr__ = $("<tr>").css("background-color", "transparent").css("vertical-align", "middle").css("text-align", "left").appendTo(tblh__);
                                var tblbxv = $("<table>").css("border", "0px");

                                $("<td>").css("vertical-align", "middle").css("text-align", "left").css("border", "0px").appendTo(tblhtr__)
                                        .append($("<b>").text(c.name).css("font-size", "15px").css("cursor", "pointer").click(function() {
                                            window.open(c.link)
                                        })).append($("<br>")).append($("<span>").css("font-size", "10px").text(c.date))
                                        .append($("<br>")).append(tblbxv);

                                var trb = $("<tr>").css("background-color", "transparent").appendTo(tblbxv);
                                $("<td>").append($("<b>").text("-").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                                var tdb1 = $("<td>").css("border", "0px").appendTo(trb);
                                $("<td>").append($("<span>").text("0").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                                var tdb2 = $("<td>").css("border", "0px").appendTo(trb);
                                $("<td>").append($("<b>").text("+").css("font-size", "14px")).css("border", "0px").appendTo(trb);
                                var div__ = $("<div>").css("direction", "rtl").appendTo(tdb1);
                                var div_ = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(div__);
                                var div = $("<div>").attr("class", "rateit").attr("data-rateit-ispreset", "false").attr("data-rateit-readonly", "true").appendTo(tdb2);
                                if (parseFloat(c.star) <= 0) {
                                    div_.rateit({max: parseInt((parseFloat(c.star) * -1) + 1), step: 0.5, min: 0, value: (parseFloat(c.star) * -1)});
                                    div.rateit({max: 1, step: 0.5, min: 0});
                                    //tdb2_.hide();
                                } else {
                                    div.rateit({max: parseInt(parseFloat(c.star) + 1), step: 0.5, min: 0, value: (parseFloat(c.star))});
                                    div_.rateit({max: 1, step: 0.5, min: 0});
                                    //tdb1_.hide();
                                }
                                //$("<td>").css("vertical-align", "middle").css("text-align", "left").css("border", "0px").appendTo(tblhtr__).append(tblbxv);
                                tblhtr__ = $("<tr>").css("background-color", "transparent").width("300px").css("vertical-align", "middle").css("text-align", "left").appendTo(tblh__);
                                $("<td>").width("300px").css("font-size", "15px").css("vertical-align", "middle").css("text-align", "left").css("border", "0px").appendTo(tblhtr__).html(c.comment);
                                divallComment.append($("<br>"));
                            });
                            getlink(dept, namaCalon, td_, i, ii);
                        } catch (e) {

                        }
                    }
                    , error: function() {

                    }
                    , complete: function() {

                    }
                });
            }
            function showvideo() {
                if ($("#fbvideo").css("display") == "none") {
                    $("#fbvideo").show();
                    $("#fbvideolink").html("Sembunyikan Facebook Frame");
                } else {
                    $("#fbvideo").hide();
                    $("#fbvideolink").html("Kunjungi Facebook Page");
                }
            }
//https://www.facebook.com/v2.0/dialog/oauth?app_id=855917207769189&client_id=855917207769189&display=popup&domain=www.kabinetrakyat.org&e2e=%7B%7D&locale=en_US&origin=1&redirect_uri=http%3A%2F%2Fstatic.ak.facebook.com%2Fconnect%2Fxd_arbiter%2FoDB-fAAStWy.js%3Fversion%3D41%23cb%3Df56bb8e3%26domain%3Dwww.kabinetrakyat.org%26origin%3Dhttp%253A%252F%252Fwww.kabinetrakyat.org%252Fff49e004%26relation%3Dopener%26frame%3Df36d6a2724&response_type=token%2Csigned_request&scope=public_profile%2Cemail&sdk=joey&version=v2.0
        </script>
    </head>
    <body>
        <div id="headerdiv" style="width:100%;">
            <table style="width:100%;">
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: center;font-weight: bolder; background-color: transparent;">KawalMenteri 2014</td></tr>
                <tr>
                    <td style="text-align: center;font-weight: bolder; background-color: transparent;">Apa itu KawalMenteri?</td>
                    <td style="text-align: center;font-weight: bolder; background-color: transparent;">Mengapa KawalMenteri?</td>
                </tr>
                <tr>
                    <td>KawalMenteri adalah sebuah platform partisipasi public melalui media internet yang memungkinkan semua masyarakat Indonesia menyampaikan pendapat dan penilaiannya terhadap para bakal calon Menteri dan kinerja Menteri yang telah terpilih dalam Kabinet Pemerintahan Periode 2014-2019.</td>
                    <td>KawalMenteri diperlukan untuk memfasilitasi arus baru demokrasi yang partisipatif, transparan, dan akuntabel sehingga memungkinkan adanya penyampaian langsung pendapat dan penilaian public terhadap para pejabat public dalam hal ini Menteri. Sebelum itu, pendapat dan penilaian public juga disampaikan terkait para bakal calon Menteri yang dianggap layak untuk menjadi anggota Menteri pada Kabinet Pemerintahan Periode 2014-2019.</td>
                </tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;">Lihat Lebih Lengkap di <a target="_blank" href="https://docs.google.com/document/d/1xgPEGkSGuMHtkRE21gV6_Q4nNjJrBTa1HZJuw1SscbM/edit?usp=sharing">Media Baru Demokrasi yang Partisipatif, Transparan, dan Akuntabel Menuju Indonesia Baru</a></td></tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;">Ingin tahu bagaimana <a target="_blank" href="https://docs.google.com/document/d/1po6fsXFLDgVHP-1sd6dy2Gput_SqfBigGLw13rfrPMs/edit?usp=sharing">Cara Berpartisipasi</a>?</td></tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;">Ingin jadi Anggota atau Koordinator <a target="_blank" href="https://docs.google.com/spreadsheets/d/1v6_bhQfa6gDuNwip8iFgw-Ju2X5NDgoddFGoC0snCGc/edit?usp=sharing">Tim Pos Kementerian</a></td></tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;color: red;">Semua Data akan di-reset (hapus) pada tanggal 17-Agustus-2014 </td></tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;color: red;"><a target="_blank" href="https://docs.google.com/document/d/1Aj7GssygE5Z5jbth9CNHDRkh_z8ibk5bkAeGjZS_E6k/edit?usp=sharing">Pertemuan Berikutnya Tanggal 7-Agustus-2014 Jam 18:30 WIB (Google Hangout akan dishare. Tebuka untuk umum)</a></td></tr>
                <tr style="background-color: transparent;"><td colspan="2" style="text-align: left;background-color: transparent;"><a href="javascript:showvideo()" id="fbvideolink">Kunjungi Facebook Page</a></td></tr>
            </table>
            <br/>
            <div id="fbvideo" style="display:none;">
                <div class="fb-post" data-href="https://www.facebook.com/photo.php?v=353140568168464" data-width="750"><div class="fb-xfbml-parse-ignore"><a href="https://www.facebook.com/photo.php?v=353140568168464">Post</a> by <a href="https://www.facebook.com/KawalMenteri">Kawal Menteri</a>.</div></div>
            </div>
        </div>
        <hr><br>

        <div id="fb-root"></div>
    <fb:login-button scope="public_profile,email" onlogin="checkLoginState();" id="loginFBbutton"></fb:login-button>
    <div class="fb-like" data-href="http://www.kawalmenteri.org" data-layout="button_count" data-action="like" data-show-faces="true" data-share="true"></div>
    <br/>
    <div><p id="navTop"></p></div>
    <div id="content"></div>
    <div><p id="navBottom"></p></div>
    <br/><br/>
    <div id="footerdiv"></div>
    <br/><br/><br/><br/>


</body>
</html>
