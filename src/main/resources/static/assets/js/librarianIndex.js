
var display = $('.admin-main .librarian-display');
var categories = new Array();
var locations = new Array();
var cookieUtil = $.AMUI.utils.cookie;
/* 创建书籍对象 */
function book(bookId,bookImg, author, barcode, bookName, bookType, price,
              description,location,inventory,barCodeUrl) {
    this.bookId = bookId;
    this.bookImg = bookImg;
    this.author = author;
    this.barcode = barcode;
    this.bookName = bookName;
    this.bookType = bookType;
    this.price = price;
    this.description = description;
    this.location = location;
    this.inventory = inventory;
    this.barcodeUrl = barCodeUrl;
}
/* 创建读者对象 */
function reader(readerId,username,email,phoneNumber,password,paidFine,unpaidFine) {
    this.readerId = readerId;
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.paidFine = paidFine;
    this.unpaidFine = unpaidFine;
}
/* 创建借书记录对象 */
function order(orderId,readerId,bookId,bookName,orderDate,barCode,price) {
    this.orderId = orderId;
    this.bookId = bookId;
    this.bookName = bookName;
    this.orderDate = orderDate;
    this.readerId = readerId;
    this.barCode = barCode;
    this.price = price;
}
/* 创建借书记录对象 */
function borrow(borrowId,readerId,bookId,borrowDate) {
    this.borrowId = borrowId;
    this.readerId = readerId;
    this.bookId = bookId;
    this.borrowDate = borrowDate;
}
/* 创建还书记录对象 */
function returnlist(returnId,bookId,returnDate,readerId) {
	this.returnId = returnId;
    this.bookId = bookId;
    this.returnDate = returnDate;
    this.readerId = readerId;
}
/* 创建公告对象 */
function announcement(id,title,content) {
    this.id =id;
    this.title = title;
    this.content = content;
}
/* 创建书籍历史删除对象*/
function bookDeleteHistory(id,bookId,bookName,librarianId,deleteDate)
{
    this.id = id;
    this.bookId = bookId;
    this.bookName = bookName;
    this.librarainId = librarianId;
    this.deleteDate = deleteDate;
}
/* 书籍列表对象*/
function Category(id,name)
{
    this.id = id;
    this.name = name;
}
/*  书籍位置对象 */
function Location(id,name)
{
    this.id = id;
    this.name = name;
}
function Income(depositIncome,fineIncome)
{
    this.depositIncome = depositIncome;
    this.fineIncome = fineIncome;
}


function clickFun(index){
    display.css('display','none')
    $(display[index]).css('display','block');
    if(index==0){
        // 加载所有的书
        var tbodys = $("#book-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
		{
            $("#book-table")[0].removeChild(tbodys[0]);
		}
        loadCategoryAndLocation();
        loadBooks(0);
	}else if(index == 1){
        // 借书管理
        var tbodys = $("#borrow-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#borrow-table")[0].removeChild(tbodys[0]);
        }
        loadBooks(1);
	}else if(index==2){
		// 还书管理

    }else if(index==3)
    {
        // 书籍删除记录
        var tbodys = $("#book-delete-form")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#book-delete-form")[0].removeChild(tbodys[0]);
        }
        loadBookDeleteHistory();
    }else if(index==4){

	    // category and location manage
        var tbodys = $("#category-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#category-table")[0].removeChild(tbodys[0]);
        }
        var tbodys = $("#location-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#location-table")[0].removeChild(tbodys[0]);
        }
        loadLocation();
        loadCategory();
    } if(index ==5)
	{
		// Account Manager
        var tbodys = $("#reader-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#reader-table")[0].removeChild(tbodys[0]);
        }
		loadReaders();
	}else if(index == 6)
	{
		// Reader Reservation
        var tbodys = $("#reserve-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#reserve-table")[0].removeChild(tbodys[0]);
        }
        loadOrders();
	}else if(index == 7)
	{
		// Borrow History
        var tbodys = $("#borrow-history-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#borrow-history-table")[0].removeChild(tbodys[0]);
        }
		loadBorrowHistorys();
	}else if(index ==8)
	{
		// Return History
        var tbodys = $("#return-history-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#return-history-table")[0].removeChild(tbodys[0]);
        }
		loadReturnHistorys();
	}else if(index ==9)
	{
		// Announcement Manager
        var tbodys = $("#announcement-table")[0].getElementsByTagName("tbody");
        var len = tbodys.length;
        for (var i=0;i<len;i++)
        {
            $("#announcement-table")[0].removeChild(tbodys[0]);
        }
        loadAnnouncements();
	}else if(index==10)
    {
        $.ajax({
            type:'GET',
            dataType:'json',
            url:'/libralian/income/records',
            contentType:'application/json;charset=UTF-8',
            async: false,

            success:function(data){//返回结果
                incomes = data.data;
                console.log(data);
                var incomeList = new Array();
                for(var i=0; i<incomes.length;i++){

                    incomeList.push(new Income(incomes[i].deposit,incomes[i].fine));
                }
                loadIncome(incomeList);
            }
        });
    }
}

/**
 *  加载income 信息
 *  0 -- daily
 *  1 -- weekly
 *  2 --- montyly
 */
