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

//뒤로가기 버튼
function handleClickBackward() {
	location.href = document.referrer;
}

//msg 커스텀 모달	
function modalCustomOpen() {
	const modal = document.getElementById("modal_custom");
	modal.classList.add("visible");
}

//통화종료 모달
function modalCallEndOpen() {
	const modal = document.getElementById("modal_endcall");
	modal.classList.add("visible");
}
//상담원 연결 모달
function modalcsrOpen() {
	const modal = document.getElementById("modal_csr");
	modal.classList.add("visible");
}


//모달 끄기
function modalClose(element) {
	while (true) {
		if (!element) return;
		if (element.classList.contains("ui-modal")) break;
		element = element.parentNode;
	}
	element.classList.remove("visible");
}





// 공백제거
function noSpaceForm(obj) {
	var str_space = /\s/;
	if (str_space.exec(obj.value)) {
		obj.value = obj.value.replace(' ', '');
		return false;
	}
}

function handleClickHome(partner) {
	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');
	location.href = "/" + partner + "/menu?token=" + token;
}


function showLoading() {
  var loadingElement = document.getElementById("loading");
  if (loadingElement) {
    loadingElement.style.display = "block";
  }
}

function hideLoading() {
  var loadingElement = document.getElementById("loading");
  if (loadingElement) {
    loadingElement.style.display = "none";
  }
}