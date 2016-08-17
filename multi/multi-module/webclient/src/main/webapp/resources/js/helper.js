$(document).ready(function(){
		
var irowdep = -1;
var irowempl = -1;
var chosendep = -1;
var smode = 1;
var msgElement = document.getElementById("errmsg");
var msgElement1 = document.getElementById("errmsg1");
//showerrmsg("test");
//clearerrmsg();

$('tbody tr:even').addClass('even');
$('tbody tr:odd').addClass('odd');
//$('#soptdob2').addClass('hidden');
$('#soptdob2').hide();


function showerrmsg(msgelem,msg){
msgelem.innerHTML=msg;
}

function clearerrmsg(msgelem){
msgelem.innerHTML=" ";
}

$('#deptbl tbody tr').on("click", function() { 
clearerrmsg(msgElement);
irowdep =$(this).attr('id');
$("#deptbl tbody tr").siblings().removeClass('selected');
$(this).addClass('selected');
});

$('#empltbl tbody tr').on("click", function() { 
clearerrmsg(msgElement1);
irowempl =$(this).attr('id');
$("#empltbl tbody tr").siblings().removeClass('selected');
$(this).addClass('selected');
});


$("#depopt").on("change", function() { 
chosendep = $(this).val();
window.location = "./?depid="+chosendep;
});

$("#depnext").on("click", function() { 
window.location = "./?page=1";
});

$("#depprev").on("click", function() { 
window.location = "./?page=-1";
});

$("#emplnext").on("click", function() { 
window.location = "./?page=2";
});

$("#emplprev").on("click", function() { 
window.location = "./?page=-2";
});

$("#deptbl tbody tr").hover(
 
 function () {
    $(this).css("background", "yellow");
   },

 function () {
    $(this).css("background", "");
   }
       
 ); 

$("#empltbl tbody tr").hover(
 
 function () {
    $(this).css("background", "yellow");
   },

 function () {
    $(this).css("background", "");
   }
       
 ); 

$('.menu').hover(
 
 function () {
    $(this).css("background", "yellow");
   },

 function () {
    $(this).css("background", "");
   }
       
 ); 

$("#adddep").click(function () 
{ 
 window.location = "dep/add"
})


$("#editdep").click(function () 
        { 
if (irowdep != -1){
 window.location = "dep/edit?id="+(irowdep % 1000);
}else{
 showerrmsg(msgElement,"Select record to edit");
}

})

$("#deldep").click(function () 
        { 
if (irowdep != -1){
if (irowdep < 1000){
   window.location = "dep/del?id="+irowdep;
  }else{
   showerrmsg(msgElement,"Not empty, delete employee records first");
  }
}else{
 showerrmsg(msgElement,"Select record to delete");
}

})


$("#addempl").click(function () 
{ 
 window.location = "empl/add"
})


$("#editempl").click(function () 
        { 
if (irowempl != -1){
 window.location = "empl/edit?id="+irowempl;
}else{
 showerrmsg(msgElement1,"Select record to edit");
}

})

$("#delempl").click(function () 
        { 
if (irowempl != -1){
 window.location = "empl/del?id="+irowempl;
}else{
 showerrmsg(msgElement1,"Select record to delete");
}

})


}); 