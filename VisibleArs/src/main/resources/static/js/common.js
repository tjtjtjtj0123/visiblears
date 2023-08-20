//window.onload 함수
function fnOnLoad() {
	const scrollHeight = document.scrollingElement.scrollHeight;
	//const noticeElement = document.querySelector('#notice');
	//noticeElement.style.top = `${scrollHeight - 100}px`;
}

// 히든 필드 생성 및 추가
function addHiddenField(name, value) {
    var hiddenField = document.createElement('input');
    hiddenField.type = 'hidden';
    hiddenField.name = name;
    hiddenField.value = value;
    return hiddenField;
}


//음성 호출
function fnArsCall(proc) {
	try {
		const urlParams = new URL(location.href).searchParams;
		const token = urlParams.get('token');
		const param = {
			keycode: "kjkOcDy5cH2p5i+gQHXop1M+c8uSH64+7Q0Xy6b87KQ=",
			proc: proc,
			token: token
		}

		$.ajax({
			url: "https://pbx116138.smartcti.co.kr:9042/ServiceAPI/ViewARS/",
			data: param,
			type: 'POST',
			timeout: 30000,
		}).done(function(data) {
			console.log(data)
		});
	} catch {

	}
}

//통화종료
function fnArsEnd() {
	try {
		const urlParams = new URL(location.href).searchParams;
		const token = urlParams.get('token');
		const param = {
			token: token,
			keycode:"kjkOcDy5cH2p5i+gQHXop1M+c8uSH64+7Q0Xy6b87KQ=",
			paging :"service02"
		}
		$.ajax({
			url: "https://pbx116138.smartcti.co.kr:9042/ServiceAPI/ViewARSHangup/index.html",
			data: param,
			dataType : 'jsonp',
			type: 'POST',
		}).done(function (data) {
			console.log(data)
		});
	} catch {

	}
}

//뒤로가기 버튼
function handleClickBackward() {
	window.history.go(-1);
}

//등록 완료 모달
function modalOpen() {
	const modal = document.getElementById("modal");
	modal.classList.add("visible");
}

	
function modalInfoOpen() {
	const modal = document.getElementById("modal_info");
	modal.classList.add("visible");
}

//모달 끄기
function modalClose(element) {
	while(true) {
		if (!element) return;
		if (element.classList.contains("ui-modal")) break;
		element = element.parentNode;
	}
	element.classList.remove("visible");
}



function initLayerPosition() {
	var width = 300; //우편번호서비스가 들어갈 element의 width
	var height = 500; //우편번호서비스가 들어갈 element의 height
	var borderWidth = 1; //샘플에서 사용하는 border의 두께

	// 위에서 선언한 값들을 실제 element에 넣는다.
	element_layer.style.width = width + 'px';
	element_layer.style.height = height + 'px';
	element_layer.style.border = borderWidth + 'px solid';
	// 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
	element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
	element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
}


function addressIns() {
	new daum.Postcode({
		oncomplete: function (data) { //선택시 입력값 세팅
			document.getElementById("address").value = data.address; // 주소 넣기
			document.querySelector("input[name=address_detail]").focus(); //상세입력 포커싱
		}
	}).open();
}

//주소 찾기 scrip end

	
// 공백제거
function noSpaceForm(obj) 
{             
    var str_space = /\s/;               
    if(str_space.exec(obj.value)) 
    {     
        obj.value = obj.value.replace(' ',''); 
        return false;
    }
}

function handleClickHome(partner)
{
	location.href = "/"+partner+"/menu";
}