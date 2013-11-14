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

    var baseUri = "/app/service/collectors";
    function reloadCollectors(){
        $.ajax({
            type: "GET",
            url: baseUri + "/",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#tbl_data_collectors").find("tr:gt(0)").remove();
                $("#data_collector_select").find("option").remove()
                var ordinalNumber = 1;
                $.each(data, function (i, element) {
                    $("#tbl_data_collectors tr:last").after(

                            "<tr><td>" + ordinalNumber++ + "</td>" +
                                    "<td>" + element.name + "</td>" +
                                    "<td>" + element.description + "</td>" +
                                    "<td>" + element.type + "</td>" +
                                    "<td>" + element.cronExpression + "</td>" +
                                    "<td>" + element.registered + "</td></tr>"
                    );
                    $("#data_collector_select").append("<option>" + element.name + "</option>").val(element.name);
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


        reloadCollectors();

        $('#add_collector_button').click(function () {

            var xmlContentValue = editor.getValue();
            var object = new Object();
            object.xmlContent = xmlContentValue;
            $.ajax({
                type: "POST",
                url: baseUri + "/collector/add",
                data: JSON.stringify(object),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadCollectors()
                }
            });

        });

        $('#remove_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/remove",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadCollectors()
                }
            });
        });

        $('#reload_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/reload",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadCollectors();
                }
            });
        });

        $('#deploy_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/deploy",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadCollectors();
                }
            });
        });

        $('#undeploy_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/undeploy",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadCollectors();
                }
            });
        });

        $('#run_collecting_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/collect",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                }
            });
        });

        $('#view_xml_collector_button').click(function () {
            var collectorId = $("#data_collector_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/collector/" + collectorId + "/xml",
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

<h3>Data Collectors</h3>
<hr>
<table id="tbl_data_collectors" class="table table-bordered">
    <tbody>
    <tr>
        <th style="width: 5%">#</th>
        <th style="width: 15%">Data Collector Name</th>
        <th style="width: 40%">Description</th>
        <th style="width: 15%">Type</th>
        <th style="width: 15%">Cron Expression</th>
        <th style="width: 5%">Deployed</th>
    </tr>
    </tbody>
</table>
<hr>
<select id="data_collector_select" style="margin-top: 10px;">
</select>

<div class="btn-group">
    <button id="remove_collector_button" class="btn btn-primary" style="margin-right: 10px;">Remove</button>
    <button id="reload_collector_button" class="btn btn-primary" style="margin-right: 10px;">Reload</button>
    <button id="deploy_collector_button" class="btn btn-primary" style="margin-right: 10px;">Deploy</button>
    <button id="undeploy_collector_button" class="btn btn-primary" style="margin-right: 10px;">Undeploy</button>
    <button id="run_collecting_collector_button" class="btn btn-primary" style="margin-right: 10px;">Run Collecting
    </button>
    <button id="view_xml_collector_button" class="btn btn-primary" style="margin-right: 10px;">View XML</button>
</div>
<div id="data-collector-xml-area">
    <label>Data Collector XML Configuration</label>

    <div style="border-top: 1px solid black; border-bottom: 1px solid black;">
        <textarea id="code" cols="120" rows="5"></textarea>
    </div>

    <script type="text/javascript">

    </script>
    <hr>
    <button id="add_collector_button" class="btn btn-primary" style="margin-right: 10px;">Add Data Collector</button>
    <hr>
</div>