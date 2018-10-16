$('#addReader').click(function () {
    $('#prompt-title').text('Add Book')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var userName = data[0];
            var email = data[1];
			var phoneNumber = data[2];

			var userName = getId("doc1").value;
			if (userName == '') {
				alert('userName can not be null！');
				return false;
			} 

			var email = getId("doc2").value;
			if (email == '') {
				alert('email can not be null！');
				return false;
			}
			var phoneNumber = getId("doc3").value;
			if (phoneNumber == '') {
				alert('phoneNumber can not be null！');
				return false;
			}
			
            var requestData = JSON.stringify({
                "userName": userName,
                "email": email,
				"phoneNumber": phoneNumber,
            });
            addAccount(requestData, 'add/reader')
        },
        onCancel: function (e) {
        }
    })
});
// 获取reader 列表
var readerlist = new Array();
$(document).ready(function(){ 				
    $.ajax({
            type:'GET',
            dataType:'json',
            url:'/readers',
            contentType:'application/json;charset=UTF-8',
            async: false,
            
            success:function(data){//返回结果
				readers = [];
				readers = data.data;
				console.log(readers);
                for(var i=0; i<readers.length;i++){
                    
                    readerlist.push(new reader(readers[i].readerId,readers[i].userName,readers[i].email,readers[i].phoneNumber));
                    }
                loadData();

            } 
            
        });	
        
})

$(document).ready(function(){ 
    $("#home").click(function(){                  
        window.location.href="librarian.html";                              
     });
 })
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
var numberRowsInTable = theTable.rows.length;
// 数据条数
var numRows = getId("spanTotalNumRows");

// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));
// readerlist.push(new reader("123456", "赵云","2454779230@qq.com","18293884567"));



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

/* 关闭修改窗体 */
var showHide5 = function(obj) {
	var overDiv = getId("over5");
	overDiv.style.display = "none";
}

