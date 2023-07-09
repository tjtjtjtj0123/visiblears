//window.onload 함수
function fnOnLoad() {
	const scrollHeight = document.scrollingElement.scrollHeight;
	const noticeElement = document.querySelector('#notice');
	noticeElement.style.top = `${scrollHeight - 100}px`;
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

//닫기버튼
function handleClickClose() {
	// when clicked close icon.
}

//홈버튼
function handleClickHome() {
	// when clicked home icon.
}