function loadIncome(incomelist)
{
    var dailyIncome = incomelist[0];
    var weeklyIncome = incomelist[1];
    var monthlyIncome = incomelist[2];

    // 填充html
    console.log($('#income-table').find('tbody'));

    // dailyTbody
    var dailyTbody = $('#income-table tbody')[0];
    dailyTbody.getElementsByTagName('th')[1].innerHTML=dailyIncome.depositIncome
    dailyTbody.getElementsByTagName('th')[2].innerHTML=dailyIncome.fineIncome;

    var weeklyTbody = $('#income-table tbody')[1];
    weeklyTbody.getElementsByTagName('th')[1].innerHTML=weeklyIncome.depositIncome
    weeklyTbody.getElementsByTagName('th')[2].innerHTML=weeklyIncome.fineIncome;

    var monthlyTbody = $('#income-table tbody')[2];
    monthlyTbody.getElementsByTagName('th')[1].innerHTML=monthlyIncome.depositIncome
    monthlyTbody.getElementsByTagName('th')[2].innerHTML=monthlyIncome.fineIncome;
}
function loadCategoryAndLocation()
{
    categories = new Array();
    locations = new Array();
    $.ajax({
        type: 'get',
        url: "/categories",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            datas = e.data;
            for(var i=0;i<datas.length;i++)
            {
                categories.push(new Category(datas[i].id,datas[i].name));
            }
        }
    });
    $.ajax({
        type: 'get',
        url: "/locations",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            datas = e.data;
            for(var i=0;i<datas.length;i++)
            {
                locations.push(new Location(datas[i].id,datas[i].name));
            }
        }
    });
}

$('#addNewBook').click(function () {

    /**
     *  查找出 所有的 category 和 location
     */
    //category-select
    //location-select
    console.log(categories);
    var select = $("#category-select")[0].getElementsByTagName("option");
    var len1 = select.length;
    for (var i=0;i<len1;i++)
    {
        $("#category-select")[0].removeChild(select[0]);
    }
    var location = $("#location-select")[0].getElementsByTagName("option");
    var len2 = location.length;
    for (var i=0;i<len2;i++)
    {
        $("#location-select")[0].removeChild(location[0]);
    }
    for(var i=0;i<categories.length;i++)
    {
        $("#category-select").append('<option value="'+categories[i].name+'">'+categories[i].name+'</option>')
    }
    for(var i=0;i<locations.length;i++)
    {
        $("#location-select").append('<option value="'+locations[i].name+'">'+locations[i].name+'</option>')
    }

	$('#prompt-title').text('Add Book')
    $('#addBookPrompt').modal({
		relatedTarget: this,
		
        onConfirm: function (e) {
			var data = e.data;
			console.log(data);
			var ISBN = data[0];
            var bookImg = data[1];
            var author = data[2];
			var bookName = data[3];
			var bookType = $("#category-select option:selected").text();
            var price = data[4];
			var description = data[5];
			var location = $("#location-select option:selected").text();
			var quantity = data[6];
            var requestData = JSON.stringify({
                "bookIsbn":ISBN,
                "bookUrl": bookImg,
                "author": author,
				"bookName": bookName,
				"bookType": bookType,
                "price": price,
				"description": description,
				"location":location,
				// "pubdate" :pubdate,
				"publisher" :publisher,
                "quantity":quantity
            });
            addBook(requestData, '/book/add')
        },
        onCancel: function (e) {
        }
    })
});
function addBook(data, url) {
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
	var isbn = document.getElementById("doc8").value;
	var url = 'https://api.douban.com/v2/book/isbn/'+isbn;
	$.ajax({
		url: url,
		dataType: 'jsonp',
		success:function (data) {
            console.log(data);
            pubdate = data.pubdate;
            pubdate = new Date(pubdate.replace(/-/g,"/"));
            publisher = data.publisher;
			getId("doc1").value = data.image;
			getId("doc2").value = data.author;
			getId("doc3").value = data.title;
			console.log(data.price.slice(0,-1));
			getId("doc5").value = parseFloat(data.price.slice(0,-1));
        }
	});
}
var getId = function(id)
{
    return document.getElementById(id);
}
// 获取书籍信息列表
$(document).ready(function(){
	 loadBooks(0);
    loadCategoryAndLocation();
})



$('#borrowButton').click(function () { 
    $('#borrowPrompt').modal({
        relatedTarget:this,
        //TODO
        onConfirm:{},
        onCancel:{}
    })
    
});
function loadBooks(type)
{
	var url = "/books";
	if(type==1)
	{
		url = "/borrow/books"
	}
    $.ajax({
        type:'GET',
        dataType:'json',
        url:url,
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            //{"content":[{"bookId":1,"bookName":"四级词汇","price":999.0,"inventory":3,"location":"120201","author":"卢根","bookType":"英语类","barCode":"12345678","status":1,"bookUrl":"www.baidu.com","description":"ojsjfoiajosdifjoasjfo"}]
            var books = []
            books =data.data.content
            console.log(data.data.content);
            var message = new Array();
            for(var i=0; i<books.length;i++){

                message.push(new book(books[i].bookId,books[i].bookUrl, books[i].author,
                    books[i].barCode,books[i].bookName,books[i].bookType, books[i].price,
                    books[i].description, books[i].location,books[i].inventory,books[i].barCodeUrl));
            }
            if(type==0)
			{
            	loadBookData(message);
			}else{
            	console.log(message);
            	loadBorrowBooks(message);
			}
        }

    });
}
/**
 *  加载所有的book列表
 */
