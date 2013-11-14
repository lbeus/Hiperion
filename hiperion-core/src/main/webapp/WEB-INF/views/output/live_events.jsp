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

    $(function () {
        var ordinalNumber = 0
        $('#clear_button').click(function () {
            $("#tbl_events").find("tr:gt(0)").remove();
            ordinalNumber = 0;
        });

        var client = new Faye.Client('/live',{timeout: 3600});
        client.disable('websocket');
        client.subscribe('/events', function(message) {
            var event = JSON.parse(message);
            var eventDate = new Date(event.timestamp);
            $("#tbl_events tr:last").after(

                    "<tr><td>" + ++ordinalNumber + "</td>" +
                            "<td>" + event.eventId + "</td>" +
                            "<td>" + event.description + "</td>" +
                            "<td>" + event.eventState + "</td>" +
                            "<td>" + eventDate.toTimeString() + "</td></tr>"
            );
        });
    });
</script>

<h3>Live Events</h3>
<hr>
<div class="btn-group">
    <button id="clear_button" class="btn btn-primary" style="margin-right: 10px;">Clear</button>
</div>
<hr>
<table id="tbl_events" class="table table-bordered">
    <tbody>
        <tr>
            <th style="width: 5%">#</th>
            <th style="width: 15%">Event Id</th>
            <th style="width: 30%">Description</th>
            <th style="width: 15%">Event State</th>
            <th style="width: 25%">Timestamp</th>
        </tr>
    </tbody>
</table>