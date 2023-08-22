package com.xcally.ars.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xcally.ars.controller.BaseController;

@Controller
@RequestMapping("/therapedic")
public class TherapedicController  extends BaseController{

	@Override
	protected String getPartner() {
		return "therapedic";
	}

}