/* 创建读者对象 */
function reader(readerId,username,email,phoneNumber) {
	this.readerId = readerId;
    this.username = username;
    this.email = email;
	this.phoneNumber = phoneNumber;
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


/* 加载数据 */
function loadData() {
	for (var i = 0; i < readerlist.length; i++) {
		var readerId = readerlist[i].readerId;
        var username = readerlist[i].username;
        var email = readerlist[i].email;
		var phoneNumber = readerlist[i].phoneNumber;
		// 创建tr
		var tr = createObj("tr");
		// 创建td
		var checkTd = createObj("td");
		var serialTd = createObj("td");
		var readerIdTd = createObj("td");
        var usernameTd = createObj("td");
        var emailTd = createObj("td");
        var phoneNumberTd = createObj("td");
        var borrowTd = createObj("td");
        var returnTd = createObj("td");
        var fineTd = createObj("td");
		var dmlTd = createObj("td");

		var checkBtn = createObj("input");
		checkBtn.type = "checkbox";
		checkBtn.value="${readerlist[i].readerId}";
		// 将复选框添加到第一列；
		checkTd.appendChild(checkBtn);
		// 将获得的值添加到创建的指定Td中；
		var tbody = getId("tb");
		var rows = tbody.rows.length;
		// 将获得的信息添加到指定的为td中
		serialTd.innerHTML = rows + 1;
		readerIdTd.innerHTML = readerId;
        usernameTd.innerHTML = username;
		emailTd.innerHTML = email;
		phoneNumberTd.innerHTML = phoneNumber;

        // 创建个button按钮，添加到借书记录列；
		var borrowBtn = createObj("input");
		borrowBtn.type = "button";
		borrowBtn.value = "Borrow";
		// 为新建的borrowBtn创建监听属性；
		borrowBtn.onclick = function() {
			borrowTr(this);
        };
        
        // 创建个button按钮，添加到还书记录列；
		var returnBtn = createObj("input");
		returnBtn.type = "button";
		returnBtn.value = "Return";
		// 为新建的returnBtn创建监听属性；
		returnBtn.onclick = function() {
			returnTr(this);
        };

        // 创建个button按钮，添加到罚金记录列；
		var fineBtn = createObj("input");
		fineBtn.type = "button";
		fineBtn.value = "Fine";
		// 为新建的fineBtn创建监听属性；
		fineBtn.onclick = function() {
			fineTr(this);
        };
        
		// 创建个button按钮，添加到操作列；
		var lookBtn = createObj("input");
		lookBtn.type = "button";
		lookBtn.value = "View";
		// 为新建的lookBtn创建监听属性；
		lookBtn.onclick = function() {
			lookTr(this);
		};

        // 创建个button按钮，添加到操作列；
		var changeBtn = createObj("input");
		changeBtn.type = "button";
		changeBtn.value = "Edit";

		// 为新建的changeBtn创建监听属性；
		changeBtn.onclick = function() {
			modTr(this);
        }
        
        borrowTd.appendChild(borrowBtn);
        returnTd.appendChild(returnBtn);
        fineTd.appendChild(fineBtn);
		dmlTd.appendChild(lookBtn);
        dmlTd.appendChild(changeBtn);
        
		// 将新建的td加入到新建的行中
		tr.appendChild(checkTd);
		tr.appendChild(serialTd);
		tr.appendChild(readerIdTd);
        tr.appendChild(usernameTd);
        tr.appendChild(emailTd);
        tr.appendChild(phoneNumberTd);
        tr.appendChild(borrowTd);
        tr.appendChild(returnTd);
        tr.appendChild(fineTd);
		tr.appendChild(dmlTd);

		// 将新建的tr加入到tbody中
		var tbody = getId("tb");
		tbody.appendChild(tr);

		// 隔行换色。
		var table = document.getElementById("table");
		table.tBodies[0].rows[table.tBodies[0].rows.length - 1].style.display = 'none';
		numberRowsInTable++;
		totalPage.innerHTML = pageCount();
		numRows.innerHTML = numberRowsInTable;
		first();
	}
	changeColor();
}

/* 增加读者信息 */
var addTr = function() {
	// 做添加，首先获取输入的值
	var username = getId("i1").value;
	if (username == '') {
		alert('username can not be null！');
		return false;
	} 

	var email = getId("i2").value;
	if (email == '') {
		alert('email can not be null！');
		return false;
    }
    
    var phoneNumber = getId("i3").value;
	if (phoneNumber == '') {
		alert('phoneNumber can not be null！');
		return false;
	}
	
	// 存放公告信息
	message.push(new  announcement(title,content));
	showHide1();
	getId("i1").value = "";
	getId("i2").value = "";
    getId("i3").value = "";

	// 创建tr
	var tr = createObj("tr");
	// 创建td
	var checkTd = createObj("td");
    var serialTd = createObj("td");
    var readerIdTd = createObj("td");
    var usernameTd = createObj("td");
    var emailTd = createObj("td");
    var phoneNumberTd = createObj("td");
    var borrowTd = createObj("td");
    var returnTd = createObj("td");
    var fineTd = createObj("td");
    var dmlTd = createObj("td");
	var checkBtn = createObj("input");
	checkBtn.type = "checkbox";
	checkTd.appendChild(checkBtn);
	// 将获得的值添加到创建的指定Td中；
	var tbody = getId("tb");
	var rows = tbody.rows.length;
	serialTd.innerHTML = rows + 1;
    readerIdTd.innerHTML = readerId;
    usernameTd.innerHTML = username;
    emailTd.innerHTML = email;
    phoneNumberTd.innerHTML = phoneNumber;
   

    // 创建个button按钮，添加到借书记录列；
    var borrowBtn = createObj("input");
    borrowBtn.type = "button";
    borrowBtn.value = "Borrow";
    // 为新建的borrowBtn创建监听属性；
    borrowBtn.onclick = function() {
        borrowTr(this);
    };

    // 创建个button按钮，添加到还书记录列；
    var returnBtn = createObj("input");
    returnBtn.type = "button";
    returnBtn.value = "Return";
    // 为新建的returnBtn创建监听属性；
    returnBtn.onclick = function() {
        returnTr(this);
    };

    // 创建个button按钮，添加到罚金记录列；
    var fineBtn = createObj("input");
    fineBtn.type = "button";
    fineBtn.value = "Fine";
    // 为新建的fineBtn创建监听属性；
    fineBtn.onclick = function() {
        fineTr(this);
    };

    // 创建个button按钮，添加到操作列；
    var lookBtn = createObj("input");
    lookBtn.type = "button";
    lookBtn.value = "View";
    // 为新建的lookBtn创建监听属性；
    lookBtn.onclick = function() {
        lookTr(this);
    };

    // 创建个button按钮，添加到操作列；
    var changeBtn = createObj("input");
    changeBtn.type = "button";
    changeBtn.value = "Edit";

    // 为新建的changeBtn创建监听属性；
    changeBtn.onclick = function() {
        modTr(this);
    }

    borrowTd.appendChild(borrowBtn);
    returnTd.appendChild(returnBtn);
    fineTd.appendChild(fineBtn);
    dmlTd.appendChild(lookBtn);
    dmlTd.appendChild(changeBtn);

    // 将新建的td加入到新建的行中
    tr.appendChild(checkTd);
    tr.appendChild(serialTd);
    tr.appendChild(readerIdTd);
    tr.appendChild(usernameTd);
    tr.appendChild(emailTd);
    tr.appendChild(phoneNumberTd);
    tr.appendChild(borrowTd);
    tr.appendChild(returnTd);
    tr.appendChild(fineTd);
    tr.appendChild(dmlTd);

    
	// 将新建的tr加入到tbody中
	var tbody = getId("tb");
	tbody.appendChild(tr);

	var table = document.getElementById("table");
	// 隔行换色。
	changeColor();
	table.tBodies[0].rows[table.tBodies[0].rows.length - 1].style.display = 'none';
	numberRowsInTable++;
	totalPage.innerHTML = pageCount();
	numRows.innerHTML = numberRowsInTable;
	last();
}

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
					checkedList.push($(this).val());
					// 删除已选择的行
					tbody.removeChild(input.parentNode.parentNode);
					// table长度减一
					numberRowsInTable--;
				}
			}
		}
		$.ajax({                      
			type: "POST",                      
			url: "deletemore",                      
			data: {'delitems':checkedList.toString()},                    
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

/* 获取借书记录信息 */
var borrowTr = function(obj) {
	window.location.href="borrow.html";
}

/* 获取还书记录信息 */
var returnTr = function(obj) {
	window.location.href="return.html";
}

/* 获取罚金记录信息 */
var fineTr = function(obj) {
	var readerId = "";
	var paidFine = "";
	var unpaidFine = "";
    $.ajax({
        type:'POST',
        dataType:'json',
        url:'/fine',
        contentType:'application/json;charset=UTF-8',
        async: false,
        
        success:function(data){//返回结果

			 readerId = data.readerId;
			 paidFine = data.paidFine;
			 unpaidFine = data.unpaidFine;
        }   
    });
	// 获得隐藏的DIV
	var overDiv = getId("over5");
	var buttonConfirm = getId("hide5");
	getId("lookMessages5").innerHTML = "view fine record";
	buttonConfirm.style.display = "none";

	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	
    
	// 获得遮罩层的tbody
	var tb = getId("over_tb5");
	// 获得tb中所有的input
    var inputs = tb.getElementsByTagName("input");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = readerId;
    inputs[1].value = paidFine;
    inputs[2].value = unpaidFine;
	inputs[0].disabled = "disabled";
    inputs[1].disabled = "disabled";
	inputs[2].disabled = "disabled";
	
	
}

/* 获取修改当前公告信息 */
var modTr = function(obj) {
	var buttonConfirm = getId("hide");
	getId("lookMessages").innerHTML = "Edit account";
	buttonConfirm.style.display = "block";
	// 获得隐藏的DIV
	var overDiv = getId("over2");
	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	// 通过按钮来获得tr;
	var tr = obj.parentNode.parentNode;
    
	// 获得需要修改的内容
	var readerId = tr.cells[2].innerHTML;
	var username = tr.cells[3].innerHTML;
    var email = tr.cells[4].innerHTML;
    var phoneNumber = tr.cells[5].innerHTML;
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
    var inputs = tb.getElementsByTagName("input");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = readerId;
	inputs[1].value = username;
    inputs[2].value = email;
    inputs[3].value = phoneNumber;
	inputs[0].disabled = "disabled";
    inputs[1].disabled = "";
	inputs[2].disabled = "";
	inputs[3].disabled = "";

}

/* 查看公告信息 */
var lookTr = function(obj) {
	// 获得隐藏的DIV
	var overDiv = getId("over2");
	var buttonConfirm = getId("hide");
	getId("lookMessages").innerHTML = "View account";
	buttonConfirm.style.display = "none";

	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	// 通过按钮来获得tr;
	var tr = obj.parentNode.parentNode;
    
	// 获得需要修改的内容
	var readerId = tr.cells[2].innerHTML;
	var username = tr.cells[3].innerHTML;
    var email = tr.cells[4].innerHTML;
    var phoneNumber = tr.cells[5].innerHTML;
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
    var inputs = tb.getElementsByTagName("input");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = readerId;
	inputs[1].value = username;
    inputs[2].value = email;
    inputs[3].value = phoneNumber;
	inputs[0].disabled = "disabled";
    inputs[1].disabled = "disabled";
	inputs[2].disabled = "disabled";
	inputs[3].disabled = "disabled";
	
}

/* 确认修改按钮 */
var okBtn = function() {
	// 获得遮罩层中的tbody
	var tb = getId("over_tb2");
	// 获得tb中的所有的input的值，并且赋值给变量
    var inputs = tb.getElementsByTagName("input");
	var readerId = inputs[0].value;
	var username = inputs[1].value;
	var email = inputs[2].value;
	var phoneNumber = inputs[3].value;
	
	// 获得主页中的数据,将修改的数据填入到主页中,
	var tbody = getId("tb");
	var rows = tbody.rows.length; // 获得所有的行
	for (var i = 0; i < rows; i++){
		var tr = tbody.rows[i];
		if (i + 1 == serialTxt) {
			if (tr.cells[3].innerHTML != username) {
				if (username == '') {
					alert('username can not be null！');
					return false;
				} 
				tr.cells[3].innerHTML = username;
			}
			if (tr.cells[4].innerHTML != email) {
				if (email == '') {
					alert('email can not be null！');
					return false;
				}
				tr.cells[4].innerHTML = email;
			}
			if (tr.cells[5].innerHTML != phoneNumber) {
				if (phoneNumber == '') {
					alert('phoneNumber can not be null！');
					return false;
				}
				tr.cells[5].innerHTML = phoneNumber;
			}

		}
	}
	$.ajax({
		type:'POST',
		dataType:'json',
		url:'/update',
		contentType:'application/json;charset=UTF-8',	
		data:JSON.stringify({"readerId":readerId,"username":username,"email":email,"phoneNumber":phoneNumber}),
		success:function(data){//返回结果
				location.reload();
		  },
	    error:function(data){
			art.dialog.tips('更新修改数据失败!');
		}
	});
	// 隐藏遮罩层
	showHide2();
}

/* 下一页 */
function next() {

	hideTable();

	currentRow = pageSize * page;
	maxRow = currentRow + pageSize;
	if (maxRow > numberRowsInTable)
		maxRow = numberRowsInTable;
	for (var i = currentRow; i < maxRow; i++) {
		theTable.rows[i].style.display = '';
	}
	page++;
	if (maxRow == numberRowsInTable) {
		nextText();
		lastText();
	}
	showPage();
	preLink();
	firstLink();
}

/* 上一页 */
function pre() {

	hideTable();

	page--;

	currentRow = pageSize * page;
	maxRow = currentRow - pageSize;
	if (currentRow > numberRowsInTable)
		currentRow = numberRowsInTable;
	for (var i = maxRow; i < currentRow; i++) {
		theTable.rows[i].style.display = '';
	}

	if (maxRow == 0) {
		preText();
		firstText();
	}

	showPage();
	nextLink();
	lastLink();
}

/* 第一页 */
function first() {
	hideTable();
	page = 1;
	for (var i = 0; i < pageSize && i < numberRowsInTable; i++) {
		theTable.rows[i].style.display = '';
	}
	showPage();

	preText();
	nextLink();
	lastLink();
}

/* 最后一页 */
function last() {
	hideTable();
	page = pageCount();
	currentRow = pageSize * (page - 1);
	for (var i = currentRow; i < numberRowsInTable; i++) {
		theTable.rows[i].style.display = '';
	}
	showPage();

	preLink();
	nextText();
	firstLink();
}

/* 隐藏table */
function hideTable() {
	for (var i = 0; i < numberRowsInTable; i++) {
		theTable.rows[i].style.display = 'none';
	}
}

/* 展示第几页 */
function showPage() {
	pageNum.innerHTML = page;
}

/* 总共页数 */
function pageCount() {
	var count = 1;
	if (numberRowsInTable % pageSize != 0)
		count = 1;
	return parseInt(numberRowsInTable / (pageSize + 0.1)) + count;
}

/* 显示链接 */
function preLink() {
	spanPre.innerHTML = "<a class='upLink' href='javascript:pre();'>pre page</a>";
}
function preText() {
	spanPre.innerHTML = "pre page";
}

function nextLink() {
	spanNext.innerHTML = "<a class='downLink' href='javascript:next();'>next page</a>";
}
function nextText() {
	spanNext.innerHTML = "next page";
}

function firstLink() {
	spanFirst.innerHTML = "<a href='javascript:first();'>firstPage , </a>";
}
function firstText() {
	spanFirst.innerHTML = "firstPage , ";
}

function lastLink() {
	spanLast.innerHTML = "<a href='javascript:last();'>lastPage , </a>";
}
function lastText() {
	spanLast.innerHTML = "lastPage , ";
}

/* 隐藏表格 */
function hide() {
	for (var i = pageSize; i < numberRowsInTable; i++) {
		theTable.rows[i].style.display = 'none';
	}

	totalPage.innerHTML = pageCount();
	pageNum.innerHTML = '1';
	numRows.innerHTML = numberRowsInTable;

	nextLink();
	lastLink();
}

hide();

/*显示时特效*/
$(document).ready(function() {
	$("#show").click(function() {
		$(".over").show("slow");
	});
});

/*隐藏时特效*/
$(document).ready(function() {
	$("#hide23").click(function() {
		$(".over").hide("slow");
	});
});