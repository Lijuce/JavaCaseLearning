<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>

<script>
    function showProductsAsideCategorys(cid) {
        $("div.eachCategory[cid=" + cid + "]").css("background-color", "white");
        $("div.eachCategory[cid=" + cid + "] a").css("color", "#87CEFA");
        $("div.productsAsideCategorys[cid=" + cid + "]").show();
    }

    function hideProductsAsideCategorys(cid) {
        $("div.eachCategory[cid=" + cid + "]").css("background-color", "#e2e2e3");
        $("div.eachCategory[cid=" + cid + "] a").css("color", "#000");
        $("div.productsAsideCategorys[cid=" + cid + "]").hide();
    }

    $(function () {
        $("div.eachCategory").mouseenter(function () {
            var cid = $(this).attr("cid");
            showProductsAsideCategorys(cid);
        });
        $("div.eachCategory").mouseleave(function () {
            var cid = $(this).attr("cid");
            hideProductsAsideCategorys(cid);
        });
        $("div.productsAsideCategorys").mouseenter(function () {
            var cid = $(this).attr("cid");
            showProductsAsideCategorys(cid);
        });
        $("div.productsAsideCategorys").mouseleave(function () {
            var cid = $(this).attr("cid");
            hideProductsAsideCategorys(cid);
        });

        $("div.rightMenu span").mouseenter(function () {
            var left = $(this).position().left;
            var top = $(this).position().top;
            var width = $(this).css("width");
            var destLeft = parseInt(left) + parseInt(width) / 2;
            $("img#catear").css("left", destLeft);
            $("img#catear").css("top", top - 20);
            $("img#catear").fadeIn(500);

        });
        $("div.rightMenu span").mouseleave(function () {
            $("img#catear").hide();
        });

        var left = $("div#carousel-of-product").offset().left;
        $("div.categoryMenu").css("left", left - 20);
        $("div.categoryWithCarousel div.head").css("margin-left", left);
        $("div.productsAsideCategorys").css("left", left - 20);

    });
</script>

<img src="${pageContext.request.contextPath}/img/site/catear.png" id="catear" class="catear"/>

<div class="categoryWithCarousel">

<%--    首栏商品分类信息 --%>
    <div class="headbar show1">
        <div class="head ">

            <span style="margin-left:10px" class="glyphicon glyphicon-th-list"></span>
            <span style="margin-left:10px">商品分类</span>

        </div>

        <div class="rightMenu">
            <span><a href=""><img src="${pageContext.request.contextPath}/img/site/chaoshi.png"/></a></span>
            <span><a href=""><img src="${pageContext.request.contextPath}/img/site/guoji.png"/></a></span>

            <c:forEach items="${categories}" var="category" varStatus="st">
                <c:if test="${st.count<=4}">
                <span>
<%--                <a href="forecategory?cid=${category.id}">--%>
<%--                <a href="CategoryForServlet?cid=${category.id}">--%>
                <a href="CategoryById?cid=${category.id}">

                        ${category.name}
                </a></span>
                </c:if>
            </c:forEach>
        </div>

    </div>

<%--    左侧商品分类栏  --%>
    <div style="position: relative">
        <%@include file="categoryMenu.jsp" %>
    </div>
<%--    <div class="categoryMenu">--%>
<%--        <ul>--%>
<%--            <c:forEach items="${categories}" var="c" varStatus="vs">--%>
<%--                <c:if test="${vs.count>=1 and vs.count<=13}">--%>
<%--                    <li cid="${c.id}"><span class="glyphicon glyphicon-link"></span><a href="category?id=${c.id}">${c.name}</a></li>--%>
<%--                </c:if>--%>
<%--            </c:forEach>--%>
<%--        </ul>--%>
<%--    </div>--%>
<%--    <c:forEach items="${categories}" var="c" varStatus="vs">--%>
<%--        <div class="d-menu" cid="${c.id}" style="display: none">--%>
<%--            <c:forEach items="${c.products}" var="p" varStatus="vs">--%>
<%--                <a href="product?id=${p.id}">${p.subTitle}</a>--%>
<%--            </c:forEach>--%>
<%--        </div>--%>
<%--    </c:forEach>--%>


    <div style="position: relative;left: 0;top: 0;">
<%--        <td class="registerTip registerTableLeftTD">设置会员名</td>--%>
        <%@include file="productsAsideCategories.jsp" %>
    </div>

    <%@include file="carousel.jsp" %>

    <div class="carouselBackgroundDiv">
    </div>

</div>