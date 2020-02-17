package org.akalu.webclient;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.akalu.dataclient.DataClient;
import org.akalu.model.Dob;
import org.akalu.model.Dob2;
import org.akalu.model.Employee;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SearchController handles requests for the search page.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Controller
public class SearchController {
	@Autowired
	private DataClient dataClient;

	private static final Logger logger = Logger.getLogger(SearchController.class);

	private static final String EMPTYMSG = "";
	private static String stitle = "";

	@RequestMapping(value = LocalURI.NEW_SEARCH, method = RequestMethod.GET)
	public ModelAndView showSearchPage() {

		// get all lists from server
		List<Employee> resList = null;

		Dob dob = new Dob();
		Dob2 dob2 = new Dob2();

		ModelAndView model = new ModelAndView("search");
		model.addObject("searchmodelattr", dob);
		model.addObject("searchmodelattr2", dob2);
		model.addObject("resultstitle", EMPTYMSG);
		model.addObject("foundList", resList);

		logger.debug("return search page");

		return model;

	}

	@RequestMapping(value = LocalURI.SEARCH1, method = RequestMethod.POST)
	public String showResultPage1(@Valid @ModelAttribute("searchmodelattr") Dob dob, BindingResult bindingResult1,
			@ModelAttribute("searchmodelattr2") Dob2 dob2, ModelMap model) {

		if (bindingResult1.hasErrors()) {
			return "search";
		}
		try {
			List<Employee> resList = dataClient.getFoundList(dob.getDob());

			if (resList != null) {
				Integer n = resList.size();
				stitle = "Records found: " + n;
			}
			logger.debug("Search by 1 date:" + dob.getDob());

			model.addAttribute("searchmodelattr", dob);
			model.addAttribute("searchmodelattr2", dob2);
			model.addAttribute("resultstitle", stitle);
			model.addAttribute("foundList", resList);

			return "search";
		} catch (HttpClientErrorException e) {
			logger.debug("connection error");
			return "error";
		}
	}

	@RequestMapping(value = LocalURI.SEARCH2, method = RequestMethod.POST)
	public String showResultPage2(@ModelAttribute("searchmodelattr") Dob dob,
			@Valid @ModelAttribute("searchmodelattr2") Dob2 dob2, BindingResult bindingResult2, ModelMap model) {

		if (bindingResult2.hasErrors()) {
			return "search";
		}
		try {
			logger.debug("Search by 2 dates:" + dob2.getDob1() + " and " + dob2.getDob2());

			List<Employee> resList = dataClient.getFoundList(dob2.getDob1(), dob2.getDob2());

			if (resList != null) {
				Integer n = resList.size();
				stitle = "Records found: " + n;
			}

			model.addAttribute("searchmodelattr", dob);
			model.addAttribute("searchmodelattr2", dob2);
			model.addAttribute("resultstitle", stitle);
			model.addAttribute("foundList", resList);

			return "search";
		} catch (HttpClientErrorException e) {
			logger.debug("connection error");
			return "error";
		}
	}

}