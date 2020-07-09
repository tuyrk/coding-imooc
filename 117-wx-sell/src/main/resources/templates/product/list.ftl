<html>
<#include "../common/header.ftl">
<body>
<div id="wrapper" class="toggled">
<#--边栏sidebar-->
    <#include "../common/nav.ftl">
<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row-fluid">
            <#--表格-->
                <div class="col-md-12">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>商品ID</th>
                            <th>名称</th>
                            <th>图片</th>
                            <th>单价</th>
                            <th>库存</th>
                            <th>描述</th>
                            <th>类目</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                    <#list productInfoPage.content as productInfo>
                    <tr>
                        <td>${productInfo.productId}</td>
                        <td>${productInfo.productName}</td>
                        <td><img src="${productInfo.productIcon}" alt="商品图片" height="50"></td>
                        <td>${productInfo.productPrice}</td>
                        <td>${productInfo.productStock}</td>
                        <td>${productInfo.productDescription}</td>
                        <td>${productInfo.categoryType}</td>
                        <td>${productInfo.createTime}</td>
                        <td>${productInfo.updateTime}</td>
                        <td><a href="/sell/seller/product/index?productId=${productInfo.productId}">修改</a></td>
                        <td>
                            <#if productInfo.getProductStatusEnum().message == "在架">
                                <a href="/sell/seller/product/off_sale?productId=${productInfo.productId}">下架</a>
                            <#else>
                                <a href="/sell/seller/product/on_sale?productId=${productInfo.productId}">上架</a>
                            </#if>

                        </td>
                    </tr>
                    </#list>
                        </tbody>
                    </table>
                </div>
            <#--分页-->
                <div class="col-md-12">
                    <ul class="pagination pull-right">
                    <#-- orderDTOPage.number == current-1 -->
                <#if productInfoPage.number lte 0>
                    <li class="disabled"><a href="#">上一页</a></li>
                <#else>
                    <li>
                        <a href="/sell/seller/product/list?page=${productInfoPage.number}&size=${productInfoPage.size}">上一页</a>
                    </li>
                </#if>
                <#list 1..productInfoPage.totalPages as index>
                    <#if productInfoPage.number+1 == index>
                        <li class="disabled"><a href="#">${index}</a></li>
                    <#else>
                        <li><a href="/sell/seller/product/list?page=${index}&size=${productInfoPage.size}">${index}</a>
                        </li>
                    </#if>
                </#list>
                <#if productInfoPage.number+1 gte productInfoPage.totalPages>
                    <li class="disabled"><a href="#">下一页</a></li>
                <#else>
                    <li>
                        <a href="/sell/seller/product/list?page=${productInfoPage.number+2}&size=${productInfoPage.size}">下一页</a>
                    </li>
                </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
