<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/admin/user/list">鸿志博客</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a target="_blank" href="http://www.pole.org.cn/">设置</a></li>
                <li><a target="_blank" href="http://www.pole.org.cn/">帮助</a></li>
            </ul>
            <form class="navbar-form navbar-right" method="get" action="${pageContext.request.contextPath}/admin/user/search">
                <input type="text" name="query" class="form-control" id="Search" placeholder="Search..."
                                   value="${query}">
            </form>
        </div>
    </div>
</nav>
