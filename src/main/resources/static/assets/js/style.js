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