function loadBookData(message) {
    for (var i = 0; i < message.length; i++) {
        var bookId = message[i].bookId;
        var author = message[i].author;
        var barcode = message[i].barcode;
        var bookName = message[i].bookName;
        var bookType = message[i].bookType;
        var price = message[i].price;
        var description = message[i].description;
        var location = message[i].location;
        var inventory = message[i].inventory;
        var barCodeUrl = message[i].barcodeUrl;
        $("#book-table").append('<tbody>'+
            '<tr>'+
            '<td><input type="checkbox" /></td>'+
            '<td>'+(i+1)+'</td>'+
            '<td>'+bookId+'</td>'+
            '<td>'+inventory+'</td>'+
            '<td>'+author+'</td>'+
            '<td><a name="'+barCodeUrl+'" onclick="showBarCode(this)">'+barcode+'</a></td>'+
            '<td>'+bookName+'</td>'+
            '<td>'+bookType+'</td>'+
            '<td>'+location+'</td>'+
            '<td>'+
            '<div class="am-btn-toolbar">'+
            ' <div class="am-btn-group am-btn-group-xs">\n' +
            '         <button type="button" onclick="modBook(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Edit</button>\n' +
            '         <button type="button" onclick="delBookItem(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Delete</button>\n' +
            '         </div>'+
            '</div>'+
            '</td></tr></tbody>');
    }
}

/**
 *  修改书籍内容
 */
function modBook(e)
{
    var tds = e.parentNode.parentNode.parentNode.parentNode.childNodes;
    $('#edit-book-title').text('Edit Book');
	var inputs = $("#editBookForm").find("input");
    /**
	 * var bookId = tr.cells[2].innerHTML;
     var inventoryTxt = tr.cells[3].innerHTML;
     var authorTxt = tr.cells[4].innerHTML;
     var barcodeTxt = tr.cells[5].innerHTML;
     var bookNameTxt = tr.cells[6].innerHTML;
     var bookTypeTxt = tr.cells[7].innerHTML;
     var priceTxt = tr.cells[8].innerHTML;
     var descriptionTxt = tr.cells[9].innerHTML;
     var locationTxt = tr.cells[10].innerHTML;
     */
	inputs[0].value = tds[2].innerHTML;	//bookId
	console.log(inputs[0].style);
	inputs[1].value = tds[3].innerHTML;	//Inventory
	inputs[2].value = tds[4].innerHTML;	//Author
	inputs[3].value = tds[5].innerHTML;	//BarCode
	inputs[4].value = tds[6].innerHTML;	//bookName
	inputs[5].value = tds[7].innerHTML;// bookType
	inputs[6].value = tds[8].innerHTML;// price
	// inputs[7].innerHTML = tds[9].innerHTML;// description
	inputs[8].value = tds[8].innerHTML;	//location
	console.log(tds[2].innerHTML);
    $('#editBookPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var bookId = data[0];
            var inventory = data[1];
            var author = data[2];
            var barCode = data[3];
            var bookName = data[4];
            var bookType = data[5];
            var price = data[6];
            var description = data[7];
            var location = data[8];
            // var requestData = JSON.stringify({
            //     "bookId":bookId,
				// "inventory":inventory,
				// "author":author,
				// "barCode":barCode,
				// "bookName":bookName,
				// "bookType":bookType,
				// "price":price,
				// "description":description,
				// "location":location
            // });
            $.ajax({
                type:'POST',
                dataType:'json',
                url:'/book/update/profile',
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify({"bookId":bookId,"inventory":inventory,"author":author,"barCode":barCode,"bookName":bookName,
                    "bookType":bookType,"price":parseFloat(price),"description":description,"location":location}),
                success:function(data){//返回结果
                    // location.reload();
                    alert("Success");
                    clickFun(0);
                },
                error:function(data){
                    alert('更新修改数据失败!');
                }
            });
        },
        onCancel: function (e) {
        }
    })
    console.log(tds);
}
function loadBorrowBooks(message)
{
	for (var i = 0; i < message.length; i++) {
		var barcode = message[i].barcode;
		var bookName = message[i].bookName;
		var price = message[i].price;
		var location = message[i].location;
		console.log($("#borrow-table"));
		$("#borrow-table").append('<tbody>'+
			'<tr>'+
			'<td><input type="checkbox" /></td>'+
			'<td>'+(i+1)+'</td>'+
			'<td>'+barcode+'</td>'+
			'<td>'+bookName+'</td>'+
			'<td>'+location+'</td>'+
			'<td>'+price+'</td>'+
			'<td>'+
			'<div class="am-btn-toolbar">'+
			' <div class="am-btn-group am-btn-group-xs">\n' +
			'         <button type="button" onclick="borrowBook(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Borrow</button>\n' +
			// '         <button type="button" onclick="detail(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Detail</button>\n' +
			'         </div>'+
			'</div>'+
			'</td></tr></tbody>');

    }
}

