	const urlParams = new URL(location.href).searchParams;
	const token = urlParams.get('token');

	window.onload = function () {
		fnOnLoad();
		fnArsCall("service01");
	};
	
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


	function closeTermsModal() {
		const element = document.querySelector('.terms-modal.visible');
		element.classList.remove('visible');
	}

	function detectAllTermsAgreed() {
		const checkboxItems = document.querySelectorAll(
			'.terms-content .ui-checkbox input[type="checkbox"]'
		);
		const toggleElement = document.querySelector('#toggle');
		for (let i = 0, length = checkboxItems.length; i < length; ++i) {
			if (!checkboxItems[i].checked) {
				toggleElement.checked = false;
				return;
			}
		}
		toggleElement.checked = true;
	}

	function handleClickAgreeTerms(event, target, termsId) {
		if (event) event.stopPropagation();
		const checkboxItems = document.querySelectorAll(
			'.terms-content .ui-checkbox input[type="checkbox"]'
		);
		if (/[0-4]/.test(termsId)) {
			checkboxItems[termsId].checked = true;
			closeTermsModal();
		} else {
			for (let i = 0, length = checkboxItems.length; i < length; ++i)
				checkboxItems[i].checked = target.checked;
		}
		detectAllTermsAgreed();
	}

	function handleToggleTermsModal(event, target) {
		event.stopPropagation();

		if (target.className === 'icon' && String(target.getAttribute('src')).includes('ico_more')) {
			for (
				let i = 0, elements = target.parentElement.children, length = elements.length;
				i < length;
				++i
			) {
				if (elements[i].className === 'terms-modal') {
					elements[i].classList.add('visible');
					break;
				}
			}
		} else closeTermsModal();
	}


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
				document.getElementById("address").value = addr;
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
		var height = 400; //우편번호서비스가 들어갈 element의 height
		var borderWidth = 5; //샘플에서 사용하는 border의 두께

		// 위에서 선언한 값들을 실제 element에 넣는다.
		element_layer.style.width = width + 'px';
		element_layer.style.height = height + 'px';
		element_layer.style.border = borderWidth + 'px solid';
		// 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
		element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
		element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
	}


	function dataSend() {

		var name = $("#name").val();
		var phone = $("#phone").val();
		if (name.length == 0) {
			alert('이름을 입력해 주세요.');
			return false;
		}
		if (phone.length == 0) {
			alert('전화번호를 입력해 주세요.');
			return false;
		}

		//문자열에 공백이 있는 경우
		var blank_pattern = /[\s]/g;
		if (blank_pattern.test(name) == true) {
			alert('공백은 입력 할 수 업습니다.');
			return false;
		}

		//특수문자가 있는 경우
		var special_pattern = /[`~!@#$%^&*|\\\'\";:\/?]/gi;
		if (special_pattern.test(name) == true) {
			alert('특수 문자는 입력 할 수 없습니다.');
			return false;
		}

		if (!($("input:checkbox[id='checkbox1']").is(":checked") &&
			$("input:checkbox[id='checkbox2']").is(":checked") &&
			$("input:checkbox[id='checkbox3']").is(":checked") &&
			$("input:checkbox[id='checkbox4']").is(":checked") &&
			$("input:checkbox[id='checkbox5']").is(":checked"))) {
			alert('모두 동의해 주세요.');
			return false;
		}

		const order = {
			seqNo: 0,
			ordererName: $("#name").val(),
			ordererPhone1: $("#phone").val(),
			receiverAddress: $("#address").val(),
			quantity: 0,
			payAmt: 0
		}
		$.ajax({
			url: "/sornia/get",
			data: order,
			type: 'POST',
		}).done(function (data) {

			if (!!data.sabangNo) {
				location.href = "/sornia/detail?sabangNo=" + data.sabangNo + "&token=" + token;
			} else {

				var form = document.createElement('form');
				form.setAttribute('method', 'post');
				form.setAttribute('action', "/sornia/noorder?token=" + token);

				var hiddenField = document.createElement('input');
				hiddenField.setAttribute('type', 'hidden');
				hiddenField.setAttribute('name', 'ordererName');
				hiddenField.setAttribute('value', $("#name").val());
				form.appendChild(hiddenField);

				var hiddenField = document.createElement('input');
				hiddenField.setAttribute('type', 'hidden');
				hiddenField.setAttribute('name', 'ordererPhone1');
				hiddenField.setAttribute('value', $("#phone").val());
				form.appendChild(hiddenField);

				var hiddenField = document.createElement('input');
				hiddenField.setAttribute('type', 'hidden');
				hiddenField.setAttribute('name', 'receiverAddress');
				hiddenField.setAttribute('value', $("#address").val());

				form.appendChild(hiddenField);
				document.body.appendChild(form);
				form.submit();
			}

		});
	}

	function addressIns() {
		new daum.Postcode({
			oncomplete: function (data) { //선택시 입력값 세팅
				document.getElementById("address").value = data.address; // 주소 넣기
				document.querySelector("input[name=address_detail]").focus(); //상세입력 포커싱
			}
		}).open();
	}