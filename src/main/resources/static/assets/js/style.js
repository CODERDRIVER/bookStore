/*
    author: zbt
    date: 2018-10-18
 */

var cookieUtil = $.AMUI.utils.cookie;

function findKey(){
    var email = getId("doc-email").val;
    var type = $(':input[name="loginType"]:checked').val();
    $.ajax({
        type:'get',
        dataType:'json',
        url:'/password',
        contentType:'application/json;charset=UTF-8',
        data:{"email":email,"type":type},
        success:function(data){//返回结果
         console.log(data);
                // location.reload();
             window.location.reload();
          },
        error:function(data){
         alert('请求找回密码失败!');
        }
    });
}
//login function
$(function () {
    //如果已登录，切换到注销按钮
    if (cookieUtil.get('token') != null) {
        $('#log-out').css('display','block')
        $('#sign-in').css('display','none')
    }

    $('#sign-in').on('click', function () {
        if (cookieUtil.get('token') != null) {
            window.location = "login/all"
        } else {
            $('#loginPrompt').modal({
                relatedTarget: this,
                onConfirm: function (e) {
                    var data = e.data;
                    var email = data[0];
                    var password = data[1];
                    var requestData = JSON.stringify({
                        "email": email,
                        "password": password
                    });
                    var type = $(':input[name="loginType"]:checked').val();

                    if (type === "admin") {
                        login(requestData, 'login/admin', "admin")
                    } else if (type === "librarian") {
                        login(requestData, 'login/librarian', 'librarian')
                    } else if (type === "reader") {
                        login(requestData, 'login/reader', '/')
                    }
                },
                onCancel: function (e) {

                }
            });
        }
    });
});

/**
 * 模态登录窗口
 * @param data  登录数据
 * @param url   登录url接口
 * @param href  跳转页面
 */
function login(data, url, href) {
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            console.log(e);
            if (e.code === 0) {
                window.location.href = href
            } else {
                alert(e.message)
            }
        }
    })
}

/**
 * 登出按钮
 */
$('#log-out').click(function () {
    $.ajax({
        type: 'delete',
        url: 'logout',
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            if (e.code === 0) {
                window.location.href = "/"
            }
        }
    })
})

/**
 * 搜索框点击事件
 */
$('#search-button').click(function () {
    searchFun()
});
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        searchFun()
    }
});

/**
 * 加载搜索结果
 */
function searchFun() {
    $('#backGround').removeClass("get-full").addClass("get-small")
    var keyStr = $('#search-input').val()
    //加载分页结果
    loadBookPage(1, 5, keyStr)
    $('#bookPane').css("display", "block")
}


/**
 * 分页数据
 */
function loadBookPage(pageNo, pageSize, keyStr) {
    console.log(keyStr)
    if (keyStr == null) {
        keyStr = ""
    }
    $.ajax({
        type: 'get',
        url: 'books?pageNo=' + pageNo + '&pageSize=' + pageSize + '&keyStr=' + keyStr,
        contentType: "application/json;charset=UTF-8",
        error: function () {
            alert("网络异常！")
        },
        success: function (data) {
            $('#bookList').empty()
            $('#bookPagination').empty()
            //如果没有数据
            if (data.data.numberOfElements == 0) {
                // $('#morePage').attr('disabled', 'true')
                $('#bookList').append('' +
                    '        <li>\n' +
                    '            <div>\n' +
                    '                <strong class="am-center" style="\n' +
                    '                padding: 50px 0 350px 0;\n' +
                    '                text-align: center;\n' +
                    '                font-size: larger;">\n' +
                    '                        There is no matching results.\n' +
                    '                </strong>\n' +
                    '            </div>\n' +
                    '        </li>')
            } else {
                $('#morePage').unbind("click").bind("click", function () {
                    loadBookPage(1, 10, $('#search-input').val())
                });
                for (var i = 0; i < data.data.numberOfElements; i++) {
                    showBookInfo(data, i)
                }
                showBookPagination(data)
            }
        }
    })


    function showBookInfo(data, no) {
        var Name = data.data.content[no].bookName
        var inventory = data.data.content[no].inventory
        var bookType = data.data.content[no].bookType
        var author = data.data.content[no].author
        var barCode = data.data.content[no].barCode
        // console.log(data.data.content[no])
        $('#bookList').append('' +
            '            <li class="am-cf am-text-truncate">\n' +
            '                <!--书籍缩略图-->\n' +
            '                <p class="am-align-left">\n' +
            '                    <img class="am-radius" src="assets/i/bookicon1.png" alt="..."\n' +
            '                         width="140" height="140">\n' +
            '                </p>\n' +
            '                <!--书名作为h2标题-->\n' +
            '                <h2 style="margin: 2px 0 0 0">\n' +
            '                    <strong class="am-text-middle" href="">' + Name + '</strong>\n' +
            '                </h2>\n' +
            '                <!--书籍信息-->\n' +
            '                <p class="am-text-top" style="margin: 0 0 0 0">by\n' +
            '                    <strong>' + author + '</strong>\n' +
            '                </p>\n' +
            '                <p class="book-info-p am-text-middle am-text-sm">\n' +
            '                    Remain:\n' +
            '                    <em>' + inventory + '</em>\n' +
            '                <button class="am-btn am-btn-default am-btn-xs am-text-secondary am-align-right" type="button">\n' +
            '                <span class="am-icon-clock-o"></span> Reserve</button>\n' +
            '                </p>\n' +
            '                <p class="book-info-p am-text-middle am-text-xs">' + bookType + '</p>\n' +
            '                <p class="book-info-p am-text-middle am-text-xs">' + barCode + '</p>\n' +
            '            </li>\n')
    }


    function showBookPagination(data) {
        var totalPages = data.data.totalPages;
        var pagination = $('#bookPagination');
        pagination.empty()
        pagination.append('<li><a id="lastPage" href="#bookPane" onclick="">&laquo;</a></li>')
        for (var i = 1; i < totalPages + 1; i++) {
            if (i == pageNo) {
                pagination.append('<li class="am-active"><a class="pageNo" href="#bookPane" onclick="return false">' + i + '</a></li>\n')
            } else {
                pagination.append('<li><a class="pageNo" href="#bookPane" onclick="return false">' + i + '</a></li>\n')
            }
        }
        pagination.append('<li><a id="nextPage" href="#bookPane" onclick="">&raquo;</a></li>\n')
        // console.log(pageNo)
        if (pageNo == 1) {
            $('#lastPage').parent().addClass("am-disabled")
        }
        if (pageNo == totalPages) {
            $('#nextPage').parent().addClass("am-disabled")
        }
        $('#lastPage').click(function () {
            loadBookPage(Number(pageNo - 1), pageSize, keyStr)
        });
        $('#nextPage').click(function () {
            loadBookPage(Number(pageNo + 1), pageSize, keyStr)
        });
        pagination.find("a.pageNo").click(function () {
            // alert($(this).text())
            loadBookPage(Number($(this).text()), pageSize, keyStr)
        })
    }
}


