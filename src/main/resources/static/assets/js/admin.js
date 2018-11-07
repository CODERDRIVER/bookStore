$('#addLibrarian').click(function () {
  //  $('#prompt-title').text('Add Librarian')
    $('#input-deposit').css('display','none')
    $('#addAccountPrompt').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        closeOnConfirm:false,
        onConfirm: function (e) {
            var data = e.data;
            var email = data[0];
            var name = data[1];
			var password = data[2];
			

			var email = getId("doc1").value;
			if (email == '') {
				alert('email can not be null！');
				return false;
		    }

			var username = getId("doc2").value;
			if (username == '') {
				alert('username can not be null！');
				return false;
			} 

		    var password = getId("doc3").value;
			if (password == '') {
				alert('password can not be null！');
				return false;
			}
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


$('#changeFine').click(function () {
    $('#changeFinePrompt').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        closeOnConfirm:false,
        onConfirm: function (e) {
        	var fine = e.data;
        	if(fine == '')
			{
				alert("please set your fine");
			}else{
                var requestData = {
                    "fine": e.data,
                };
                addAccount(requestData, '/admin/book/fine');
			}

        },
        onCancel: function (e) {
        }
    })
});

$('#changeReturnperiod').click(function () {
    $('#changeReturnperiodPrompt').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        closeOnConfirm:false,
        onConfirm: function (e) {
        	var days  = e.data;
        	if (days == '')
			{
				alert("please set your returnPeriod");
			}else{
                var requestData = {
                    "days": e.data+"",
                };
                addAccount(requestData, '/admin/book/returnDate')
			}
        },
        onCancel: function (e) {
        }
    })
});

$('#changeDeposit').click(function () {
    $('#changeDepositPrompt').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        closeOnConfirm:false,
        onConfirm: function (e) {
        	var deposit = e.data;
        	if (deposit == '')
			{
				alert("please set your deposit");
			}else{
                var requestData = {
                    "deposit": e.data+"",
                };
                addAccount(requestData, '/admin/reader/deposit')
			}

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
			window.location.reload();
			// loadData();
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
  });
//搜索账户 根据用户名 邮箱
function searchAccount()
{
	var key = $("#account-key").val();
    $.ajax({
        type: 'get',
        url: "/admin/exact/accounts?key="+key,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
			var accounts = e.data;
            var tbodys = $("#table")[0].getElementsByTagName("tbody");
            var len = tbodys.length;
            for (var i=0;i<len;i++)
            {
                $("#table")[0].removeChild(tbodys[0]);
            }
            var librarianlist = new Array();
            for(var i=0; i<accounts.length;i++){

                librarianlist.push(new librarian(accounts[i].id,accounts[i].userName,accounts[i].email,accounts[i].password));
            }
            loadData(librarianlist);
        }
    })
}
// 获取图书管理员列表


$(document).ready(function(){ 				
    $.ajax({
            type:'GET',
            dataType:'json',
            url:'/admin/librarians',
            contentType:'application/json;charset=UTF-8',
            async: false,
            
            success:function(data){//返回结果
				console.log(data);
                var librarianlist = new Array();
                for(var i=0; i<data.length;i++){
                    
                    librarianlist.push(new librarian(data[i].id,data[i].userName,data[i].email,data[i].password));
                    }
                loadData(librarianlist);

            } 
            
        });	
        
})

 
var getId = function(id) {
	return document.getElementById(id);
}

/* 动态创建标签 */
var createObj = function(tagName) {
	return document.createElement(tagName);
}

var serialTxt = 0;
// 总页数
var totalPage = getId("spanTotalPage");
// 页号
var pageNum = getId("spanPageNum");
// 获取上翻按钮
var spanPre = getId("spanPre");
// 获取下翻按钮
var spanNext = getId("spanNext");
var pageSize = 10;
var page = 1;
var theTable = getId("tb");
// 获取行的长度
// var numberRowsInTable = theTable.rows.length;
// 数据条数
var numRows = getId("spanTotalNumRows");

// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));
// librarianlist.push(new librarian("123456", "赵云","2454779230@qq.com"));


/* 显示增加窗体 */
var showHide = function(obj) {
	var overDiv = getId("over");
	overDiv.style.display = "block";
}

