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

    function reloadLogFileNames(){
        $.ajax({
            type: "GET",
            url: baseUri + "/logs",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $("#log_select").find("option").remove()
                $.each(data, function (i, element) {
                    $("#log_select").append("<option>" + element + "</option>").val(element);
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

        reloadLogFileNames();

        $('#get_log_button').click(function(){
            var logFileName = $("#log_select").val();
            $.ajax({
                type: "POST",
                url: baseUri + "/log/get",
                data: logFileName,
                contentType: "text/plain; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    var jsondata = JSON.stringify(data);
                    var myObject = JSON.parse(jsondata);
                    editor.setValue(myObject.logContent);
                }
            });
        });
    });
</script>

<h3>Log Viewer</h3>
<hr>
<form class="form-inline">
    <label class="control-label" for="log_select">Log File Name:</label>
    <select id="log_select"></select>
</form>
<button id="get_log_button" class="btn btn-primary" style="margin-right: 10px;">Get Log Content</button>
<hr>
<div id="log-content-area">
    <label>Log Content:</label>
    <div style="border-top: 1px solid black; border-bottom: 1px solid black;">
        <textarea id="code" cols="120" rows="20"></textarea>
    </div>
</div>