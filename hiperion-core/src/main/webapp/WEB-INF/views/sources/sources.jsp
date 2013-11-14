<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
  Created by IntelliJ IDEA.
  User: iobestar
  Date: 12.05.13.
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>

<script type="text/javascript">

    var baseUri = "/app/service/sources";

    function reloadSources(){
        $.ajax({
            type: "GET",
            url: baseUri + "/",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#tbl_data_sources").find("tr:gt(0)").remove();
                $("#data_source_select").find("option").remove()
                var ordinalNumber = 1;
                $.each(data, function (i, element) {
                    $("#tbl_data_sources tr:last").after(

                            "<tr><td>" + ordinalNumber++ + "</td>" +
                                    "<td>" + element.name + "</td>" +
                                    "<td>" + element.description + "</td>" +
                                    "<td>" + element.latitude + "</td>" +
                                    "<td>" + element.longitude + "</td></tr>"
                    );
                    $("#data_source_select").append("<option>" + element.name + "</option>").val(element.name);
                });
            }
        });
    }

    $(function () {

        var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
            mode: {name: "xml", alignCDATA: true},
            lineNumbers: true,
            autoCloseTags: true
        });

        reloadSources()

        $('#add_source_button').click(function () {

            var xmlContentValue = editor.getValue();
            var object = new Object();
            object.xmlContent = xmlContentValue;
            $.ajax({
                type: "POST",
                url: baseUri + "/source/add",
                data: JSON.stringify(object),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadSources();
                }
            });

        });

        $('#remove_source_button').click(function () {
            var sourceId = $("#data_source_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/source/" + sourceId + "/remove",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadSources();
                }
            });
        });

        $('#reload_source_button').click(function () {
            var sourceId = $("#data_source_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/source/" + sourceId + "/reload",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadSources()
                }
            });
        });

        $('#view_xml_source_button').click(function () {
            var sourceId = $("#data_source_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/source/" + sourceId + "/xml",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    editor.setValue(myObject.xmlContent);
                }
            });
        });
    });
</script>

<h3>Data Sources</h3>
<hr>
<table id="tbl_data_sources" class="table table-bordered">
    <tbody>
    <tr>
        <th style="width: 5%">#</th>
        <th style="width: 15%">Data Source Name</th>
        <th style="width: 15%">Description</th>
        <th style="width: 10%">Latitude</th>
        <th style="width: 10%">Longitude</th>
    </tr>
    </tbody>
</table>
<hr>
<select id="data_source_select" style="margin-top: 10px;">
</select>

<div class="btn-group">
    <button id="remove_source_button" class="btn btn-primary" style="margin-right: 10px;">Remove</button>
    <button id="reload_source_button" class="btn btn-primary" style="margin-right: 10px;">Reload</button>
    <button id="view_xml_source_button" class="btn btn-primary" style="margin-right: 10px;">View XML</button>
</div>
<div id="data-source-xml-area">
    <label>Data Source XML Configuration</label>

    <div style="border-top: 1px solid black; border-bottom: 1px solid black;">
        <textarea id="code" cols="120" rows="5"></textarea>
    </div>

    <script type="text/javascript">

    </script>
    <hr>
    <button id="add_source_button" class="btn btn-primary" style="margin-right: 10px;">Add Data Source</button>
    <hr>
</div>