/* 关闭新增窗体 */
var showHide1 = function(obj) {
	var overDiv = getId("over");
	overDiv.style.display = "none";
}

/* 关闭修改窗体 */
var showHide2 = function(obj) {
	var overDiv = getId("over2");
	overDiv.style.display = "none";
}



/* 创建读者对象 */
function librarian(librarianId,username,email,password) {
	this.librarianId = librarianId;
    this.username = username;
    this.email = email;
    this.password = password;
}


var changeColor = function() {
	// 隔行换色。
	var table = document.getElementById("table");
	for (var j = 0; j < table.tBodies[0].rows.length; j++) {
		if (j % 2 == 0) {
			table.tBodies[0].rows[j].style.background = "#FFFFFF";
			table.tBodies[0].rows[j].onmouseover = function() {
				this.style.background = "#E2E2E1";
			}
			table.tBodies[0].rows[j].onmouseout = function() {
				this.style.background = "#FFFFFF";
			}
		} else {
			table.tBodies[0].rows[j].style.background = "#EEF1F8";
			table.tBodies[0].rows[j].onmouseover = function() {
				this.style.background = "#E2E2E1";
			}
			table.tBodies[0].rows[j].onmouseout = function() {
				this.style.background = "#EEF1F8";
			}
		}
	}
}
// loadData();


/* 加载数据 */
function loadData(librarianlist) {
	for (var i = 0; i < librarianlist.length; i++) {
		var librarianId = librarianlist[i].librarianId;
        var username = librarianlist[i].username;
        var email = librarianlist[i].email;
		var password = librarianlist[i].password;
		$("#table").append('<tbody>'+
		'<tr>'+
		'<td><input type="checkbox" /></td>'+
		'<td>'+(i+1)+'</td>'+
		'<td>'+librarianId+'</td>'+
		'<td>'+username+'</td>'+
		'<td class="am-hide-sm-only">'+email+'</td>'+
		'<td>'+password+'</td>'+
		'<td>'+
			'<div class="am-btn-toolbar">'+
				'<button type="button" class="am-btn am-btn-default am-btn-xs am-text-secondary" onclick="modTr(this)"><span class="am-icon-pencil-square-o"></span> Edit</button>'+
			'</div>'+
		'</td></tr></tbody>');
	}
}

/* 增加读者信息 */
// var addTr = function() {
// 	// 做添加，首先获取输入的值
// 	var username = getId("i1").value;
// 	if (username == '') {
// 		alert('username can not be null！');
// 		return false;
// 	} 

// 	var email = getId("i2").value;
// 	if (email == '') {
// 		alert('email can not be null！');
// 		return false;
//     }
    
//     var phoneNumber = getId("i3").value;
// 	if (phoneNumber == '') {
// 		alert('phoneNumber can not be null！');
// 		return false;
// 	}
	
// 	// 存放公告信息
// 	message.push(new  announcement(title,content));
// 	showHide1();
// 	getId("i1").value = "";
// 	getId("i2").value = "";
//     getId("i3").value = "";

// 	// 创建tr
// 	var tr = createObj("tr");
// 	// 创建td
// 	var checkTd = createObj("td");
//     var serialTd = createObj("td");
//     var librarianIdTd = createObj("td");
//     var usernameTd = createObj("td");
//     var emailTd = createObj("td");
//     var dmlTd = createObj("td");
// 	var checkBtn = createObj("input");
// 	checkBtn.type = "checkbox";
// 	checkTd.appendChild(checkBtn);
// 	// 将获得的值添加到创建的指定Td中；
// 	var tbody = getId("tb");
// 	var rows = tbody.rows.length;
// 	serialTd.innerHTML = rows + 1;
//     librarianIdTd.innerHTML = librarianId;
//     usernameTd.innerHTML = username;
//     emailTd.innerHTML = email;
   

//     // 创建个button按钮，添加到操作列；
//     var lookBtn = createObj("input");
//     lookBtn.type = "button";
//     lookBtn.value = "View";
//     // 为新建的lookBtn创建监听属性；
//     lookBtn.onclick = function() {
//         lookTr(this);
//     };

