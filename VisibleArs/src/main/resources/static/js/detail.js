	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');

	window.onload = function () {
		fnOnLoad();
		fnArsCall("service03");
	};

	function fnMoveMenu() {
		location.href = "/sornia/inquiry?sabangNo=" + "[[${sabangNo}]]&token=" + token;
	}
