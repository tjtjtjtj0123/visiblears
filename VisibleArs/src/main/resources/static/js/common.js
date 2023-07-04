
function test() {
	alert("tese");

}
//뒤로가기 버튼
function handleClickBackward() {
	// when clicked backward icon.
	window.history.go(-1);
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

function handleClickClose() {
	// when clicked close icon.
}
