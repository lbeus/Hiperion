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

    var baseUri = "/app/service/common";

    function reloadStreams(){
        $.ajax({
            type: "GET",
            url: baseUri + "/streams",
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

        reloadStreams()

        var client;
        $('#start_button').click(function(){
            $("#tbl_data").find("tr:gt(0)").remove();
            client = new Faye.Client('/live',{timeout: 3600});
            client.disable('websocket')
            var streamId = $('#stream_select').val();
            var channel = "/data/" + streamId;
            $('#stream_select').prop('disabled', true);
            client.subscribe(channel, function(message){
                var data = JSON.parse(message);
                var dataFields = data.dataFields;

                $("#tbl_data").find("tr:gt(0)").remove();
                var ordinalNumber = 0;
                $.each(dataFields, function (i, element) {
                    var dataDate = new Date(element.timestamp);
                    var valueString = element.value;

                    if(element.dataType == "BINARY_PNG"){
                        valueString = '<img src="data:image/png;base64,' + element.value + ' />'
                    }
                    if(element.dataType == "BINARY_JPG"){
                        valueString = '<img src="data:image/jpg;base64,' + element.value + '" />'
                    }

                    $("#tbl_data tr:last").after(

                            "<tr><td>" + ++ordinalNumber + "</td>" +
                                    "<td>" + element.name + "</td>" +
                                    "<td>" + element.dataType + "</td>" +
                                    "<td>" + valueString + "</td>" +
                                    "<td>" + dataDate.toTimeString() + "</td></tr>"
                    );
                });
            });
        });

        $('#stop_button').click(function(){
            $("#tbl_events").find("tr:gt(0)").remove();
            client.unsubscribe();
            $('#stream_select').prop('disabled', false);
        });
    });
</script>

<h3>Live Data</h3>
<hr>
<form class="form-inline">
    <label class="control-label" for="stream_select">Stream Id:</label>
    <select id="stream_select"></select>
</form>
<div class="btn-group">
    <button id="start_button" class="btn btn-primary" style="margin-right: 10px;">Start</button>
    <button id="stop_button" class="btn btn-primary" style="margin-right: 10px;">Stop</button>
</div>
<hr>
<table id="tbl_data" class="table table-bordered">
    <tbody>
    <tr>
        <th style="width: 5%">#</th>
        <th style="width: 15%">Field Name</th>
        <th style="width: 15%">Data Type</th>
        <th style="width: 20%">Data value</th>
        <th style="width: 25%">Timestamp</th>
    </tr>
    </tbody>
</table>
