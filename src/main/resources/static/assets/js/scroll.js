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
				  str+= "<li><a href='#'>"+j+":"+announcements[i].title+"</a></li>"
				  // message.push(new announcement(announcements[i].id,announcements[i].title,announcements[i].content));
				   }
				   document.getElementById("scrollBox").innerHTML="<ul>"+str+"</ul>";
			   console.log(document.getElementById("scrollBox").innerHTML);
		   }


	   });


});


	