/**
 *  借书
 * @param e
 */
function borrowBook(e)
{
    var bookName = e.parentNode.parentNode.parentNode.parentNode.childNodes[3].innerHTML;
    console.log(bookName);
    $('#my-prompt').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            var phoneNumber = e.data;
            if(phoneNumber=="")
            {
                alert("please input your phoneNumber");
            }else{
                $.ajax({
                    type:'POST',
                    dataType:'json',
                    url:'/book/borrow',
                    contentType:'application/json;charset=UTF-8',
                    data:JSON.stringify({"bookName":bookName,"phoneNumber":phoneNumber}),
                    success:function(data){//返回结果
                        console.log(data);
                        // location.reload();
                        if(data.code==0)
                        {
                            // location.reload();
                            alert('借阅成功!');
                            clickFun(1);	//刷新页面
                        }else{
                            alert(data.message);
                            clickFun(1);	//刷新页面
                        }
                        // window.location.reload();
                    },
                    error:function(data){
                        alert('借阅失败!');
                    }
                });
            }

        },
        onCancel: function(e) {
            // alert('不想说!');
        }
    });
}

/**
 *  还书
 */
function returnBook(e)
{
    /**
	 *  获得 填写的内容
     */
	inputs = $("#return-form").find("input");
	var barCode = inputs[0].value;
	var bookId = inputs[1].value;
	var phoneNumber = inputs[2].value;
	if(bookId==''&&barCode=='')
	{
		alert("Please fill it barCode Or phoneNumber ");
	}
	if(phoneNumber=='')
    {
        alert("please fill phoneNumber");
    }
	else{
        $.ajax({
            type:'POST',
            dataType:'json',
            url:'/librarian/book/return',
            contentType:'application/json;charset=UTF-8',
            data:JSON.stringify({"bookId":bookId,"phoneNumber":phoneNumber,"barCode":barCode}),
            success:function(data){//返回结果
                console.log(data);
                // location.reload();
                if(data.code==0)
                {
                    // location.reload();
                    alert('归还成功!');

                }else{
                    alert(data.message);
                }
                clickFun(2)
                // window.location.reload();
            },
            error:function(data){
                alert('借阅失败!');
            }
        });
	}

}

/**
 *  加载书籍删除历史
 */
function loadBookDeleteHistory()
{
    $.ajax({
        type:'GET',
        dataType:'json',
        url:"/book/deleteRecords",
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            var books = []
            books =data.data
            console.log(data.data);
            var history = new Array();
            for(var i=0; i<books.length;i++){

                history.push(new bookDeleteHistory(books[i].id,books[i].bookId,books[i].bookName,books[i].librarainId,books[i].deleteDate));
            }
            loadBookDeleteHistoryData(history);
        }

    });
}

/**
 *   加载位置信息
 */
function loadLocation()
{
    $.ajax({
        type:'GET',
        dataType:'json',
        url:"/locations",
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            var books = []
            locations =data.data
            console.log(data.data);
            var locationList = new Array();
            for(var i=0; i<locations.length;i++){

                locationList.push(new Category(locations[i].id,locations[i].name));
            }
            loadLocationData(locationList);
        }

    });
}
function loadLocationData(locationList)
{
    for (var i = 0; i < locationList.length; i++) {
        var id = locationList[i].id;
        var name = locationList[i].name;
        $("#location-table").append('<tbody>'+
            '<tr>'+
            '<td>'+id+'</td>'+
            '<td>'+name+'</td>'+
            '<td>\n' +
            '                                                <div class="am-btn-toolbar">\n' +
            '                                                    <div class="am-btn-group am-btn-group-xs">\n' +
            '                                                        <button type="button" class="am-btn am-btn-default am-btn-xs am-text-secondary" onclick="editLocation(this)"><span class="am-icon-pencil-square-o"></span>\n' +
            '                                                            Edit</button>\n' +
            '                                                    </div>\n' +
            '                                                </div>\n' +
            '                                            </td>'+
            '</tr></tbody>');
    }
}

/**
 *  修改location
 */
function editLocation(e)
{
    var tr = e.parentNode.parentNode.parentNode.parentNode.childNodes;
    var id = tr[0].innerHTML;
    var name = tr[1].innerHTML;
    $("#location-input").val(name);
    $('#location-title').text('Edit Location')
    $('#location-prompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            console.log(data)
            var requestData = JSON.stringify({
                "name": data
            });
            addAccount(requestData, '/location/'+id,4);
        },
        onCancel: function (e) {
        }
    })
}
/**
 *  加载类别列表
 */
