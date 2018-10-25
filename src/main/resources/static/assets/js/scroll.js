var str ="";
var offLeft;
var aLen;	// announcement length
var index = 0;
$(document).ready(function(){
    $.ajax({
	   type:'GET',
	   dataType:'json',
	   url:'/announcements',
	   contentType:'application/json;charset=UTF-8',
	   async: false,

	   success:function(data){//返回结果
		   console.log(data);
		   announcements = data.data;
		   for(var i=0; i<announcements.length;i++){
			   var j = i+1;
			   var content=announcements[i].content;
			   var title=announcements[i].title;
			   $("#announcement").append('<p style="display: inline;position:relative;left: 0px"> <i class="am-icon-volume-up am-icon-fw"></i>&nbsp;&nbsp;'+title+'&nbsp;:&nbsp;'+content+'</p>');
		   }
		   // move();
		   aLen = $("#announcement").find("p").length;
		   for(var i=1;i<aLen;i++)
		   {
               $("#announcement").find("p")[i].style.display="none";
		   }
		   if(aLen>=1)
		   {
               setInterval(move,100);
           }
       }
   });

});
function move() {
    offLeft = $("#announcement").find("p")[index].offsetLeft;
    offLeft+=$("#announcement").find("p")[index].offsetWidth;
	var left = parseInt($("#announcement").find("p")[index].style.left);
	if (left >= -offLeft)
	{
       	$("#announcement").find("p")[index].style.left = (-5+left)+"px";
    }else{
        $("#announcement").find("p")[index].style.display="none";
        $("#announcement").find("p")[index].style.left="0px";	// 恢复到原来的位置
        if (index == aLen-1)
		{
			// 说明已经遍历完
			index = 0;
		}else{
			// 移动下一个公共
            index++;
        }
        $("#announcement").find("p")[index].style.display="inline";
    }
}

	