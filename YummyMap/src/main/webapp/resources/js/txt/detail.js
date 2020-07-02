

'use strict';
$(document).ready(function(){
	
	  let userid = '${sid}';
	  if(!userid) {
		  $('#login').show();
		  $('#logout').hide();
		  $('#mypage').attr('href', '/yummymap/join/join.mmy');
	  } else {
		  $('#login').hide();
		  $('#logout').show();
		  $('#mypage').attr('href', '/yummymap/member/mypage.mmy');
	  }
		//마이리스트 이벤트 처리
		$('#myListIcon').click(function(){
			if(!userid) {
				alert('로그인을 진행해주세요');
				return;
			}
			$(location).attr('href', '/yummymap/main/myList.mmy');
		});
		
	// txt update
	$('#update').click(function() {
		$('#frm').attr('action', '/yummymap/txt/edit.mmy');
		$('#frm').submit();
	});
	// txt delete
	$('#delete').click(function() {
		if(!confirm("삭제하시겠습니까?")){
			return;
		}
		var txtno = $('#txtno').val();
		$('#frm').attr('action', '/yummymap/txt/delete.mmy');
		$('#frm').submit();
		alert("삭제되었습니다.");
	});
	
	// reply button
	$('#rtxt').focusin(function(){
		$('#reply-btn').show();
	});
	// reply write
	$('#reply-btn').click(function() {
		var txtno = $('#upno').val();
		replyWrite(txtno);
	});
});		
			
$(document).on('click','.rrrno',function(){
	var txtno = $(this).attr('value');
	replyDelete();
	
// reply delete ajax
function replyDelete(){
	if(!confirm("삭제하시겠습니까?")){
		return;
	}
	var upno = $('#upno').val();
	$.ajax({
		url: "/yummymap/txt/rDelete.mmy",
		type: 'post',
		dataType: 'json',
		data: {
			'txtno' : txtno,
			'upno' : upno
		},
		success: function(result){
			if(result){
				$('#'+'reno'+txtno).remove();
				alert("삭제되었습니다.");
				$('#reply-btn').hide();
			}
			console.log(result);
			},
			error:function(request, status, error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
});

// reply write ajax
function replyWrite(txtno) {
	var mtxt = $('#rtxt').val();
	var mid = $('#mid').val();
	var upno = $('#upno').val();
	var param="mtxt="+mtxt+"&mid="+mid+"&upno="+upno;
	
	$.ajax({
		url: '/yummymap/txt/rWrite.mmy',
		type:'post',
		dataType: 'json',
		data: param,
		success: function(data){
			replyList();
			$('#rtxt').val('');
			$('#reply-btn').hide();
		},
		error:function(request, status, error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	 });
}


// reply list ajax
function replyList(){
	var txtno = $('#upno').val();
	
	$.ajax({
		url: "/yummymap/txt/rList.mmy",
		type: 'get',
		dataType: 'json',
		data: {
			'txtno' : txtno
		},
		success: function(result){
			var html = "";
			var len = result.length;
			
			if(len > 0){
				for(var i=0; i<len; i++){
					html += 	'<div class="" id="reno'+result[i].txtno+'">';
					html += 	'<div class="d-flex reply-txt-1 mt-3">';
					html += 	'<p id="">'+result[i].mid+'</p>';
					html += 	'<p id="" class="pl-2">'+ result[i].cdate +'</p>';
					html += 	'</div>';
					html += 	'<div class="reply-txt-2 ">';
					html += 	'<p id="listReply" style="display:inline-block; width: 680px;">'+result[i].mtxt+'</p>';				
					html += 	'<a onclick="" class="btn btn-sm btn-outline-light rrrno" value="'+result[i].txtno+'">삭제</a>';
					html += 	'</div>';
					html += 	'<div class="b-w border-bottom ml-5 mt-4" style="height: 0px;">'+'</div>';
					html += 	'</div>';
				}
			} else {
				html += "<div>";
				html +=	"<div class='d-flex reply-txt-3'>등록된 댓글이 없습니다.";
				html += "</div>";
				html += "</div>";
			}
            $("#re-count").html(len);
            $("#rList").html(html);
		},
		error:function(request, status, error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

// recommend ajax
function like(){
	var txtno = $('#upno').val();
	var rnum = $('#rnum').attr('value');
	var mid = $('#mid').val();
	alert(rnum);

	var param="txtno="+txtno+"&rnum="+rnum+"&mid="+mid;
	$.ajax({
		url: "/yummymap/txt/like.mmy",
		type: 'post',
		dataType: 'json',
		data: param,
		success: function(data){
			let str = data.result;
			$('#rnum').text(data.rnum);
			alert(data.rnum);
		},
		error: function(request, status, error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function getLike(){
	var txtno = $('#upno').val();
	var rnum = $('#rnum').attr('value');
	var param="txtno="+txtno+"&rnum="+rnum;
	$.ajax({
		url: "/yummymap/txt/likeCnt.mmy",
		type: 'post',
		dataType: 'json',
		data: param,
		success: function(data){
			$('#rnum').text(rnum);
		},
		error: function(request, status, error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

$(function() {
	replyList();
	getLike();
});
