
	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');

	window.onload = function () {
		fnOnLoad();
		fnArsCall("index");
	};