//     // 创建个button按钮，添加到操作列；
//     var changeBtn = createObj("input");
//     changeBtn.type = "button";
//     changeBtn.value = "Edit";

//     // 为新建的changeBtn创建监听属性；
//     changeBtn.onclick = function() {
//         modTr(this);
//     }

//     dmlTd.appendChild(lookBtn);
//     dmlTd.appendChild(changeBtn);

//     // 将新建的td加入到新建的行中
//     tr.appendChild(checkTd);
//     tr.appendChild(serialTd);
//     tr.appendChild(librarianIdTd);
//     tr.appendChild(usernameTd);
//     tr.appendChild(emailTd);
//     tr.appendChild(dmlTd);

    
// 	// 将新建的tr加入到tbody中
// 	var tbody = getId("tb");
// 	tbody.appendChild(tr);

// 	var table = document.getElementById("table");
// 	// 隔行换色。
// 	changeColor();
// 	table.tBodies[0].rows[table.tBodies[0].rows.length - 1].style.display = 'none';
// 	numberRowsInTable++;
// 	totalPage.innerHTML = pageCount();
// 	numRows.innerHTML = numberRowsInTable;
// 	last();
// }

/* 删除所选 */
var delSel = function() {
	var flag = window.confirm("确定删除？");
	var notDelete = numberRowsInTable;
	if (flag) {
		// 获得tbody对象
		var tbody = getId("tb");
		// 获得tbody下的input元素
		var preDelete = 0;
		var inputs = tbody.getElementsByTagName("input");
		for (var i = inputs.length - 1; i >= 0; i--) {
			if (inputs[i].type == "checkbox") {
				if (inputs[i].checked) {
					preDelete = i;
				}
			}
		}

		var checkedList = new Array();
		for (var i = inputs.length - 1; i >= 0; i--) {
			// 过滤出checkbox类型
			if (inputs[i].type == "checkbox") {
				var input = inputs[i];
				// 找出checkbox的所选择的行
				if (input.checked) {
					checkedList.push(input.value);
					// 删除已选择的行
					tbody.removeChild(input.parentNode.parentNode);
					// table长度减一
					numberRowsInTable--;
				}
			}
		}
		$.ajax({                      
			type: "DELETE",
			url: "/admin/delete/librarian/id/",
			data: {'ids':checkedList.join(",")},
			success: function(data) {
				    location.reload();
				},
			error:function(data){
				 art.dialog.tips('删除失败!');
			 }
			});     

		numRows.innerHTML = numberRowsInTable;
		var rows = tbody.rows.length;
		for (var i = 0; i < rows; i++) {
			var tr = tbody.rows[i];
			tr.cells[1].innerHTML = i + 1;
		}
		// 页数
		totalPage.innerHTML = pageCount();

		for (var i = 0; i < numberRowsInTable; i++) {
			theTable.rows[i].style.display = 'none';
		}

		var locationPage = Math.floor(preDelete / (3 * pageSize));
		if (preDelete % (3 * pageSize) == 0
				&& preDelete / 3 == numberRowsInTable) {
			locationPage--;
		}

		for (var j = locationPage * pageSize; j < locationPage * pageSize
				+ pageSize
				&& j < numberRowsInTable; j++) {
			theTable.rows[j].style.display = '';
		}

		pageNum.innerHTML = locationPage + 1;
		page = locationPage + 1;

		if (numberRowsInTable == 0) {
			pageNum.innerHTML = 0;
		}
		if (locationPage == 0) {
			preText();
			nextLink();
		} else if (locationPage + 1 == pageCount()) {
			nextText();
		} else {
			preLink();
			nextLink();
		}
		if (numberRowsInTable <= pageSize) {
			nextText();
		}
		changeColor();
	}
	if (notDelete == numberRowsInTable) {
		alert("删除为空！请重新选择：");
	}
}