function loadCategory()
{
    $.ajax({
        type:'GET',
        dataType:'json',
        url:"/categories",
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            var books = []
            categories =data.data
            console.log(data.data);
            var categoryList = new Array();
            for(var i=0; i<categories.length;i++){

                categoryList.push(new Category(categories[i].id,categories[i].name));
            }
            loadCategoryData(categoryList);
        }

    });
}
function loadCategoryData(categoryList)
{
    for (var i = 0; i < categoryList.length; i++) {
        var id = categoryList[i].id;
        var name = categoryList[i].name;
        $("#category-table").append('<tbody>'+
            '<tr>'+
            '<td>'+id+'</td>'+
            '<td>'+name+'</td>'+
            '<td>\n' +
            '                                                <div class="am-btn-toolbar">\n' +
            '                                                    <div class="am-btn-group am-btn-group-xs">\n' +
            '                                                        <button type="button" onclick="editCategory(this)" class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span>\n' +
            '                                                            Edit</button>\n' +
            '                                                    </div>\n' +
            '                                                </div>\n' +
            '                                            </td>'+
            '</tr></tbody>');
    }
}

function editCategory(e)
{
    var tr = e.parentNode.parentNode.parentNode.parentNode.childNodes;
    var id = tr[0].innerHTML;
    var name = tr[1].innerHTML;
    $('#category-input').val(name);
    $('#category-title').text('Edit Category')
    $('#category-prompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            console.log(data)
            var requestData = JSON.stringify({
                "name": data
            });
            addAccount(requestData, '/category/'+id,4);
        },
        onCancel: function (e) {
        }
    })
}

function loadBookDeleteHistoryData(history)
{
    for (var i = 0; i < history.length; i++) {
        var id = history[i].id;
        var bookId = history[i].bookId;
        var bookName = history[i].bookName;
        var librarainId = history[i].librarainId;
        var deleteDate = history[i].deleteDate;
        $("#book-delete-table").append('<tbody>'+
            '<tr>'+
            '<td>'+(i+1)+'</td>'+
            '<td>'+id+'</td>'+
            '<td>'+bookId+'</td>'+
            '<td>'+bookName+'</td>'+
            '<td>'+librarainId+'</td>'+
            '<td>'+deleteDate+'</td>'+
           '</tr></tbody>');
    }
}
/**
 *  添加回车事件
 */
$("#book-search").keyup(function (e) {
	if (e.keyCode == 13)
	{
		searchBook();
	}
});
/**
 *  搜书
 */
function searchBook(){
    var key = document.getElementById("search-key").value;
    console.log(key);
    $("#tb").html("");
    $.ajax({
        type:'GET',
        dataType:'json',
        url:'/books?keyStr='+key,
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            //{"content":[{"bookId":1,"bookName":"四级词汇","price":999.0,"inventory":3,"location":"120201","author":"卢根","bookType":"英语类","barCode":"12345678","status":1,"bookUrl":"www.baidu.com","description":"ojsjfoiajosdifjoasjfo"}]
            var books = []
            books =data.data.content
            console.log(data.data.content);
            message = new Array();
            for(var i=0; i<books.length;i++){

                message.push(new book(books[i].bookId,books[i].bookUrl, books[i].author,
                    books[i].barCode,books[i].bookName,books[i].bookType, books[i].price,
                    books[i].description, books[i].location,books[i].inventory));
            }
            /**
			 *  清空数据
             */
            var tbodys = $("#borrow-table")[0].getElementsByTagName("tbody");
            var len = tbodys.length;
            for (var i=0;i<len;i++)
            {
                $("#borrow-table")[0].removeChild(tbodys[0]);
            }
            loadBorrowBooks(message);
        }

    });
}

/**
 *  加载所有的读者
 */
function loadReaders()
{
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
            var readerList = new Array();
            for(var i=0; i<readers.length;i++){

                readerList.push(new reader(readers[i].readerId,readers[i].userName,readers[i].email,readers[i].phoneNumber,readers[i].password,readers[i].paidFine,readers[i].unPaidFine));
            }
            loadReaderData(readerList);
        }

    });
}

function loadReaderData(readerList)
{
	for (var i = 0; i < readerList.length; i++) {
		var readerId = readerList[i].readerId;
		var username = readerList[i].username;
		var email = readerList[i].email;
		var phoneNumber = readerList[i].phoneNumber;
		var password = readerList[i].password;
		var paidFine = readerList[i].paidFine;
		var unpaidFine = readerList[i].unpaidFine;
		console.log($("#reader-table"));
		$("#reader-table").append('<tbody>'+
			'<tr>'+
			'<td>'+readerId+'</td>'+
			'<td>'+username+'</td>'+
			'<td>'+email+'</td>'+
			'<td>'+phoneNumber+'</td>'+
			'<td>'+password+'</td>'+
			'<td>'+paidFine+'</td>'+
			'<td>'+unpaidFine+'</td>'+
			'<td>'+
			'<div class="am-btn-toolbar">'+
			' <div class="am-btn-group am-btn-group-xs">\n' +
			'         <button type="button" onclick="modReader(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Edit</button>\n' +
			'         <button type="button" onclick="delReaderItem(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Delete</button>\n' +
			'         <button type="button" onclick="balanceAccount(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-money"></span> Balance</button>\n' +
			'         </div>'+
			'</div>'+
			'</td></tr></tbody>');
	}
}

/**
 *  修改读者信息
 */
