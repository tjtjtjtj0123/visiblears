package com.xcally.ars.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xcally.ars.controller.BaseController;

@Controller
@RequestMapping("/testsornia")
public class TestSorniaController  extends BaseController{

	@Override
	protected String getPartner() {
		return "testsornia";
	}

}
