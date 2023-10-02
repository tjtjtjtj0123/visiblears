package com.xcally.ars.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xcally.ars.controller.BaseController;

@Controller
@RequestMapping("/testxcally")
public class TestXcallyController extends BaseController{

	@Override
	protected String getPartner() {
		return "testxcally";
	}

}
