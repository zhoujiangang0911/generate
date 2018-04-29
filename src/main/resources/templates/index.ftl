<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="keywords" content="在线MyBatis生成数据库映射文件，生成MyBatis映射文件"><!--关键词-->
    <meta name="description" content="在线MyBatis生成数据库映射文件,生成MyBatis映射文件"><!--描述-->

    <title>在线MyBatis生成数据库映射文件</title>  <link rel="shortcut icon" href="${images}/mall.ico" type="image/x-icon">

    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"  ></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->

</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container">

        <div class="right_col" role="main">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                        <div class="x_content">
                            <form action="/generator" method="post" class="form-horizontal form-label-left" novalidate>
                                <div class="item form-group" style="margin-top: 100px;text-align: center;font-size: 40px"">
                                在线MyBatis生成数据库映射文件
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">选择数据库<span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <select class="form-control" name="dbType" required="required">
                                    <option value="mysql">mysql</option>
                                    <option value="oracle">oracle</option>
                                </select>
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">ip <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="dbIP" placeholder="" value="127.0.0.1" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">port <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="dbPort" placeholder="" value="3306" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">dbName <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="dbName" placeholder=""  value="veve8" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">dbUser <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="dbUser" placeholder="" value="root" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">dbPass <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="dbPass" placeholder="" value="123456" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">tableName <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="tableName" placeholder="" value="goods"  required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">modelPackage <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="modelPackage" placeholder="" value="com.zhoujg77" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">xmlPackage<span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="xmlPackage" placeholder="" value="com.zhoujg77" required="required" type="text">
                            </div>
                        </div>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12">mapPackage<span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input  class="form-control col-md-7 col-xs-12" data-validate-length-range="1,120"
                                        name="mapPackage" placeholder="" value="com.zhoujg77" required="required" type="text">
                            </div>

                        </div>
                        <div class="ln_solid"></div>
                        <div class="form-group">
                            <div class="col-md-6 col-md-offset-3">
                                <input type="submit" class="btn btn-success submit" value="生成下载(预计需要10秒钟)">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-6 col-md-offset-3">
                                <a href="https://ngrok.com/"  target="black">本地数据库可以用ngork代理tpc数据库端口使用</a>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-6 col-md-offset-3">
                                <a href="https://github.com/zhoujiangang0911/generate"  target="black">项目GitHub地址</a>
                            </div>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</div>
</div>
</body>
</html>
