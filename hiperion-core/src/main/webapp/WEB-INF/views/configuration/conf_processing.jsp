<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
  Created by IntelliJ IDEA.
  User: iobestar
  Date: 12.05.13.
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>

<script type="text/javascript">

    var baseUri = "/app/service/configuration/processing";

    function reloadActions() {
        $.ajax({
            type: "GET",
            url: baseUri + "/actions",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#action_select").find("option").remove()
                $.each(data, function (i, element) {
                    $("#action_select").append("<option>" + element.actionId +  "</option>").val(element.actionId);
                });
            }
        });
    }

    function reloadStreams(){
        $.ajax({
            type: "GET",
            url: "/app/service/common/streams",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#stream_select").find("option").remove()
                $.each(data, function (i, element) {
                    $("#stream_select").append("<option>" + element + "</option>").val(element);
                });
            }
        });
    }

    $(function () {

        reloadActions();
        reloadStreams();

        $('#enable_button').click(function () {
            var actionId = $("#action_select").val();
            var streamId = $("#stream_select").val();
            var object = new Object();
            object.actionId = actionId;
            object.streamId= streamId;
            $.ajax({
                type: "POST",
                url: baseUri + "/enable",
                data: JSON.stringify(object),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                }
            });
        });

        $('#disable_button').click(function () {
            var actionId = $("#action_select").val();
            var streamId = $("#stream_select").val();
            var object = new Object();
            object.actionId = actionId;
            object.streamId= streamId;
            $.ajax({
                type: "POST",
                url: baseUri + "/disable",
                data: JSON.stringify(object),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                }
            });
        });
    });
</script>

<h3>Processing configuration</h3>
<hr>
<form class="form-inline">
    <label class="control-label" for="action_select">Processing Action Id:</label>
    <select id="action_select"></select>
    <label class="control-label" for="stream_select">Stream Id:</label>
    <select id="stream_select" placeholder="stream id"></select>
</form>
<hr>
<div class="control-group">
    <div class="controls">
        <button id="enable_button" class="btn btn-primary" style="margin-right: 10px;">Enable</button>
        <button id="disable_button" class="btn btn-primary" style="margin-right: 10px;">Disable</button>
    </div>
</div>