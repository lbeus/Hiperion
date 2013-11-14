<%-- 
    Document   : DefaultLayout
    Created on : Sep 8, 2012, 9:41:03 PM
    Author     : iobestar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="/static/js/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="/static/js/libs/codemirror/codemirror.css" rel="stylesheet" media="screen">
    <link href="/static/css/style.css" rel="stylesheet" media="screen">

    <style type="text/css">
    </style>
    <script type="text/javascript">
            var CONTEXT_ROOT = '<%= request.getContextPath() %>';
    </script>
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <script src="/static/js/libs/codemirror/codemirror.js"></script>
    <script src="/static/js/libs/codemirror/xml.js"></script>
    <script src="/static/js/libs/faye/faye-browser-min.js"></script>
    <script src="http://code.jquery.com/jquery.js"></script>
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand" href="#">HIPERION</a>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><a href="/">Home</a></li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Data Collecting<b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/app/view/collectors">Collectors</a> </li>
                                <li><a href="/app/view/sources">Sources</a></li>
                                <li><a href="/app/view/events">Events</a></li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Output<b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/app/view/output/live-data">Live Data</a> </li>
                                <li><a href="/app/view/output/live-events">Live Events</a></li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Configuration<b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/app/view/configuration/nodes">Nodes</a></li>
                                <li><a href="/app/view/configuration/processing">Processing</a></li>
                            </ul>
                        </li>
                        <li><a href="/app/view/action">Action Runner</a></li>
                        <li><a href="/app/view/log">Log Viewer</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <tiles:insertAttribute name="header"/>
        <tiles:insertAttribute name="body"/>
    </div>
    <div class="navbar navbar-inverse navbar-fixed-bottom">
        <div class="navbar-inner">
            <footer id="footer">
                <div>
                    <p>&copy; RASIP@FER 2013</p>
                </div>
            </footer>
        </div>
    </div>
</body>
<script src="/static/js/libs/bootstrap/js/bootstrap.min.js"></script>
</html>