function modReader(e)
{
    var tds = e.parentNode.parentNode.parentNode.parentNode.childNodes;
    $('#edit-account-title').text('Edit Reader');
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
    inputs[3].value = tds[5].innerHTML;	//PhoneNumber
    inputs[4].value = tds[6].innerHTML;	//Password
    $('#editAccountPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var readerId = data[0];
            var userName = data[1];
            var email = data[2];
            var phoneNumber = data[3];
            var password = data[4];
            $.ajax({
                type:'POST',
                dataType:'json',
                url:'/reader/edit',
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify(
                    {"readerId":readerId,"userName":userName,"email":email,"phoneNumber":phoneNumber,"password":password}),
                success:function(data){//返回结果
                    alert("success");
                    // location.reload();
                    clickFun(3);
                },
                error:function(data){
                    art.dialog.tips('更新修改数据失败!');
                }
            });
        },
        onCancel: function (e) {
        }
    })
}

function balanceAccount(e)
{
   var tr =  e.parentNode.parentNode.parentNode.parentNode.childNodes;
   var readerId = tr[0].innerHTML;
   var unpaidFine = tr[6].innerHTML;
   console.log(tr[6].innerHTML);
    $.ajax({
        type:'POST',
        dataType:'json',
        url:'/reader/'+readerId+'/unpaidFine',
        contentType:'application/json;charset=UTF-8',
        data:{
            "unpaidFine":unpaidFine
        },
        success:function(data){//返回结果
            if(data.code==0)
            {
                alert("success");
            }
            // location.reload();
            clickFun(3);
        },
        error:function(data){
            art.dialog.tips('更新修改数据失败!');
        }
    });
}
/**
 *  加载所有的预约记录
 */
function loadOrders()
{
    $.ajax({
        type:'get',
        dataType:'json',
        url:'/librarian/orders',
        contentType:'application/json;charset=UTF-8',
        async: false,

        success:function(data){//返回结果
            console.log(data);
            var borrows = data.data;
            var orderRecord = new Array();
            for(var i=0; i<borrows.length;i++){

                orderRecord.push(new order(borrows[i].orderId,borrows[i].readerId,borrows[i].bookId,borrows[i].bookName,borrows[i].orderDate,borrows[i].barCode,borrows[i].price));
            }
            loadOrderData(orderRecord);

        }
    });
}

function loadOrderData(orderRecord)
{
    for (var i = 0; i < orderRecord.length; i++) {
        var bookId = orderRecord[i].bookId;
        var bookName = orderRecord[i].bookName;
        var readerId = orderRecord[i].readerId;
        var orderDate = orderRecord[i].orderDate;
        var orderId = orderRecord[i].orderId;
        var barCode = orderRecord[i].barCode;
        var price = orderRecord[i].price;
        $("#reserve-table").append('<tbody>'+
            '<tr>'+
            '<td><input type="checkbox" /></td>'+
            '<td>'+(i+1)+'</td>'+
            '<td>'+orderId+'</td>'+
            '<td>'+readerId+'</td>'+
            '<td>'+barCode+'</td>'+
            '<td>'+bookName+'</td>'+
            '<td>'+price+'</td>'+
            '<td>'+
            '<div class="am-btn-toolbar">'+
            ' <div class="am-btn-group am-btn-group-xs">\n' +
            '         <button type="button" onclick="complete(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Complete</button>\n' +
            '         <button type="button" onclick="cancel(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Cancel</button>\n' +
            '         </div>'+
            '</div>'+
            '</td></tr></tbody>');

    }
}

/**
 *  完成点击按钮
 */
function complete(e)
{
    var tr = e.parentNode.parentNode.parentNode.parentNode;
    // var bookId = tr.childNodes[2].innerHTML;
    var readerId = tr.childNodes[3].innerHTML;
    var orderId = tr.childNodes[2].innerHTML;
    var barCode = tr.childNodes[4].innerHTML;
    var flag = window.confirm("确定同意该借阅请求？");
    if (flag) {
        $.ajax({
            type: 'post',
            url: '/librarian/confirm/bookOrder',
            data: JSON.stringify({
                "readerId":readerId,
                "orderId":orderId,
                "barCode":barCode
            }),
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function (e) {
                alert(e.message);
                clickFun(4);
            }
        })
    }
}

/**
 *  取消按钮
 */
function cancel(e)
{
    var tr = e.parentNode.parentNode.parentNode.parentNode;
    var bookId = tr.childNodes[2].innerHTML;
    var readerId = tr.childNodes[4].innerHTML;
    var orderId = tr.childNodes[6].innerHTML;
    var flag = window.confirm("确定拒绝该借阅请求？");
    if (flag) {
        $.ajax({
            type: 'post',
            url: '/librarian/reject/bookOrder',
            data: {
                "orderId":orderId,
                "readerId":readerId,
                "bookId":bookId
            },
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function (e) {
                console.log(e);
                alert(e.message);
            }
        })
    }
}

/**
 *  加载所有的借阅信息
 */
