// 获取借书记录列表
var borrowRecord = new Array();
$(document).ready(function(){ 				
    $.ajax({
        type:'get',
        dataType:'json',
        url:'/borrows/readerId',
        contentType:'application/json;charset=UTF-8',
        async: false,
        
        success:function(data){//返回结果
			console.log(data);
			var borrows = data.data;
            for(var i=0; i<borrows.length;i++){
                
                borrowRecord.push(new borrow(borrows[i].bookId,borrows[i].bookName,borrows[i].borrowDate,borrows[i].fine));
                }
            loadData();
                
        } 
    });
})

$(document).ready(function(){ 
    $("#home").click(function(){                  
        window.location.href="/";
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
var numberRowsInTable = theTable.rows.length;
// 数据条数
var numRows = getId("spanTotalNumRows");

var borrowRecord = new Array();
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));
// borrowRecord.push(new borrow("123", "某本书","2018/10/13 21：55","100"));

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

/* 创建借书记录对象 */
function borrow(bookId,bookName,borrowDate,fine) {
    this.bookId = bookId;
    this.bookName = bookName;
	this.borrowDate = borrowDate;
	this.fine = fine;
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
	for (var i = 0; i < borrowRecord.length; i++) {
		var bookId = borrowRecord[i].bookId;
        var bookName = borrowRecord[i].bookName;
        var fine = borrowRecord[i].fine;
        var borrowDate = borrowRecord[i].borrowDate;

		// 创建tr
		var tr = createObj("tr");
		// 创建td
		var serialTd = createObj("td");
        var bookIdTd = createObj("td");
        var bookNameTd = createObj("td");
        var borrowDateTd = createObj("td");
        var fineTd = createObj("td");
        var dmlTd = createObj("td");
		// 将获得的值添加到创建的指定Td中；
		var tbody = getId("tb");
		var rows = tbody.rows.length;
		// 将获得的信息添加到指定的为td中
		serialTd.innerHTML = rows + 1;
		bookNameTd.innerHTML = bookName;
        fineTd.innerHTML = fine;
        bookIdTd.innerHTML = bookId;
		borrowDateTd.innerHTML = borrowDate;

		// 创建个归还的button按钮，添加到操作列；
		var returnBtn = createObj("input");
		returnBtn.type = "button";
		returnBtn.value = "Return";
		// 为新建的returnBtn创建监听属性；
		returnBtn.onclick = function() {
			var flag = window.confirm("确定归还？");
			if (flag) {
					$.ajax({
						type: 'post',
						url: '/reader/return',
						data: {"bookId":bookId},
						dataType: "json",
						contentType: "application/json;charset=UTF-8",
						success: function (e) {
							console.log(e);
							alert(e.message);
						}
					})
				}
		};

		dmlTd.appendChild(returnBtn);
		// 将新建的td加入到新建的行中
		tr.appendChild(serialTd);
        tr.appendChild(bookIdTd);
        tr.appendChild(bookNameTd);
        tr.appendChild(borrowDateTd);
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