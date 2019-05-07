<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../common/header.jsp" %>
</head>
<body>

<%@include file="../common/nav.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%@include file="../common/sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div id="myBreadcrumb">
                <ul class="breadcrumb">
                    <li>
                        <a href="#">首页</a> <span class="divider">></span>
                    </li>
                    <li class="active">
                        <a href="${pageContext.request.contextPath}/admin/user/list">商品列表</a>
                    </li>
                </ul>
            </div>
            <br>
            <div id="content">
                <div class="table-responsive">
                    <form class="form-inline" method="get" action="${pageContext.request.contextPath}/admin/user/mall">
                        <div class="form-group">
                            <label for="Search" class="sr-only">Search</label>
                            <input type="text" name="productName" class="form-control" id="Search" placeholder="Search"
                                   value="${productName}">
                        </div>
                        <button type="submit" class="btn btn-default">查询</button>
                        <a href="${pageContext.request.contextPath}/admin/user/add">
                            <button type="button" class="btn btn-info">添加</button>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/user/restoreData">
                            <button type="button" class="btn btn-warning">数据还原</button>
                        </a>
                        <button type="button" class="btn btn-danger" onclick="deleteUserMore()">批量删除</button>
                    </form>
                    <br>
                    <c:choose>
                        <c:when test="${fn:length(products) > 0}">
                            <a>整体信息</a>
                            <table class="table table-bordered">
                                <tr>
                                    <th>网站</th>
                                    <th>商户名</th>
                                    <th>评论数</th>
                                    <th>销量</th>
                                </tr>
                                <c:forEach var="shopInfo" items="${shopInfos}">
                                    <tr>
                                        <td>${shopInfo.ecName}</td>
                                        <td>${shopInfo.shopName}</td>
                                        <td>${shopInfo.reviewNum}</td>
                                        <td>${shopInfo.tradeNum}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <a>详细信息</a>

                            <table class="table table-bordered">
                                <tr>
                                    <th><input type="checkbox" id="allSelect" onclick="DoCheck()"></th>
                                    <th>ID</th>
                                    <th>网站</th>
                                    <th>商品名</th>
                                    <th>商户名</th>
                                    <th>商品价格</th>
                                    <th>评论数</th>
                                    <th>销量</th>
                                </tr>
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td><input type="checkbox" name="ids" value="${product.productid}"></td>
                                        <td>${product.productid}</td>
                                        <td>${product.ecName}</td>
                                        <td>${product.productName}</td>
                                        <td>${product.shopName}</td>
                                        <td>${product.productPrice}</td>
                                        <td>${product.reviewNum}</td>
                                        <td>${product.tradeNum}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:when>
                        <c:otherwise><%--如果没有文章--%>
                            <div class="alert alert-danger">
                                <a href="#" class="close" data-dismiss="alert">
                                    &times;
                                </a>
                                <strong>警告！</strong>这里什么都没有。哼，小坏坏！
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="alert alert-success">
                        <a href="#" class="close" data-dismiss="alert">
                            &times;
                        </a>
                        <strong>温馨提示&nbsp;</strong> <br>
                        1、如果数据被删除了，可以点击上方的"<strong>数据还原</strong>"按钮，后台会在数据库中重新添加29条记录 <br>
                        2、查询功能：根据用户名、昵称、邮箱、电话、个人主页模糊查询 <br>
                        3、当前user表中有条记录,共页 <br>
                        4、可以通过page和size参数来跳转指定页码和设置每页显示数量，如list?page=2&size=10 <br>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="../common/footer.jsp" %>