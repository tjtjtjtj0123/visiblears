	/*<![CDATA[*/
	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');

	window.onload = function () {
		fnOnLoad();
		fnArsCall("service02");
	};

	function goInquiry() {
		var pa1 = "[[${order.ordererName}]]";
		var pa2 = "[[${order.ordererPhone1}]]";
		var pa3 = "[[${order.receiverAddress}]]";
		location.href = "/sornia/inquiry?ordererName=" + pa1 + "&ordererPhone1=" + pa2 + "&receiverAddress=" + pa3 + "&token=" + token;

	}
	/*]]>*/