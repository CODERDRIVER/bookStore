    // 获取请求还书记录列表
    var returnRecord = new Array();
    $(document).ready(function(){
        $.ajax({
            type:'GET',
            dataType:'json',
            url:'/book/return/manage/records',
            contentType:'application/json;charset=UTF-8',
            async: false,
            success:function(data){//返回结果
                console.log(data);
                var records = data.data;
                for(var i=0; i<records.length;i++){

                    returnRecord.push(new returnlist(data[i].readerIdId,data[i].bookId,data[i].bookName,data[i].returnDate));
                    }

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

    // var returnRecord = new Array();
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));
    // returnRecord.push(new returnlist("123", "某本书","2018/10/13 21：55"));

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

    /* 创建还书记录对象 */
    function returnlist(readerId,bookId,bookName,returnDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.returnDate = returnDate;
        this.readerId = readerId;
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

    loadData();

    /* 加载数据 */
    function loadData() {
        for (var i = 0; i < returnRecord.length; i++) {
            var bookId = returnRecord[i].bookId;
            var bookName = returnRecord[i].bookName;
            var returnDate = returnRecord[i].returnDate;

            // 创建tr
            var tr = createObj("tr");
            // 创建td
            var serialTd = createObj("td");
            var readerIdTd = createObj("td");
            var bookIdTd = createObj("td");
            var bookNameTd = createObj("td");
            var returnDateTd = createObj("td");
            var dmlTd = createObj("td");
            // 将获得的值添加到创建的指定Td中；
            var tbody = getId("tb");
            var rows = tbody.rows.length;
            // 将获得的信息添加到指定的为td中
            serialTd.innerHTML = rows + 1;
            bookNameTd.innerHTML = bookName;
            bookIdTd.innerHTML = bookId;
            readerIdTd.innerHTML = readerId;
            returnDateTd.innerHTML = returnDate;

            // 创建个确认归还的button按钮，添加到操作列；
            var acceptBtn = createObj("input");
            acceptBtn.type = "button";
            acceptBtn.value = "Check";
            // 为新建的acceptBtn创建监听属性；
            acceptBtn.onclick = function() {
                var flag = window.confirm("需要确认该归还请求？");
                if (flag) {
                        $.ajax({
                            type: 'post',
                            url: '/reader/return/check',
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

            dmlTd.appendChild(acceptBtn);
            // 将新建的td加入到新建的行中
            tr.appendChild(serialTd);
            tr.appendChild(readerIdTd);
            tr.appendChild(bookIdTd);
            tr.appendChild(bookNameTd);
            tr.appendChild(returnDateTd);
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

    