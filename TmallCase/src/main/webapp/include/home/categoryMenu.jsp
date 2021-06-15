<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>

<div class="categoryMenu">
    <c:forEach items="${categories}" var="category">
        <div cid="${category.id}" class="eachCategory">
            <span class="glyphicon glyphicon-link"></span>
<%--            <a href="forecategory?cid=${category.id}">--%>
            <a href="CategoryForServlet?cid=${category.id}">
                    ${category.name}
            </a>
        </div>
    </c:forEach>

<%--    <c:forEach items="${categories}" var="c" varStatus="vs">--%>
<%--        <div class="d-menu" cid="${c.id}" style="display: none">--%>
<%--            <c:forEach items="${c.products}" var="p" varStatus="vs">--%>
<%--                <a href="product?id=${p.id}">${p.subTitle}</a>--%>
<%--            </c:forEach>--%>
<%--        </div>--%>
<%--    </c:forEach>--%>
</div>