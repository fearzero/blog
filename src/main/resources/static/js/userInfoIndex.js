$(function () {
    $.ajax({
        url: "/getUserInfo",
        dataType: "json",
        success: function (data) {
            $(".loginheadpic img").attr("src", "face/" + data.userPic);
            $(".pic img").attr("src", "face/" + data.userPic);
        }
    });

    $("#choosepic").click(function () {
        $("input[name='file']").click();
    })

    $.ajax({
        url: "/getUserinfo1",
        type: "post",
        dataType: "json",
        success: function (data) {
            $.ajax({
                url: "/getLoginUserName",
                type: "post",
                dataType: "text",
                success: function (data1) {

                    $.ajax({
                        url: "/selectFansCount1",
                        type: "post",
                        dataType: "json",
                        success: function (data2) {
                            $.ajax({
                                url: "/selectBlogerCount1",
                                type: "post",
                                dataType: "json",
                                success: function (data3) {
                                    var $li2 = (" <div class=\"idinfo1\">" +
                                        "<li>用户名：<span>" + data1 + "</span></li>\n" +
                                        "                            <ul>\n" +
                                        "                                <li>粉丝数：<span>" + data2 + "</span></li>\n" +
                                        "                                <li>关注数：<span>" + data3 + "</span></li>\n" +
                                        "                            </ul>" +
                                        "</div>");
                                    var $li = ("<div class=\"info\">" +
                                        "<li>真实姓名：<span>" + data.realName + "</span></li>" +
                                        "<li>性别：<span>" + data.userSex + "</span></li>" +
                                        "<li>生日：<span>" + new Date(data.userBirthday).format("yyyy-MM-dd") + "</span></li>" +
                                        "<li>职业：<span>学生</span></li>" +
                                        "<li>地区：<span>福建省 厦门市 集美区</span></li>" +
                                        "<li>简介：<span></span></li>" +
                                        "</div>");
                                    $("div[class='otherinfo']").append($li);
                                    $("div[class='idinfo']").append($li2);
                                }
                            })
                        }
                    })
                }
            })
        }
    });
    $(".updateinfo span").click(function () {
        $(".modifyinfo").show()
    })
    $(".backbutton").click(function () {
        $(".modifyinfo").hide()
    })
    Date.prototype.format = function (fmt) {   //日期格式化方法
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }
        return fmt;
    };
})