/* 全选 */
var selAll = function() {
	var thead = getId("thead");
	var tbody = getId("tb");
	// 获取全部的input元，素返回一个集合；
	var inputs = tbody.getElementsByTagName("input");
	var check = thead.getElementsByTagName("input");
	if (check[0].checked) {
		for (var i = (page - 1) * (3 * pageSize); i < inputs.length
				&& i < (page - 1) * (3 * pageSize) + (3 * pageSize); i += 3) {
			var input = inputs[i];

			if (input.type == "checkbox") {
				// 设置checkbox为已选
				input.checked = true;
			}
		}
	} else {
		for (var i = (page - 1) * (3 * pageSize); i < inputs.length
				&& i < (page - 1) * (3 * pageSize) + (3 * pageSize); i += 3) {
			var input = inputs[i];

			if (input.type == "checkbox") {
				// 设置checkbox为已选
				input.checked = false;
			}
		}
	}

}


/* 修改管理员账户信息 */
var modTr = function(e) {
    var tds = e.parentNode.parentNode.parentNode.childNodes;
    $('#edit-account-title').text('Edit Librarian');
    var inputs = $("#editAccountForm").find("input");
    /**
     * var readerId = tr.cells[2].innerHTML;
     var username = tr.cells[3].innerHTML;
     var email = tr.cells[4].innerHTML;
     var phoneNumber = tr.cells[5].innerHTML;
     */
    inputs[0].value = tds[2].innerHTML;	//readerId
    inputs[1].value = tds[3].innerHTML;	//UserName
    inputs[2].value = tds[4].innerHTML;	//Email
    inputs[3].value = tds[5].innerHTML;	//Password
    $('#editAccountPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var librarianId = data[0];
            var username = data[1];
            var email = data[2];
            var password = data[3];
            $.ajax({
                type:'POST',
                dataType:'json',
                url:'/admin/edit/librarian/',
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify({"id":librarianId,"userName":username,"email":email,"password":password}),
                success:function(data){//返回结果
                    location.reload();
                    alert("Success");
                },
                error:function(data){
                    alert('更新修改数据失败!');
                }
            });
        },
        onCancel: function (e) {
        }
    });
}

// /* 查看公告信息 */
// var lookTr = function(obj) {
// 	// 获得隐藏的DIV
// 	var overDiv = getId("over2");
// 	var buttonConfirm = getId("hide");
// 	getId("lookMessages").innerHTML = "View account";
// 	buttonConfirm.style.display = "none";
//
// 	// 将隐藏的div有隐藏显现出来
// 	overDiv.style.display = "block";
// 	// 通过按钮来获得tr;
// 	var tr = obj.parentNode.parentNode;
//
//     // 获得需要修改的内容
// 	var librarianId = tr.cells[2].innerHTML;
// 	var username = tr.cells[3].innerHTML;
//     var email = tr.cells[4].innerHTML;
// 	// 获得遮罩层的tbody
// 	var tb = getId("over_tb2");
// 	// 获得tb中所有的input
//     var inputs = tb.getElementsByTagName("input");
// 	// 往遮罩层中的input填入从表格中取得来的数据
// 	inputs[0].value = librarianId;
//     inputs[1].value = username;
//     inputs[2].value = email;
// 	inputs[0].disabled = "disabled";
//     inputs[1].disabled = "disabled";
//     inputs[2].disabled = "disabled";
//
// }

/* 确认修改按钮 */
var okBtn = function() {
	// 获得遮罩层中的tbody
	var tb = getId("over_tb2");
	// 获得tb中的所有的input的值，并且赋值给变量
	var inputs = tb.getElementsByTagName("input");
	var librarianId = inputs[0].value;
	var username = inputs[1].value;
	var email = inputs[2].value;
	var password = inputs[3].value;
	// 获得主页中的数据,将修改的数据填入到主页中,
	// var tbody = getId("tb");
	// var rows = tbody.rows.length; // 获得所有的行
	// for (var i = 0; i < rows; i++) {
	// 	var tr = tbody.rows[i];
	// 	if (i + 1 == serialTxt) {
	//
	// 		if (tr.cells[3].innerHTML != username) {
	// 			if ( username== '') {
	// 				alert('username can not be null！');
	// 				return false;
	// 			}
	// 			tr.cells[3].innerHTML = username;
     //        }
     //
     //        if (tr.cells[4].innerHTML != email) {
	// 			if ( email== '') {
	// 				alert('email can not be null！');
	// 				return false;
	// 			}
	// 			tr.cells[4].innerHTML = email;
	// 		}
    //
	// 		if (tr.cells[5].innerHTML != password) {
     //            if ( password == '') {
     //                alert('password can not be null！');
     //                return false;
     //            }
     //            tr.cells[5].innerHTML = password;
     //        }
    //
	// 	}
	// }
	$.ajax({
		type:'POST',
		dataType:'json',
		url:'/admin/edit/librarian/',
		contentType:'application/json;charset=UTF-8',	
		data:JSON.stringify({"id":librarianId,"userName":username,"email":email,"password":password}),
		success:function(data){//返回结果
				location.reload();
				alert("Success");
		  },
	    error:function(data){
			alert('更新修改数据失败!');
		}
	     });
	// 隐藏遮罩层
	showHide2();
}

