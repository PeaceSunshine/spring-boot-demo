<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Freemarler测试</title>
    <!--TODO-->

</head>

<body style="position:relative;">

<#--${createTime?date('yyyy-MM-dd')}使用该语法错误,使用${createTime?date}-->
<h3><a href="#">${product.createTime?date}</a></h3>
</body>
</body>
</html>
