<html>
<head>
    <meta charset="UTF-8">
    <title>成功提示</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="col-md-12">
            <div class="alert alert-success">
                <button type="button" class="close" data-dismiss="alert">×</button>
                <h4>
                    成功!
                </h4> <strong>${msg!""}</strong><a href="${url}" class="alert-link">3s后自动跳转</a>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    setTimeout('location.href="${url}"', 3000);
</script>
</html>