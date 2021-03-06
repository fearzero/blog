(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return "";
    }
})(jQuery);

function getParameter() {
    var reg_int = /^[0-9]+$/;
    var key;
    var xx = $.getUrlParam('userId') + $.getUrlParam('userName');
    if (reg_int.test(xx)) {
        key = parseInt(xx);
    } else {
        key = xx;
    }
    return key;
};

function isLogin() {
    var login = 0;
    $.ajax({
        url: "/isLogin",
        dataType: "text",
        async: false,
        success: function (data) {
            if (data == "true") {
                login = 1;
            }
        }
    });
    return login;
};

function displaypage(len) {
    if (len == 0) {
        $(".blog").hide();
    } else {
        $(".blog").show();
    }
}

var userId = 0;
var blogerId;
var vueblog = new Vue({
    el: "#vue_blog",
    data: {
        blogs: [],
        userinfo: {}
    }
});
var vueleftinfo = new Vue({
    el: "#vue_leftinfo",
    data: {
        blogCount: 0,
        blogerCount: 0,
        fansCount: 0,
        username: "",
        pic: "",
        userId: 0
    }
});

function init() {
    var userParameter = getParameter();
    var userField = jQuery.isNumeric(userParameter) ? "userId" : "userName";
    if (jQuery.isNumeric(userParameter)) {
        $.ajax({
            url: "/selectUidUsafeUinfoBlog",
            type: "post",
            data: {"userId": userParameter},
            dataType: "json",
            success: function (data) {
                vueblog.usersafe = {
                    userId: data.userId,
                    userName: data.userName,
                    password: data.password,
                    userEmail: data.userEmail,
                    userTel: data.userTel,
                    userStatus: data.userStatus,
                    userMoney: data.userMoney,
                    userIntegral: data.userIntegral,
                    userRole: data.userRole,
                };

                vueleftinfo.username = data.userName;
                vueleftinfo.userId = data.userinfo.userId;

                vueblog.userinfo = {
                    userId: data.userinfo.userId,
                    realName: data.userinfo.realName,
                    userSex: data.userinfo.userSex,
                    userBirthday: data.userinfo.userBirthday,
                    userPic: "",
                    userCreateTime: data.userinfo.userCreateTime,
                };
                if (data.userinfo.userPic == null) {
                    vueblog.userinfo.userPic = "images/headpic.jpg";
                    vueleftinfo.pic = "images/headpic.jpg";
                } else {
                    vueblog.userinfo.userPic = "face/" + data.userinfo.userPic
                    vueleftinfo.pic = "face/" + data.userinfo.userPic;
                }
                vueleftinfo.blogCount = data.blogList.length;
                displaypage(vueleftinfo.blogCount);
                for (var i = 0; i < data.blogList.length; i++) {
                    vueblog.blogs.push({
                        blogId: data.blogList[i].blogId,
                        blogTitle: data.blogList[i].blogTitle,
                        blogCreateTime: data.blogList[i].blogCreateTime,
                        blogPubType: data.blogList[i].blogPubType,
                        blogReadNum: data.blogList[i].blogReadNum,
                        blogContent: data.blogList[i].blogContent,
                        blogAuthor: data.userName,
                        userId:data.userId
                    })
                }
                $.ajax({
                    url: "/selectBlogerCount",
                    type: "get",
                    data: {"fansId": data.userId},
                    dataType: "json",
                    success: function (data) {
                        vueleftinfo.blogerCount = data;
                    }
                });
                blogerId = data.userId;
                $.ajax({
                    url: "/selectFansCount",
                    type: "get",
                    data: {"blogerId": data.userId},
                    dataType: "json",
                    success: function (data) {
                        vueleftinfo.fansCount = data;
                    }
                });
                if (isLogin()) {
                    $.ajax({
                        url: "/isFans",
                        data: {"blogerId": data.userId},
                        dataType: "text",
                        success: function (data) {
                            if (data == 1) {
                                $("#watch").text("已关注");
                                $("#watch").css({"background-color": "red", "color": "white"});
                            }
                        }
                    });
                }
            }
        })
    } else {
        $.ajax({
            url: "/selectUnameUsafeUinfoBlog",
            type: "post",
            data: {"userName": userParameter},
            dataType: "json",
            success: function (data) {
                vueblog.usersafe = {
                    userId: data.userId,
                    userName: data.userName,
                    password: data.password,
                    userEmail: data.userEmail,
                    userTel: data.userTel,
                    userStatus: data.userStatus,
                    userMoney: data.userMoney,
                    userIntegral: data.userIntegral,
                    userRole: data.userRole,
                };

                vueleftinfo.username = data.userName;

                vueblog.userinfo = {
                    userId: data.userinfo.userId,
                    realName: data.userinfo.realName,
                    userSex: data.userinfo.userSex,
                    userBirthday: data.userinfo.userBirthday,
                    userPic: "face/" + data.userinfo.userPic,
                    userCreateTime: data.userinfo.userCreateTime,
                };
                if (data.userinfo.userPic == null) {
                    vueblog.userinfo.userPic = "images/headpic.jpg";
                    vueleftinfo.pic = "images/headpic.jpg";
                } else {
                    vueblog.userinfo.userPic = "face/" + data.userinfo.userPic
                    vueleftinfo.pic = "face/" + data.userinfo.userPic;
                }

                vueleftinfo.blogCount = data.blogList.length;
                displaypage(vueleftinfo.blogCount)

                for (var i = 0; i < data.blogList.length; i++) {
                    vueblog.blogs.push({
                        blogId: data.blogList[i].blogId,
                        blogTitle: data.blogList[i].blogTitle,
                        blogCreateTime: data.blogList[i].blogCreateTime,
                        blogPubType: data.blogList[i].blogPubType,
                        blogReadNum: data.blogList[i].blogReadNum,
                        blogContent: data.blogList[i].blogContent,
                        blogAuthor: data.userName
                    })
                }
                $.ajax({
                    url: "/selectBlogerCount",
                    type: "get",
                    data: {"fansId": data.userId},
                    dataType: "json",
                    success: function (data) {
                        vueleftinfo.blogerCount = data;
                    }
                });
                $.ajax({
                    url: "/selectFansCount",
                    type: "get",
                    data: {"blogerId": data.userId},
                    dataType: "json",
                    success: function (data) {
                        vueleftinfo.fansCount = data;
                    }
                })
            }
        })
    }
}

