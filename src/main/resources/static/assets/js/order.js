$('#addBook').click(function () {
    $('#prompt-title').text('Add Book')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var bookImg = data[0];
            var author = data[1];
			var bookName = data[2];
			var bookType = data[3];
            var price = data[4];
			var description = data[5];
			var location = data[6];

			var bookImg = getId("doc1").value;
			if (bookImg == '') {
				alert('bookImg can not be null！');
				return false;
			} 

			var author = getId("doc2").value;
			if (author == '') {
				alert('author can not be null！');
				return false;
			}
			var bookName = getId("doc3").value;
			if (bookName == '') {
				alert('bookName can not be null！');
				return false;
			}
			var bookType = getId("doc4").value;
			if (bookType == '') {
				alert('bookType can not be null！');
				return false;
			}
			var price = getId("doc5").value;
			if (price == '') {
				alert('price can not be null！');
				return false;
			} else if (isNaN(price)) {
				alert("price is a number");
				return false;
			}
			var description = getId("doc6").value;
			if (description == '') {
				alert('description can not be null！');
				return false;
			} 
			var location = getId("doc7").value;
			if (location == '') {
				alert('location can not be null！');
				return false;
			} 
            var requestData = JSON.stringify({
                "bookImg": bookImg,
                "author": author,
				"bookName": bookName,
				"bookType": bookType,
                "price": price,
				"description": description,
				"location":location,
            });
            addAccount(requestData, 'add/book')
        },
        onCancel: function (e) {
        }
    })
});

// 获取书籍信息列表
var message = new Array();
$(document).ready(function(){ 				
         $.ajax({
					type:'GET',
					dataType:'json',
					url:'/books',
					contentType:'application/json;charset=UTF-8',
					async: false,
					success:function(data){//返回结果
                        //{"content":[{"bookId":1,"bookName":"四级词汇","price":999.0,"inventory":3,"location":"120201","author":"卢根","bookType":"英语类","barCode":"12345678","status":1,"bookUrl":"www.baidu.com","description":"ojsjfoiajosdifjoasjfo"}]
						var books = []
						books =data.data.content
						console.log(data.data.content);
						for(var i=0; i<books.length;i++){
							
							message.push(new book(books[i].bookId,books[i].bookUrl, books[i].author,
                                books[i].barCode,books[i].bookName,books[i].bookType, books[i].price,
                                books[i].description, books[i].location));
							}
                        loadData();
							
					} 
					
				});	
})