/* 下一页 */
// function next() {

// 	hideTable();

// 	currentRow = pageSize * page;
// 	maxRow = currentRow + pageSize;
// 	if (maxRow > numberRowsInTable)
// 		maxRow = numberRowsInTable;
// 	for (var i = currentRow; i < maxRow; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
// 	page++;
// 	if (maxRow == numberRowsInTable) {
// 		nextText();
// 		lastText();
// 	}
// 	showPage();
// 	preLink();
// 	firstLink();
// }

// /* 上一页 */
// function pre() {

// 	hideTable();

// 	page--;

// 	currentRow = pageSize * page;
// 	maxRow = currentRow - pageSize;
// 	if (currentRow > numberRowsInTable)
// 		currentRow = numberRowsInTable;
// 	for (var i = maxRow; i < currentRow; i++) {
// 		theTable.rows[i].style.display = '';
// 	}

// 	if (maxRow == 0) {
// 		preText();
// 		firstText();
// 	}

// 	showPage();
// 	nextLink();
// 	lastLink();
// }

// /* 第一页 */
// function first() {
// 	hideTable();
// 	page = 1;
// 	for (var i = 0; i < pageSize && i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
// 	showPage();

// 	preText();
// 	nextLink();
// 	lastLink();
// }

// /* 最后一页 */
// function last() {
// 	hideTable();
// 	page = pageCount();
// 	currentRow = pageSize * (page - 1);
// 	for (var i = currentRow; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
// 	showPage();

// 	preLink();
// 	nextText();
// 	firstLink();
// }

// /* 隐藏table */
// function hideTable() {
// 	for (var i = 0; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = 'none';
// 	}
// }

// /* 展示第几页 */
// function showPage() {
// 	pageNum.innerHTML = page;
// }

// /* 总共页数 */
// function pageCount() {
// 	var count = 1;
// 	if (numberRowsInTable % pageSize != 0)
// 		count = 1;
// 	return parseInt(numberRowsInTable / (pageSize + 0.1)) + count;
// }

// /* 显示链接 */
// function preLink() {
// 	spanPre.innerHTML = "<a class='upLink' href='javascript:pre();'>pre page</a>";
// }
// function preText() {
// 	spanPre.innerHTML = "pre page";
// }

// function nextLink() {
// 	spanNext.innerHTML = "<a class='downLink' href='javascript:next();'>next page</a>";
// }
// function nextText() {
// 	spanNext.innerHTML = "next page";
// }

// function firstLink() {
// 	spanFirst.innerHTML = "<a href='javascript:first();'>firstPage , </a>";
// }
// function firstText() {
// 	spanFirst.innerHTML = "firstPage , ";
// }

// function lastLink() {
// 	spanLast.innerHTML = "<a href='javascript:last();'>lastPage , </a>";
// }
// function lastText() {
// 	spanLast.innerHTML = "lastPage , ";
// }

// /* 隐藏表格 */
// function hide() {
// 	for (var i = pageSize; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = 'none';
// 	}

// 	totalPage.innerHTML = pageCount();
// 	pageNum.innerHTML = '1';
// 	numRows.innerHTML = numberRowsInTable;

// 	nextLink();
// 	lastLink();
// }

// hide();

// /*显示时特效*/
// $(document).ready(function() {
// 	$("#show").click(function() {
// 		$(".over").show("slow");
// 	});
// });

// /*隐藏时特效*/
// $(document).ready(function() {
// 	$("#hide23").click(function() {
// 		$(".over").hide("slow");
// 	});
// });