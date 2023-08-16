	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');
		
	function modalOpen() {
		const modal = document.getElementById("modal");
		modal.classList.add("visible");
	}

	$(document).ready(function () {
		$("#files").on("change", SelectImg);
		fnArsCall("service04");
	});

	function modalClose(element) {
		while(true) {
			if (!element) return;
			if (element.classList.contains("ui-modal")) break;
			element = element.parentNode;
		}
		element.classList.remove("visible");
	}
	// 이미지 정보들을 담을 배열
	var sel_files = [];
	

	const fileElement = document.querySelector('#files');
	fileElement.onchange = function () {
		// file upload logic.
	};

	window.onload = function () {
		fnOnLoad();
	};


	//업로드 여러개 하기
	function uploadimg() {
	
		
		if($("#title").val().length === 0 ){
			alert('제목을 입력해 주세요.');
			return false;
		}
		
		if($("#content").val().length === 0 ){
			alert('내용을 입력해 주세요.');
			return false;
		}
		
		if($("#orderer_phone1").val().length === 0 ){
			alert('전화번호를 입력해 주세요.');
			return false;
		}
		
		if($("#orderer_name").val().length === 0 ){
			alert('이름을 입력해 주세요.');
			return false;
		}

		//파일개수 체크
		if (sel_files.length > 5) {
			alert("이미지는 5개까지 업로드가 가능합니다.");
			sel_files = [];
			$("#imglistul").empty();
			return;
		}
		//확장자 검사
		for (var i = 0; i < sel_files.length; i++) {
			const fileDot = sel_files[0].name.split(".")[1].toLowerCase();
			if(!(fileDot =="jpg" || fileDot =="png" )){
				alert("jpg 혹은 png 파일만 업로드가 가능합니다!");
				sel_files = [];
				$("#imglistul").empty();
				return false;
			}
		}		

		const formData = new FormData();

		for (var i = 0; i < sel_files.length; i++) {
			formData.append("file", sel_files[i]);
		}
		formData.append("mall_order_dt",$("#mall_order_dt").val());
		formData.append("title", $("#title").val());
		formData.append("content",$("#content").val());
		
		formData.append("orderer_phone1",$("#orderer_phone1").val());
		formData.append("orderer_name", $("#orderer_name").val());
		formData.append("inquiry_type", $("#inquiry_type").val());
		
		$.ajax({
			type: 'POST',
			url: "/sornia/uploadimglist",
			data: formData,
			cache: false,
			contentType: false,
			processData: false,
			enctype: 'multipart/form-data'
		}).done(function (data) {
			$('#result-image').attr("src", data);
			modalOpen();
			$("#uploadBtn").attr("onclick","modalOpen()");
		}).fail(function (error) {
			console.log(error);
		})
	}

	function SelectImg(e) {

		// 이미지 정보들을 초기화
		sel_files = [];
		$("#imglistul").empty();

		var files = e.target.files;
		var filesArr = Array.prototype.slice.call(files);

		var index = 0;
		filesArr.forEach(function (f) {
			sel_files.push(f);
			var reader = new FileReader();
			reader.onload = function (e) {
				var html = '<li class="image" id="img_id_' + index + '"><img src="' + e.target.result + '" style="width: 45px; height: 45px; margin-right: 20px;"><img src="/sornia/assets/ico_cancel.svg" alt="cancel" class="cancel" onclick="deleteImageAction(' + index + ')"></li>';
				$("#imglistul").append(html);
				index++;
			}
			reader.readAsDataURL(f);

		});
	}

	function deleteImageAction(index) {
		console.log("index : " + index);
		sel_files.splice(index, 1);

		var img_id = "#img_id_" + index;
		$(img_id).remove();

		console.log(sel_files);
	}   