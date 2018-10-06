/*
    author: fzm
    date: 2018-9-25
 */

//login function
$(function () {
    $('#sign-in').on('click', function () {
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
                var type = $(':input[name="loginType"]').val();

                if (type === "admin") {
                    login(requestData, 'login/admin', "admin-index")
                } else if (type === "librarian") {
                    login(requestData, 'login/librarian')
                } else if (type === "reader") {
                    login(requestData, 'login/reader')
                }
            },
            onCancel: function (e) {

            }
        });
    });
});

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

$('#search-button').click(function () {
    $('#backGround').removeClass("get-full").addClass("get-small")
    var keyStr = $('#search-input').val()

    loadBookPage(1, 5, keyStr)

    $('#bookPane').css("display", "block")
});
$('#morePage').click(function () {
    loadBookPage(1, 10)
})

/**
 * 分页数据
 */
$(function () {
})


function loadBookPage(pageNo, pageSize, keyStr) {
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
            if (data.data.numberOfElements == 0) {
                $('#bookList').append('')
            } else {
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
        console.log(pageNo)
        if (pageNo == 1) {
            $('#lastPage').parent().addClass("am-disabled")
        } else if (pageNo == totalPages) {
            $('#nextPage').parent().addClass("am-disabled")
        }
        $('#lastPage').click(function () {
            loadBookPage(pageNo - 1, pageSize)
        });
        $('#nextPage').click(function () {
            loadBookPage(pageNo + 1, pageSize)
        });
        pagination.find("a.pageNo").click(function () {
            // alert($(this).text())
            loadBookPage($(this).text(), pageSize)
        })
    }
}


