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

			var ISBN = getId("doc8").value;
			if (ISBN == '') {
				alert('ISBN can not be null！');
				return false;
			} 
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
                "bookUrl": bookImg,
                "author": author,
				"bookName": bookName,
				"bookType": bookType,
                "price": price,
				"description": description,
				"location":location,
            });
            addAccount(requestData, '/book/add')
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

/**
 * 给isbn 添加回车事件
 */
$("#doc8").bind("keypress",function (event) {
	if (event.keyCode == "13")
	{
		getBook();
	}

});

function getBook() {
	// alert("光标已经移出isbn");
	var isbn = getId("doc8").value;
	var url = 'https://api.douban.com/v2/book/isbn/'+isbn;
	$.ajax({
		url: url,
		dataType: 'jsonp',
		success:function (data) {
			console.log(data);
			getId("doc1").value = data.img;
			getId("doc2").value = data.author;
			getId("doc3").value = data.title;
			getId("doc5").value =  data.price;
		}
	});
}
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

		var checkBtn = createObj("input");
		checkBtn.type = "checkbox";
		checkBtn.value = bookId;
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
		var lookBtn = createObj("input");
		lookBtn.type = "button";
		lookBtn.value = "view";
		// 为新建的lookBtn创建监听属性；
		lookBtn.onclick = function() {
			lookTr(this);
		};

		var changeBtn = createObj("input");
		changeBtn.type = "button";
		changeBtn.value = "modify";

		// 为新建的changeBtn创建监听属性；
		changeBtn.onclick = function() {
			modTr(this);
		}
		dmlTd.appendChild(lookBtn);
		dmlTd.appendChild(changeBtn);
		// 将新建的td加入到新建的行中
		tr.appendChild(checkTd);
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