function loadBorrowHistorys()
{
    $.ajax({
        type:'get',
        dataType:'json',
        url:'/librarian/borrow/historys',
        contentType:'application/json;charset=UTF-8',
        async: false,

        success:function(data){//返回结果
            console.log(data);
            var borrows = data.data;
            var borrowRecord= new Array();
            for(var i=0; i<borrows.length;i++){

                borrowRecord.push(new borrow(borrows[i].borrowId,borrows[i].readerId,borrows[i].bookId,borrows[i].borrowDate));
            }
            loadBorrowHistoryData(borrowRecord);

        }
    });
}
function loadBorrowHistoryData(borrowRecord)
{
    for (var i = 0; i < borrowRecord.length; i++) {
        var bookId = borrowRecord[i].bookId;
        var borrowDate = borrowRecord[i].borrowDate;
        var borrowId = borrowRecord[i].borrowId;
        $("#borrow-history-table").append('<tbody>' +
            '<tr>' +
            '<td>'+(i+1)+'</td>\n' +
            '<td>'+borrowId+'</td>\n' +
            '<td>'+bookId+'</td>\n' +
            '<td>'+bookId+'</td>\n' +
            '<td>'+borrowDate+'</td>\n' +
            '</tr></tbody>');
    }
}

/**
 *  加载所有的归还信息
 */
function loadReturnHistorys()
{
    $.ajax({
        type:'GET',
        dataType:'json',
        url:'/librarian/return/records',
        contentType:'application/json;charset=UTF-8',
        async: false,
        success:function(data){//返回结果
            console.log(data);
            var records = data.data;
            var returnRecord = new Array();
            for(var i=0; i<records.length;i++){

                returnRecord.push(new returnlist(records[i].id,records[i].bookId,records[i].returnDate,records[i].readerId));
            }
            loadReturnHistoryData(returnRecord);

        }
    });
}
function loadReturnHistoryData(returnRecord)
{
    for (var i = 0; i < returnRecord.length; i++) {
    	var returnId = returnRecord[i].returnId;
        var readerId = returnRecord[i].readerId;
        var bookId = returnRecord[i].bookId;
        var returnDate = returnRecord[i].returnDate;
        $("#return-history-table").append('<tbody>' +
            '<tr>' +
            '<td>'+(i+1)+'</td>\n' +
            '<td>'+returnId+'</td>\n' +
            '<td>'+readerId+'</td>\n' +
            '<td>'+bookId+'</td>\n' +
            '<td>'+returnDate+'</td>\n' +
            '</tr></tbody>')
    }
}

/**
 *  加载公告
 */
function loadAnnouncements()
{
    $.ajax({
        type:'GET',
        dataType:'json',
        url:'/announcements',
        contentType:'application/json;charset=UTF-8',
        async: false,

        success:function(data){//返回结果
            announcements = data.data;
            var message = new Array();
            for(var i=0; i<announcements.length;i++){

                message.push(new announcement(announcements[i].id,announcements[i].title,announcements[i].content));
            }
            loadAnnouncementData(message);
        }
    });
}
function loadAnnouncementData(message)
{
    for (var i = 0; i < message.length; i++) {
        var id = message[i].id;
        var title = message[i].title;
        var content = message[i].content;
        $("#announcement-table").append('<tbody>'+
            '<tr>'+
            '<td><input type="checkbox" /></td>'+
            '<td>'+(i+1)+'</td>'+
            '<td>'+id+'</td>'+
            '<td>'+title+'</td>'+
            '<td>'+content+'</td>'+
            '<td>'+
            '<div class="am-btn-toolbar">'+
            ' <div class="am-btn-group am-btn-group-xs">\n' +
            '         <button type="button" onclick="modAnnouncement(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Edit</button>\n' +
            '         <button type="button" onclick="delAnnouncementItem(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Delete</button>\n' +
            '         </div>'+
            '</div>'+
            '</td></tr></tbody>');
    }
}

/* 修改信息 */
var modTr = function(e) {
	var tds = e.parentNode.parentNode.parentNode.parentNode.childNodes;
    $('#account-title').text('Add Reader')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var userName = data[0];
            var email = data[1];
            var phoneNumber = data[2];
            var requestData = JSON.stringify({
                "userName": userName,
                "email": email,
                "phoneNumber": phoneNumber,
            });
            addAccount(requestData, '/add/reader')
        },
        onCancel: function (e) {
        }
    })
	console.log(tds);
}
// 删除某一条记录
function delBookItem(e) {
    // 拿到 tr
    var tr = e.parentNode.parentNode.parentNode.parentNode;
    var bookId = tr.childNodes[2].innerHTML;
    $.ajax({
        type: "DELETE",
        url: "/book",
        data:{
            "bookIds":bookId+""
        },
        success: function(data) {
            if(data.code == 0)
            {
                alert("删除成功");
            }else{
                alert(data.message)
            }
            location.reload();
        },
        error:function(data){
            alert('删除失败!');
        }
    });
}
// 删除读者
function delReaderItem(e)
{
    // 拿到 tr
    var tr = e.parentNode.parentNode.parentNode.parentNode;
    var readerId = tr.childNodes[2].innerHTML;
    $.ajax({
        type: "delete",
        url: "/reader",
        data: {'readerIds':readerId+""},
        success: function(data) {
            // location.reload();
            alert("success");
            clickFun(3);
        },
        error:function(data){
            alert('删除失败!');
        }
    });
}
// 更改公告
function modAnnouncement()
{
    $('#editAnnouncementPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var id = data[0];
            var title = data[1];
            var content = data[2];
            $.ajax({
                type:'POST',
                dataType:'json',
                url:'/announcement/'+id,
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify(
                    {
                        "title":title,
                        "content":content
                    }),
                success:function(data){//返回结果
                    alert("success");
                    // location.reload();
                    clickFun(9);
                },
                error:function(data){
                    alert('更新修改数据失败!');
                }
            });
        },
        onCancel: function (e) {
        }
    })
}
// 删除公告
function delAnnouncementItem(e)
{
    // 拿到 tr
    var tr = e.parentNode.parentNode.parentNode.parentNode;
    var announcementId = tr.childNodes[2].innerHTML;
    $.ajax({
        type: "delete",
        url: "/announcement",
        data: {'ids':announcementId+""},
        success: function(data) {
            alert("success");
            location.reload();
        },
        error:function(data){
            alert('删除失败!');
        }
    });
}

