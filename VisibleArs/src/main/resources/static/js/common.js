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

//주소 찾기 scrip start
// 우편번호 찾기 화면을 넣을 element
var element_layer = document.getElementById('layer');

function closeDaumPostcode() {
	// iframe을 넣은 element를 안보이게 한다.
	element_layer.style.display = 'none';
}

function sample2_execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function (data) {
			// 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고항목 변수

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else { // 사용자가 지번 주소를 선택했을 경우(J)
				addr = data.jibunAddress;
			}

			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
				// 조합된 참고항목을 해당 필드에 넣는다.
				//document.getElementById("sample2_extraAddress").value = extraAddr;

			} else {
				//document.getElementById("sample2_extraAddress").value = '';
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			//document.getElementById('sample2_postcode').value = data.zonecode;
			document.getElementById("recvAddr").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			//document.getElementById("sample2_detailAddress").focus();

			// iframe을 넣은 element를 안보이게 한다.
			// (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
			element_layer.style.display = 'none';
		},
		width: '100%',
		height: '100%',
		maxSuggestItems: 5
	}).embed(element_layer);

	// iframe을 넣은 element를 보이게 한다.
	element_layer.style.display = 'block';

	// iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
	initLayerPosition();
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