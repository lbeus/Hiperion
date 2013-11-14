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

    var baseUri = "/app/service/configuration/nodes";
    function reloadNodes(){
        $.ajax({
            type: "GET",
            url: baseUri + "/",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#tbl_nodes").find("tr:gt(0)").remove();
                $("#node_select").find("option").remove()
                var ordinalNumber = 1;
                $.each(data, function (i, element) {
                    $("#tbl_nodes tr:last").after(

                            "<tr><td>" + ordinalNumber++ + "</td>" +
                                    "<td>" + element.nodeId + "</td>" +
                                    "<td>" + element.hostname + "</td>" +
                                    "<td>" + element.port + "</td>" +
                                    "<td>" + element.servicePath + "</td></tr>"
                    );
                    $("#node_select").append("<option>" + element.nodeId + "</option>").val(element.nodeId);
                });
            }
        });
    }

    $(function () {

        reloadNodes();

        $('#save_node_button').click(function () {
            var nodeDescriptor = new Object();
            nodeDescriptor.nodeId = $("#inputNodeId").val();
            nodeDescriptor.hostname = $("#inputHostname").val();
            nodeDescriptor.port = $("#inputPort").val();
            nodeDescriptor.servicePath = $("#inputServicePath").val();
            $.ajax({
                type: "POST",
                url: baseUri + "/node/save",
                data: JSON.stringify(nodeDescriptor),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadNodes()
                }
            });

        });

        $('#remove_node_button').click(function () {
            var nodeId = $("#node_select").val();
            $.ajax({
                type: "GET",
                url: baseUri + "/node/" + nodeId + "/remove",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    alert(myObject.restResponse);
                    reloadNodes()
                }
            });
        });
    });
</script>

<h3>Nodes</h3>
<hr>
<table id="tbl_nodes" class="table table-bordered">
    <tbody>
    <tr>
        <th style="width: 5%">#</th>
        <th style="width: 10%">Node Id</th>
        <th style="width: 10%">Hostname</th>
        <th style="width: 10%">Port</th>
        <th style="width: 30%">Service Path</th>
    </tr>
    </tbody>
</table>
<hr>
<select id="node_select" style="margin-top: 10px;">
</select>
<div class="btn-group">
    <button id="remove_node_button" class="btn btn-primary" style="margin-right: 10px;">Remove</button>
</div>
<hr>
<form class="form-inline">
    <label class="control-label" for="inputNodeId">Node Id:</label>
    <input type="text" class="input-medium" id="inputNodeId" placeholder="node id">
    <label class="control-label" for="inputHostname">Hostname:</label>
    <input type="text" class="input-medium" id="inputHostname" placeholder="hostname">
    <label class="control-label" for="inputPort">Port:</label>
    <input type="text" class="input-small" id="inputPort" placeholder="port number">
    <label class="control-label" for="inputServicePath">Service Path:</label>
    <input type="text" class="input-xlarge" id="inputServicePath" placeholder="service path">
</form>
<hr>
<div class="control-group">
    <div class="controls">
        <button id="save_node_button" class="btn btn-primary" style="margin-right: 10px;">Save</button>
    </div>
</div>