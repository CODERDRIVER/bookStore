$('#addAnnouncement').click(function () {
    //$('#prompt-title').text('Add Announcement')
    $('#addAccountPrompt').modal({
        relatedTarget: this,

        onConfirm: function (e) {
            var data = e.data;
            var title = data[0];
            var content = data[1];

			var title = getId("doc1").value;
			if (title == '') {
				alert('title can not be null！');
				return false;
		    }

			var content = getId("doc2").value;
			if (content == '') {
				alert('content can not be null！');
				return false;
			} 

            var requestData = JSON.stringify({
                "title": title,
                "content": content,
            });
            addAccount(requestData, '/announcement/add')
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

// 获取公告列表
var message = new Array();
$(document).ready(function(){ 				
         $.ajax({
					type:'GET',
					dataType:'json',
					url:'/announcements',
					contentType:'application/json;charset=UTF-8',
					async: false,
					
					success:function(data){//返回结果
  						announcements = data.data;
						for(var i=0; i<announcements.length;i++){
							
							message.push(new announcement(announcements[i].id,announcements[i].title,announcements[i].content));
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
          url: '/logout',
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
// var numberRowsInTable = theTable.rows.length;
// 数据条数
var numRows = getId("spanTotalNumRows");
// 公告信息
var message = new Array();
// message.push(new announcement("第一条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第二条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第三条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第四条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第五条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第六条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第七条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第八条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第九条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第十条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第十一条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第十二条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第十三条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));
// message.push(new announcement("第十四条假装的公告", "最近，图书馆有新的一批关于java技术的书籍上新！"));

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

/* 创建公告对象 */
function announcement(id,title,content) {
	this.id =id;
	this.title = title;
	this.content = content;
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
		var id = message[i].id;
		var title = message[i].title;
		var content = message[i].content;

        /**
		 * <tbody>
         <tr>
         <td><input type="checkbox" /></td>
         <td>Serial ID</td>
         <td>Announcement ID</td>
         <td>Title</td>
         <td>Content</td>
         <td>
         <div class="am-btn-toolbar">
         <div class="am-btn-group am-btn-group-xs">
         <button class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Edit</button>
         <button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Delete</button>
         </div>
         </div>
         </td>
         </tr>
         </tbody>
         */
        $("#table").append('<tbody>'+
            '<tr>'+
            '<td><input type="checkbox" /></td>'+
            '<td>'+(i+1)+'</td>'+
            '<td>'+id+'</td>'+
            '<td>'+title+'</td>'+
            '<td>'+content+'</td>'+
            '<td>'+
            '<div class="am-btn-toolbar">'+
            ' <div class="am-btn-group am-btn-group-xs">\n' +
            '         <button type="button" onclick="modTr(this)"  class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> Edit</button>\n' +
            '         <button type="button" onclick="delItem(this)" class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span class="am-icon-trash"></span> Delete</button>\n' +
            '         </div>'+
            '</div>'+
            '</td></tr></tbody>');
		// 创建tr
		// var tr = createObj("tr");
		// // 创建td
		// var checkTd = createObj("td");
		// var announcementId = createObj("td");
		// var serialTd = createObj("td");
		// var titleTd = createObj("td");
		// var contentTd = createObj("td");
		// var dmlTd = createObj("td");
        //
		// var checkBtn = createObj("input");
		// checkBtn.type = "checkbox";
		// checkBtn.value = id;
		// // 将复选框添加到第一列；
		// checkTd.appendChild(checkBtn);
		// // 将获得的值添加到创建的指定Td中；
		// var tbody = getId("tb");
		// var rows = tbody.rows.length;
		// // 将获得的信息添加到指定的为td中
		// serialTd.innerHTML = rows + 1;
        // announcementId.innerHTML = id;
		// titleTd.innerHTML = title;
		// contentTd.innerHTML = content;
        //
        //
		// // 创建个button按钮，添加到操作列；
		// var lookBtn = createObj("input");
		// lookBtn.type = "button";
		// lookBtn.value = "view";
		// // 为新建的lookBtn创建监听属性；
		// lookBtn.onclick = function() {
		// 	lookTr(this);
		// };
        //
		// var changeBtn = createObj("input");
		// changeBtn.type = "button";
		// changeBtn.value = "modify";
        //
		// // 为新建的changeBtn创建监听属性；
		// changeBtn.onclick = function() {
		// 	modTr(this);
		// }
		// dmlTd.appendChild(lookBtn);
		// dmlTd.appendChild(changeBtn);
		// // 将新建的td加入到新建的行中
		// tr.appendChild(checkTd);
		// tr.appendChild(serialTd);
		// tr.appendChild(announcementId);
		// tr.appendChild(titleTd);
		// tr.appendChild(contentTd);
		// tr.appendChild(dmlTd);
        //
		// // 将新建的tr加入到tbody中
		// var tbody = getId("tb");
		// tbody.appendChild(tr);
        //
		// // 隔行换色。
		// var table = document.getElementById("table");
		// table.tBodies[0].rows[table.tBodies[0].rows.length - 1].style.display = 'none';
		// numberRowsInTable++;
		// totalPage.innerHTML = pageCount();
		// numRows.innerHTML = numberRowsInTable;
		// first();
	}
	changeColor();
}

/* 增加公告信息 */
var addTr = function() {
	// 做添加，首先获取输入的值
	var title = getId("i1").value;
	if (title == '') {
		alert('title can not be null！');
		return false;
	} 

	var content = getId("i2").value;
	if (content == '') {
		alert('content can not be null！');
		return false;
	}
	
	// 存放公告信息
	message.push(new  announcement(title,content));
	showHide1();
	getId("i1").value = "";
	getId("i2").value = "";

	// 创建tr
	var tr = createObj("tr");
	// 创建td
	var checkTd = createObj("td");
	var serialTd = createObj("td");
	var titleTd = createObj("td");
	var contentTd = createObj("td");
	var dmlTd = createObj("td");
	var checkBtn = createObj("input");
	checkBtn.type = "checkbox";
	checkTd.appendChild(checkBtn);
	// 将获得的值添加到创建的指定Td中；
	var tbody = getId("tb");
	var rows = tbody.rows.length;
	serialTd.innerHTML = rows + 1;
	titleTd.innerHTML = title;
	content.innerHTML = content;


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
	tr.appendChild(titleTd);
	tr.appendChild(contentTd);
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
					// 删除已选择的行
                    checkedList.push(input.value);
					tbody.removeChild(input.parentNode.parentNode);
					// table长度减一
					numberRowsInTable--;
				}
			}
		}
        $.ajax({
            type: "delete",
            url: "/announcement",
            data: {'ids':checkedList.join(",")},
            success: function(data) {
            	alert("success");
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

/* 获取修改当前公告信息 */
var modTr = function(obj) {
	var buttonConfirm = getId("hide");
	getId("lookMessages").innerHTML = "Edit announcement";
	buttonConfirm.style.display = "block";
	// 获得隐藏的DIV
	var overDiv = getId("over2");
	// 将隐藏的div有隐藏显现出来
	overDiv.style.display = "block";
	// 通过按钮来获得tr;
	var tr = obj.parentNode.parentNode.parentNode.parentNode;
	// 获得需要修改的内容
	serialTxt = tr.cells[1].innerHTML;
	var id = tr.cells[2].innerHTML;
	var title = tr.cells[3].innerHTML;
	var content = tr.cells[4].innerHTML;
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
    var inputs = tb.getElementsByTagName("input");
    var texts = tb.getElementsByTagName("textarea");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = id;
	inputs[1].value = title;
	texts[0].value = content;
	inputs[0].disabled = true;
	inputs[1].disabled = "";
	texts[0].disabled = "";

}

/* 查看公告信息 */
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
	var title = tr.cells[2].innerHTML;
    var content = tr.cells[3].innerHTML;
    
	// 获得遮罩层的tbody
	var tb = getId("over_tb2");
	// 获得tb中所有的input
    var inputs = tb.getElementsByTagName("input");
    var texts = tb.getElementsByTagName("textarea");
	// 往遮罩层中的input填入从表格中取得来的数据
	inputs[0].value = title;
	texts[0].value = content;
	inputs[0].disabled = "disabled";
	texts[0].disabled = "disabled";
	
}
// 删除某一条记录
function delItem(e) {
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
            art.dialog.tips('删除失败!');
        }
    });
}
/* 确认修改按钮 */
var okBtn = function() {
	// 获得遮罩层中的tbody
	var tb = getId("over_tb2");
	// 获得tb中的所有的input的值，并且赋值给变量
    var inputs = tb.getElementsByTagName("input");
    var texts = tb.getElementsByTagName("textarea");
	var id = inputs[0].value;
	var title = inputs[1].value;
	var content = texts[0].value;
	
	// 获得主页中的数据,将修改的数据填入到主页中,
	// var tbody = getId("tb");
	// var rows = tbody.rows.length; // 获得所有的行
	// for (var i = 0; i < rows; i++) {
	// 	var tr = tbody.rows[i];
	// 	if (i + 1 == serialTxt) {
	// 		if (tr.cells[3].innerHTML != title) {
	// 			if (title == '') {
	// 				alert('title can not be null！');
	// 				return false;
	// 			}
	// 			tr.cells[3].innerHTML = title;
	// 		}
	// 		if (tr.cells[4].innerHTML != content) {
	// 			if (content == '') {
	// 				alert('content can not be null！');
	// 				return false;
	// 			}
	// 			tr.cells[4].innerHTML = content;
	// 		}
    //
	// 	}
	// }
	// 调用后端接口，同步到数据库中
    $.ajax({
        type:'POST',
        dataType:'json',
        url:'/announcement/'+id,
        contentType:'application/json;charset=UTF-8',
        data:JSON.stringify({
			"id":id,
            "title": title,
            "content": content,
        }),
        success:function(data){//返回结果
            location.reload();
            alert("Success");
        },
        error:function(data){
            art.dialog.tips('更新修改数据失败!');
        }
    });
	// $.ajax({
	// 	type:'POST',
	// 	dataType:'json',
	// 	url:'/librarian/edit/announcement/',
	// 	contentType:'application/json;charset=UTF-8',
	// 	data:{"id":announceId,"title":title,"content":content},
	// 	success:function(data){//返回结果
	// 			location.reload();
	// 			alert("Success");
	// 	  },
	//     error:function(data){
	// 		art.dialog.tips('更新修改数据失败!');
	// 	}
	//      });
	// 隐藏遮罩层
	showHide2();
}

/* 下一页 */
// function next() {
//
// 	hideTable();
//
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
//
// /* 上一页 */
// function pre() {
//
// 	hideTable();
//
// 	page--;
//
// 	currentRow = pageSize * page;
// 	maxRow = currentRow - pageSize;
// 	if (currentRow > numberRowsInTable)
// 		currentRow = numberRowsInTable;
// 	for (var i = maxRow; i < currentRow; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
//
// 	if (maxRow == 0) {
// 		preText();
// 		firstText();
// 	}
//
// 	showPage();
// 	nextLink();
// 	lastLink();
// }
//
// /* 第一页 */
// function first() {
// 	hideTable();
// 	page = 1;
// 	for (var i = 0; i < pageSize && i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
// 	showPage();
//
// 	preText();
// 	nextLink();
// 	lastLink();
// }
//
// /* 最后一页 */
// function last() {
// 	hideTable();
// 	page = pageCount();
// 	currentRow = pageSize * (page - 1);
// 	for (var i = currentRow; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = '';
// 	}
// 	showPage();
//
// 	preLink();
// 	nextText();
// 	firstLink();
// }
//
// /* 隐藏table */
// function hideTable() {
// 	for (var i = 0; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = 'none';
// 	}
// }
//
// /* 展示第几页 */
// function showPage() {
// 	pageNum.innerHTML = page;
// }
//
// /* 总共页数 */
// function pageCount() {
// 	var count = 1;
// 	if (numberRowsInTable % pageSize != 0)
// 		count = 1;
// 	return parseInt(numberRowsInTable / (pageSize + 0.1)) + count;
// }
//
// /* 显示链接 */
// function preLink() {
// 	spanPre.innerHTML = "<a class='upLink' href='javascript:pre();'>pre page</a>";
// }
// function preText() {
// 	spanPre.innerHTML = "pre page";
// }
//
// function nextLink() {
// 	spanNext.innerHTML = "<a class='downLink' href='javascript:next();'>next page</a>";
// }
// function nextText() {
// 	spanNext.innerHTML = "next page";
// }
//
// function firstLink() {
// 	spanFirst.innerHTML = "<a href='javascript:first();'>firstPage , </a>";
// }
// function firstText() {
// 	spanFirst.innerHTML = "firstPage , ";
// }
//
// function lastLink() {
// 	spanLast.innerHTML = "<a href='javascript:last();'>lastPage , </a>";
// }
// function lastText() {
// 	spanLast.innerHTML = "lastPage , ";
// }
//
// /* 隐藏表格 */
// function hide() {
// 	for (var i = pageSize; i < numberRowsInTable; i++) {
// 		theTable.rows[i].style.display = 'none';
// 	}
//
// 	totalPage.innerHTML = pageCount();
// 	pageNum.innerHTML = '1';
// 	numRows.innerHTML = numberRowsInTable;
//
// 	nextLink();
// 	lastLink();
// }
//
// hide();
//
// /*显示时特效*/
// $(document).ready(function() {
// 	$("#show").click(function() {
// 		$(".over").show("slow");
// 	});
// });
//
// /*隐藏时特效*/
// $(document).ready(function() {
// 	$("#hide23").click(function() {
// 		$(".over").hide("slow");
// 	});
// });