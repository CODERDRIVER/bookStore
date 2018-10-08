$('#addReader').click(function () {
    $('#prompt-title').text('Add Reader')
    $('#input-deposit').css('display','block')
    $('#addAccountPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var email = data[0];
            var name = data[1];
            var password = data[2];
            var deposit = data[3];
            var requestData = JSON.stringify({
                "email": email,
                "userName": name,
                "password": password,
                "deposit": deposit
            });
            addAccount(requestData, 'add/reader')
        },
        onCancel: function (e) {
        }
    })
});
$('#addLibrarian').click(function () {
    $('#prompt-title').text('Add Librarian')
    $('#input-deposit').css('display','none')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var email = data[0];
            var name = data[1];
            var password = data[2];
            var requestData = JSON.stringify({
                "email": email,
                "userName": name,
                "password": password,
            });
            addAccount(requestData, 'add/librarian')
        },
        onCancel: function (e) {
        }
    })
});

function addAccount(data, url) {
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            alert(e.message)
            location.reload()
        }
    })
}