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

    var baseUri = "/app/service/action";

    function reloadNodeIds(){
        $.ajax({
            type: "GET",
            url: baseUri + "/nodes",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#node_select").find("option").remove()
                $.each(data, function (i, element) {
                    $("#node_select").append("<option>" + element + "</option>").val(element);
                });
                $('#node_select').trigger('change');
            }
        });
    }

    function reloadNodeActions(nodeId){
        $.ajax({
            type: "GET",
            url: baseUri + "/node/" + nodeId + "/actions",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#action_select").find("option").remove()
                $.each(data, function (i, element) {
                    $("#action_select").append("<option>" + element + "</option>").val(element);
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

        function isNodeAvailable(nodeId){
            $.ajax({
                type: "GET",
                url: baseUri + "/node/" + nodeId + "/available",
                data: "{}",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    if(data == false){
                        alert("Node " + nodeId + " not available.");
                    }
                }
            });
        }

        $('#run_action_button').click(function () {
            var nodeId = $('#node_select').val();
            var actionId = $('#action_select').val();
            var parametersXml = editor.getValue();
            var object = new Object();
            object.nodeId = nodeId;
            object.actionId = actionId;
            object.parametersXml = parametersXml;
            $.ajax({
                type: "POST",
                url: baseUri + "/run",
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

        $('#node_select').change(function() {
            var nodeId = $(this).val();
            isNodeAvailable(nodeId);
            reloadNodeActions(nodeId);
        });

        reloadNodeIds();
    });
</script>

<h3>Action Runner</h3>
<hr>
<form class="form-inline">
    <label class="control-label" for="node_select">Node Id:</label>
    <select id="node_select"></select>
    <label class="control-label" for="action_select">Node Action Id:</label>
    <select id="action_select"></select>
</form>
<hr>
<div id="parameters-xml-area">
    <label>XML Action Parameter</label>

    <div style="border-top: 1px solid black; border-bottom: 1px solid black;">
        <textarea id="code" cols="120" rows="5"></textarea>
    </div>
    <hr>
    <button id="run_action_button" class="btn btn-primary" style="margin-right: 10px;">Run Action</button>
    <hr>
</div>