/* 增加书籍信息 */
var addTr = function() {
	// 做添加，首先获取输入的值
	var bookImg = getId("i1").value;
	if (bookImg == '') {
		alert('bookImg can not be null！');
		return false;
	} 

	var author = getId("i2").value;
	if (author == '') {
		alert('author can not be null！');
		return false;
	}
	var bookName = getId("i3").value;
	if (bookName == '') {
		alert('bookName can not be null！');
		return false;
	}
	var bookType = getId("i4").value;
	if (bookType == '') {
		alert('bookType can not be null！');
		return false;
	}
	var price = getId("i5").value;
	if (price == '') {
		alert('price can not be null！');
		return false;
	} else if (isNaN(price)) {
		alert("price is a number");
		return false;
	}
	var description = getId("i6").value;
	if (description == '') {
		alert('description can not be null！');
		return false;
	} 
	var location = getId("i7").value;
	if (location == '') {
		alert('location can not be null！');
		return false;
	} 
	// 存放学生信息
	message.push(new  book(bookId, bookImg, author, barcode, bookName, bookType, price,
				description,location));
	showHide1();
	getId("i1").value = "";
	getId("i2").value = "";
	getId("i3").value = "";
	getId("i4").value = "";
	getId("i5").value = "";
	getId("i6").value = "";
	getId("i7").value = "";
	getId("i8").value = "";
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
	var checkBtn = createObj("input");
	checkBtn.type = "checkbox";
	checkTd.appendChild(checkBtn);
	// 将获得的值添加到创建的指定Td中；
	var tbody = getId("tb");
	var rows = tbody.rows.length;
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
	var lookBtn = createObj("input");
	lookBtn.type = "button";
	lookBtn.value = "view";

	// 为新建的lookBtn创建监听属性；
	lookBtn.onclick = function() {
		lookTr(this);
	};

	var changeBtn = createObj("input");
	changeBtn.type = "button";
	changeBtn.value = "modify";
	// 为新建的changeBtn创建监听属性；
	changeBtn.onclick = function() {
		modTr(this);
	}
	dmlTd.appendChild(lookBtn);
	dmlTd.appendChild(changeBtn);
	// 将新建的td加入到新建的行中
	tr.appendChild(checkTd);
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
		var checkList = new Array();
		for (var i = inputs.length - 1; i >= 0; i--) {
			// 过滤出checkbox类型
			if (inputs[i].type == "checkbox") {
				var input = inputs[i];
				// 找出checkbox的所选择的行
				if (input.checked) {
					checkList.push(input.value);
					// 删除已选择的行
					tbody.removeChild(input.parentNode.parentNode);
					// table长度减一
					numberRowsInTable--;
				}
			}
		}

        $.ajax({
            type: "DELETE",
            url: "/book",
			data:{
                "bookIds":checkedList.join(",")
			},
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

/* 获取修改当前书籍信息 */
var modTr = function(obj) {
	var buttonConfirm = getId("hide");
	getId("lookMessages").innerHTML = "modify book information";
	buttonConfirm.style.display = "block";
	// 获得隐藏的DIV
	var overDiv = getId("over2");
	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	// 通过按钮来获得tr;
	var tr = obj.parentNode.parentNode;
	// 获得需要修改的内容
	serialTxt = tr.cells[1].innerHTML;
	var bookId = tr.cells[2].innerHTML;
	var bookImgTxt = tr.cells[3].innerHTML;
	var authorTxt = tr.cells[4].innerHTML;
	var barcodeTxt = tr.cells[5].innerHTML;
	var bookNameTxt = tr.cells[6].innerHTML;
	var bookTypeTxt = tr.cells[7].innerHTML;
	var priceTxt = tr.cells[8].innerHTML;
	var descriptionTxt = tr.cells[9].innerHTML;
	var locationTxt = tr.cells[10].innerHTML;
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
	var inputs = tb.getElementsByTagName("input");
	// 往遮罩层中的input填入从表格中取得来的数据
	// inputs[0].value = bookImgTxt;
	inputs[0].value = bookId;
	inputs[2].value = authorTxt;
	inputs[3].value = barcodeTxt;
	inputs[4].value = bookNameTxt;
	inputs[5].value = bookTypeTxt;
	inputs[6].value = priceTxt;
	inputs[7].value = descriptionTxt;
	inputs[8].value = locationTxt;
	inputs[0].disabled = true;
	for (var i=1;i<inputs.length;i++)
	{
		inputs[i].disabled = "";
	}

	// inputs[0].disabled = "";
	// inputs[1].disabled = "";
	// inputs[2].disabled = "";
	// inputs[3].disabled = "";
	// inputs[4].disabled = "";
	// inputs[5].disabled = "";
	// inputs[6].disabled = "";
	// inputs[7].disabled = "";
}

/* 查看书籍信息 */
var lookTr = function(obj) {
	// 获得隐藏的DIV
	var overDiv = getId("over2");
	var buttonConfirm = getId("hide");
	getId("lookMessages").innerHTML = "view book information";
	buttonConfirm.style.display = "none";

	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	// 通过按钮来获得tr;
	var tr = obj.parentNode.parentNode;
	// 获得需要查看的内容
	var bookImgTxt = tr.cells[3].innerHTML;
	var authorTxt = tr.cells[4].innerHTML;
	var barcodeTxt = tr.cells[5].innerHTML;
	var bookNameTxt = tr.cells[6].innerHTML;
	var bookTypeTxt = tr.cells[7].innerHTML;
	var priceTxt = tr.cells[8].innerHTML;
	var descriptionTxt = tr.cells[9].innerHTML;
	var locationTxt = tr.cells[10].innerHTML;
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
	var inputs = tb.getElementsByTagName("input");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = bookImgTxt;
	inputs[1].value = authorTxt;
	inputs[2].value = barcodeTxt;
	inputs[3].value = bookNameTxt;
	inputs[4].value = bookTypeTxt;
	inputs[5].value = priceTxt;
	inputs[6].value = descriptionTxt;
	inputs[7].value = locationTxt;
	inputs[0].disabled = "disabled";
	inputs[1].disabled = "disabled";
	inputs[2].disabled = "disabled";
	inputs[3].disabled = "disabled";
	inputs[4].disabled = "disabled";
	inputs[5].disabled = "disabled";
	inputs[6].disabled = "disabled";
	inputs[7].disabled = "disabled";
}

/* 确认修改按钮 */
var okBtn = function() {
	// 获得遮罩层中的tbody
	var tb = getId("over_tb2");
	// 获得tb中的所有的input的值，并且赋值给变量
	var inputs = tb.getElementsByTagName("input");
	var bookImgTxt = inputs[0].value;
	var authorTxt = inputs[1].value;
	var barcodeTxt = inputs[2].value;
	var bookNameTxt = inputs[3].value;
	var bookTypeTxt = inputs[4].value;
	var priceTxt = inputs[5].value;
	var descriptionTxt = inputs[6].value;
	var locationTxt = inputs[7].value;
	// 获得主页中的数据,将修改的数据填入到主页中,
	var tbody = getId("tb");
	var rows = tbody.rows.length; // 获得所有的行
	for (var i = 0; i < rows; i++) {
		var tr = tbody.rows[i];
		if (i + 1 == serialTxt) {
			if (tr.cells[3].innerHTML != bookImgTxt) {
				if (bookImgTxt == '') {
					alert('bookImg can not be null！');
					return false;
				} 
				tr.cells[3].innerHTML = bookImgTxt;
			}
			if (tr.cells[4].innerHTML != authorTxt) {
				if (authorTxt == '') {
					alert('author can not be null！');
					return false;
				}
				tr.cells[4].innerHTML = authorTxt;
			}
			if (tr.cells[5].innerHTML != barcodeTxt) {
				if (barcodeTxt == '') {
					alert('barcode can not be null！');
					return false;
				}
				tr.cells[5].innerHTML = barcodeTxt;
			}
			if (tr.cells[6].innerHTML != bookNameTxt) {
				if (bookNameTxt == '') {
					alert('bookName can not be null！');
					return false;
				}
				tr.cells[6].innerHTML = bookNameTxt;
			}
			if (tr.cells[7].innerHTML != bookTypeTxt) {
				if (bookTypeTxt == '') {
					alert('bookType can not be null！');
					return false;
				} 
				tr.cells[7].innerHTML = gradeTxt;
			}
			if (tr.cells[8].innerHTML != priceTxt) {
				if (priceTxt == '') {
					alert('price can not be null！');
					return false;
				} else if (isNaN(priceTxt)) {
					alert("price is a number");
					return false;
				}
				tr.cells[8].innerHTML = priceTxt;
			}
			if (tr.cells[9].innerHTML != descriptionTxt) {
				if (descriptionTxt == '') {
					alert('description can not be null！');
					return false;
				} 
				tr.cells[9].innerHTML = descriptionTxt;
			}
			if (tr.cells[10].innerHTML != locationTxt) {
				if (locationTxt == '') {
					alert('location can not be null！');
					return false;
				} 
				tr.cells[10].innerHTML = locationTxt;
			}
		}
	}
	$.ajax({
		type:'POST',
		dataType:'json',
		url:'/librarian/edit/book/',
		contentType:'application/json;charset=UTF-8',	
		data:{"bookId":bookId,"bookImg":bookImg,"author":author,"barcode":barcode,"bookName":bookName,
		"bookType":bookType,"price":price,"description":description,"location":location},
		success:function(data){//返回结果
				location.reload();
				alert("Success");
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