$(function () {

    $("#vue_blog").on("click", ".ajax_userName", function () {
        var userName = $(this).text();
        var url1 = "/userBlogIndex.html?userName=" + userName;
        window.location.replace(url1);
    });
    init();
    /*$("#vue_blog").on("click",".blogtitle",function () {
        var blogId = $(this).children().text();
        var url1 = "/blogDetail.html?blogId="+ blogId;
        window.location.replace(url1);
    });*/
    $.ajax({
        url: "/getUserInfo",
        dataType: "json",
        success: function (data) {
            $(".loginheadpic img").attr("src", "face/" + data.userPic);
        }
    });


    $("#watch").click(function () {
        if (isLogin()) {
            if ($("#watch").text() == '关注') {
                $.ajax({
                    url: "/insertRelation",
                    data: {"blogerId": blogerId},
                    dataType: "text",
                    success: function () {
                        $("#watch").text("已关注");
                        $("#watch").css({"background-color": "red", "color": "white"});
                    }
                });
            } else if ($("#watch").text() == '已关注' || $("#watch").text() == '取消关注') {
                $.ajax({
                    url: "deleteRelation",
                    data: {"blogerId": blogerId},
                    dataType: "text",
                    success: function () {
                        $("#watch").text("关注");
                        $("#watch").css({"background-color": "white", "color": "black"});
                    }
                });
            }
        } else {
            window.location.href = "/login.html";
        }
    });

});