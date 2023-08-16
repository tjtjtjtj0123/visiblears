package com.xcally.ars.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xcally.ars.controller.BaseController;

@Controller
@RequestMapping("/sornia")
public class SorniaController  extends BaseController{

	@Override
	protected String getPartner() {
		return "sornia";
	}

}