$('#addReader').click(function () {
    $('#account-title').text('Add Reader')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var userName = data[0];
            var email = data[1];
            var phoneNumber = data[2];
            var requestData = JSON.stringify({
                "userName": userName,
                "email": email,
                "phoneNumber": phoneNumber,
            });
            addAccount(requestData, '/add/reader',5)
        },
        onCancel: function (e) {
        }
    })
});

function addAccount(data, url,index) {
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        success: function (e) {
            alert(e.message)
            clickFun(index);
        }
    })
}

/**
 *   提交barcode事件
 */
function submitBarCode(e)
{
    var inputs = $('#bar-code-form').find("input");
    var formData = new FormData();
    formData.append("file",inputs[0].files[0]);
    // console.log(formData);
    if (inputs[0].value=='')
    {
        alert("please add a file");
    }else{
        // 图片已经上传
        console.log(new FormData($("#bar-code-form")[0]));
        $.ajax({
            url: '/librarian/upload',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success:function (data) {
                console.log(data);
                if(data.code==0)
                {
                    var barCode = data.data;
                    // 设置barCode
                    $("#return-form").find("input")[0].value = barCode;
                }
                },
                error:function () {

                }
            });
    }

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

function showBarCode(e){
    var barCodeUrl = "http://"+e.name;
    //assets/i/bookicon1.png
    // 设置src
    $("#showBarCode").find("img")[0].src=barCodeUrl;

    console.log(barCodeUrl);
    $('#showBarCode').modal({
        relatedTarget: this,
        relatedTarget: this,
        onConfirm: function (e) {
        },
        onCancel: function (e) {
        }
    })
}

$("#addAnnouncement").click(function () {
    $('#announcement-title').text('Add Announcement')
    $('#addAnnouncementPrompt').modal({
        relatedTarget: this,
        onConfirm: function (e) {
            var data = e.data;
            var title = data[0];
            var content = data[1];

            if (title == '') {
                alert('title can not be null！');
                return false;
            }
            if (content == '') {
                alert('content can not be null！');
                return false;
            }
            var requestData = JSON.stringify({
                "title": title,
                "content": content,
            });
            addAccount(requestData, '/announcement/add',9);
        },
        onCancel: function (e) {
        }
    })
});
/**
 *  添加category
 */
$('#addcategory').click(function () {
    $('#category-title').text('Add Category')
    $('#category-prompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            console.log(e.data);
            var requestData = JSON.stringify({
                "name": data
            });
            addAccount(requestData, '/add/category',4);
        },
        onCancel: function (e) {
        }
    })
});

/**
 *  添加Location
 */
$('#addlocation').click(function () {
    $('#location-title').text('Add Location')
    $('#location-prompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            console.log(data)
            var requestData = JSON.stringify({
                "name": data
            });
            addAccount(requestData, '/add/location',4);
        },
        onCancel: function (e) {
        }
    })
});
function adminLogin(e)
{
        // admin-form
        var form = e.parentNode.parentNode;
        var inputs = form.getElementsByTagName('input');
        var email = inputs[0].value;
        var password = inputs[1].value;
        var flag = inputs[2].checked;
        console.log(inputs);
        $.ajax({
            type: 'post',
            url: "/login/admin",
            data: JSON.stringify({
                "email": email,
                "password": password
            }),
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function (e) {
                console.log(e);
                if (e.code === 0) {
                    window.location.href = "/";
                } else {
                    alert(e.message)
                }
            }
        });
}
$('#my-datepicker').datepicker({locale:'en_US'}).
    on('changeDate.datepicker.amui', function(event) {
        var dateInput = $('#date-input')[0].value;
        $.ajax({
            type:'GET',
            dataType:'json',
            url:'/libralian/income/records',
            contentType:'application/json;charset=UTF-8',
            async: false,
            data:{
              "date":dateInput
            },
            success:function(data){//返回结果
                incomes = data.data;
                console.log(data);
                var incomeList = new Array();
                for(var i=0; i<incomes.length;i++){

                    incomeList.push(new Income(incomes[i].deposit,incomes[i].fine));
                }
                loadIncome(incomeList);
            }
        });
});
