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

    var baseUri = "/app/service/events";

    function reloadEvents() {
        $.ajax({
            type: "GET",
            url: baseUri + "/",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#tbl_events").find("tr:gt(0)").remove();
                $("#event_select").find("option").remove()
                var ordinalNumber = 1;
                $.each(data, function (i, element) {
                    $("#tbl_events tr:last").after(

                            "<tr><td>" + ordinalNumber++ + "</td>" +
                                    "<td>" + element.name + "</td>" +
                                    "<td>" + element.publishable + "</td>" +
                                    "<td>" + element.stateful + "</td>" +
                                    "<td>" + element.streamId + "</td>" +
                                    "<td>" + element.registered + "</td>" +
                                    "<td>" + element.description + "</td></tr>"
                    );
                    $("#event_select").append("<option>" + element.name + "</option>").val(element.name);
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

        reloadEvents();

        $('#add_event_button').click(function () {
            var xmlContentValue = editor.getValue();
            var object = new Object();
            object.xmlContent = xmlContentValue;
            $.ajax({
                type: "POST",
                url: baseUri + "/event/add",
                data: JSON.stringify(object),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadEvents();
                }
            });

        });

        $('#remove_event_button').click(function () {
            var eventId = $("#event_select").val();
            $.ajax({
                type: "GET",
                url:  baseUri + "/event/" + eventId + "/remove",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadEvents();
                }
            });
        });

        $('#reload_event_button').click(function () {
            var eventId = $("#event_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/event/" + eventId + "/reload",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadEvents();
                }
            });
        });

        $('#register_event_button').click(function () {
            var eventId = $("#event_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/event/" + eventId + "/register",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadEvents();
                }
            });
        });

        $('#unregister_event_button').click(function () {
            var eventId = $("#event_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/event/" + eventId + "/unregister",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadEvents();
                }
            });
        });

        $('#view_xml_event_button').click(function () {
            var eventId = $("#event_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/event/" + eventId + "/xml",
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

<h3>Events</h3>
<hr>
<table id="tbl_events" class="table table-bordered">
    <tbody>
    <tr>
        <th style="width: 5%">#</th>
        <th style="width: 20%">Event Name</th>
        <th style="width: 10%">Publishable</th>
        <th style="width: 5%">Stateful</th>
        <th style="width: 20%">Stream Id</th>
        <th style="width: 10%">Registered</th>
        <th style="width: 30%">Description</th>
    </tr>
    </tbody>
</table>
<hr>
<select id="event_select" style="margin-top: 10px;">
</select>

<div class="btn-group">
    <button id="remove_event_button" class="btn btn-primary" style="margin-right: 10px;">Remove</button>
    <button id="reload_event_button" class="btn btn-primary" style="margin-right: 10px;">Reload</button>
    <button id="register_event_button" class="btn btn-primary" style="margin-right: 10px;">Register</button>
    <button id="unregister_event_button" class="btn btn-primary" style="margin-right: 10px;">Unregister</button>
    <button id="view_xml_event_button" class="btn btn-primary" style="margin-right: 10px;">View XML</button>
</div>
<div id="event-xml-area">
    <label>Event XML Configuration</label>

    <div style="border-top: 1px solid black; border-bottom: 1px solid black;">
        <textarea id="code" cols="120" rows="5"></textarea>
    </div>

    <script type="text/javascript">

    </script>
    <hr>
    <button id="add_event_button" class="btn btn-primary" style="margin-right: 10px;">Add Event</button>
    <hr>
</div>