$(document).ready(function(){ 
    $("#home").click(function(){                  
        window.location.href="reader.html";
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
// 书籍信息
var message = new Array();
// message.push(new book(11503080101, "bookicon2.png","齐桓公", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080102,"bookicon2.png", "鲍叔牙", "12345678", "软件工程", "技术类", 3,"讲述相关技术", 21));
// message.push(new book(11503080103, "bookicon2.png","烛之武", "12345678", "软件工程", "技术类", 4,"讲述相关技术", 21));
// message.push(new book(11503080104, "bookicon2.png","孙膑", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 20));
// message.push(new book(11503080105,"bookicon2.png", "赵括", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 20));
// message.push(new book(11503080106,"bookicon2.png", "韩非", "12345678", "软件工程", "技术类", 2, "讲述相关技术",21));
// message.push(new book(11503080107, "../assets/i/bookicon2.png","孟子", "12345678", "软件工程", "技术类", 3, "讲述相关技术",22));
// message.push(new book(11503080108, "../assets/i/bookicon2.png","荀子", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080109, "../assets/i/bookicon2.png","吕不韦", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 22));
// message.push(new book(11503080110, "../assets/i/bookicon2.png","吴广", "12345678", "软件工程", "技术类", 2, "讲述相关技术",21));
// message.push(new book(11503080111, "../assets/i/bookicon2.png","樊哙", "12345678", "软件工程", "技术类", 2, "讲述相关技术",21));
// message.push(new book(11503080112, "../assets/i/bookicon2.png","嫪毐", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080113,"../assets/i/bookicon2.png", "项庄", "12345678", "软件工程", "技术类", 4,"讲述相关技术", 19));
// message.push(new book(11503080114,"../assets/i/bookicon2.png", "刘邦", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080115, "../assets/i/bookicon2.png","袁术", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080116, "../assets/i/bookicon2.png","吕禄", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 19));
// message.push(new book(11503080117, "../assets/i/bookicon2.png","孙权", "12345678", "软件工程", "技术类", 3,"讲述相关技术", 21));
// message.push(new book(11503080118, "../assets/i/bookicon2.png","曹操", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080119,"../assets/i/bookicon2.png", "刘备", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080120,"../assets/i/bookicon2.png", "黄忠", "12345678", "软件工程", "技术类", 1,"讲述相关技术", 21));
// message.push(new book(11503080121,"../assets/i/bookicon2.png", "马超", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 19));
// message.push(new book(11503080122,"../assets/i/bookicon2.png", "魏延", "12345678", "软件工程", "技术类", 1,"讲述相关技术", 21));
// message.push(new book(11503080123, "../assets/i/bookicon2.png","法正", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));
// message.push(new book(11503080124, "../assets/i/bookicon2.png","庞统", "12345678", "软件工程", "技术类", 2,"讲述相关技术", 21));

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

/* 创建书籍对象 */
function book(bookId,bookImg, author, barcode, bookName, bookType, price,
		description,location) {
	this.bookId = bookId;
	this.bookImg = bookImg;
	this.author = author;
	this.barcode = barcode;
	this.bookName = bookName;
	this.bookType = bookType;
	this.price = price;
	this.description = description;
	this.location = location;
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
	for (var i = 0; i < message.length; i++) {
		var bookId = message[i].bookId;
		var bookImg = message[i].bookImg;
		var author = message[i].author;
		var barcode = message[i].barcode;
		var bookName = message[i].bookName;
		var bookType = message[i].bookType;
		var price = message[i].price;
		var description = message[i].description;
		var location = message[i].location;
		// 创建tr
		var tr = createObj("tr");
		// 创建td
		var checkTd = createObj("td");
		var serialTd = createObj("td");
		var bookIdTd = createObj("td");
		var bookImgTd = createObj("td");
		var authorTd = createObj("td");
		var barcodeTd = createObj("td");
		var bookNameTd = createObj("td");
		var bookTypeTd = createObj("td");
		var priceTd = createObj("td");
		var descriptionTd = createObj("td");
		var locationTd = createObj("td");
		var dmlTd = createObj("td");

		// 将复选框添加到第一列；
		checkTd.appendChild(checkBtn);
		// 将获得的值添加到创建的指定Td中；
		var tbody = getId("tb");
		var rows = tbody.rows.length;
		// 将获得的信息添加到指定的为td中
		serialTd.innerHTML = rows + 1;
		bookIdTd.innerHTML = bookId;
		bookImgTd.innerHTML = bookImg;
		authorTd.innerHTML = author;
		barcodeTd.innerHTML = barcode;
		bookNameTd.innerHTML = bookName;
		bookTypeTd.innerHTML = bookType;
		priceTd.innerHTML = price;
		descriptionTd.innerHTML = description;
		locationTd.innerHTML = location;


		// 创建个button按钮，添加到操作列；
		var orderBtn = createObj("input");
		orderBtn.type = "button";
		orderBtn.value = "order";
		// 为新建的orderBtn创建监听属性；
		orderBtn.onclick = function() {
           $.ajax({
           		type:'POST',
           		dataType:'json',
           		url:'/order',
           		contentType:'application/json;charset=UTF-8',
           		data:{"bookId":bookId},
           		success:function(data){//返回结果
           				location.reload();
           		  },
           	    error:function(data){
           			art.dialog.tips('预约数据失败!');
           		}
           	});
		};

		dmlTd.appendChild(orderBtn);
		// 将新建的td加入到新建的行中
		tr.appendChild(serialTd);
		tr.appendChild(bookIdTd);
		tr.appendChild(bookImgTd);
		tr.appendChild(authorTd);
		tr.appendChild(barcodeTd);
		tr.appendChild(bookNameTd);
		tr.appendChild(bookTypeTd);
		tr.appendChild(priceTd);
		tr.appendChild(descriptionTd);
		tr.appendChild(locationTd);
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