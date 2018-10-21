var str ="";
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
                   document.getElementById("scrollBox").innerHTML = "";
				   for(var i=0; i<announcements.length;i++){
					   var j = i+1;
					   var obj=announcements[i].content;
					  str+= "<li><a href='#'  title="+obj+" >"+j+":"+announcements[i].title+"</a></li>"
					  // message.push(new announcement(announcements[i].id,announcements[i].title,announcements[i].content));
					   }
					   document.getElementById("scrollBox").innerHTML="<ul>"+str+"</ul>";
                   console.log(document.getElementById("scrollBox").innerHTML);
               }

			   
		   });	
			//获取滚动部分
			var area=document.getElementById("scrollBox");
			//area.innerHTML="<ul>"+str+"</ul>";
			 //设置全局定时器
			 var timer=null;
			 //定义延迟
			 var delay=200;
			 //获取高度
			 var oLiHeight=$("#scrollBox li").height();
			 //速度
			 var speed=60;
			 area.scrollTop=20;
			 area.innerHTML+=area.innerHTML;
			 function startScroll(){//开始运动
				 timer=setInterval(scrollUp,speed);
				 area.scrollTop++;
				 }
			 function scrollUp(){//循环运动
			 	if(area.scrollTop%oLiHeight==0){
			 		clearInterval(timer)
			 		setTimeout(startScroll,delay);
			 		}else{
			 			area.scrollTop++;
			 			if(area.scrollTop >= area.scrollHeight/2){
			 				area.scrollTop =20;
			 				}
			 			}
			 	}
		 //页面加载两秒后运动
		  setTimeout(startScroll,delay);
		 //鼠标事件
		// $("#scrollBox").mouseover(function(){clearInterval(timer)});
		//   $("#scrollBox").mouseout(function () {
		// 	setTimeout(startScroll,delay);
        // })
		 //$("#scrollBox").mouseout(function(){timer=setInterval('scrollUp()',speed)});
})

	