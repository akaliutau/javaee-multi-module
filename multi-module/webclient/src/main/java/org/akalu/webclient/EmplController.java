package org.akalu.webclient;

import java.util.List;

import javax.validation.Valid;

import org.akalu.dataclient.DataClient;
import org.akalu.dataclient.ListOption;
import org.akalu.dataclient.ListOptions;
import org.akalu.model.Employee;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

/**
 * EmplController handles requests for the add/edit employee page.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Controller
@RequestMapping(value = LocalURI.EMPL_DIR)
public class EmplController {

	@Autowired
	private DataClient dataClient;

	private static final Logger logger = Logger.getLogger(EmplController.class);

	private static final String MSG = "New employee was succesfully added";
	private static final String EMPTYMSG = "";

	@RequestMapping(value = LocalURI.ADD_NEW_EMPL, method = RequestMethod.GET)
	public ModelAndView showAddEmployeePage() {

		try {
			ModelAndView model = new ModelAndView("addempl");

			List<ListOption> lst = dataClient.getOptList();
			Long did = 0L;
			if (!lst.isEmpty())
				did = lst.get(0).getId();

			ListOptions lo = new ListOptions(lst, did);

			model.addObject("depOpt", lo);
			model.addObject("emplmodelattr", new Employee());
			model.addObject("message", EMPTYMSG);

			logger.debug("return addempl page");

			return model;
		} catch (HttpClientErrorException e1) {
			logger.debug("connection error");
			ModelAndView model = new ModelAndView("error");
			return model;
		}

	}

	@RequestMapping(value = LocalURI.ADD_EMPL, method = RequestMethod.POST)
	public String addEmployeePage(@Valid @ModelAttribute("emplmodelattr") Employee em, BindingResult bindingResult,
			ModelMap model) {

		try {
			if (bindingResult.hasErrors()) {

				ListOptions lo = new ListOptions(dataClient.getOptList(), em.getDepId());
				model.addAttribute("depOpt", lo);
				return "addempl";
			}

			// try to save valid data in db, display appropriate message on page
			dataClient.addEmpl(em);
			dataClient.setUpdateEmpl(true);
			dataClient.setUpdateDep(true);
			logger.debug("add new employee with id =" + em.getId());

			return "redirect: ../";
		} catch (HttpClientErrorException e) {
			logger.debug("connection error");
			return "error";
		}
	}

	@RequestMapping(value = LocalURI.EDIT_EMPL, method = RequestMethod.GET)
	public String showEditEmployeePage(ModelMap model, @RequestParam(value = "id") Integer idx) {

		try {

			Employee em = dataClient.getEmplbyIdx(idx);

			ListOptions lo = new ListOptions(dataClient.getOptList(), em.getDepId());

			model.addAttribute("depOpt", lo);
			model.addAttribute("emplmodelattr", em);
			model.addAttribute("message", EMPTYMSG);

			logger.debug("return editempl page");

			return "editempl";
		} catch (HttpClientErrorException e1) {
			logger.debug("connection error");
			return "error";
		}

	}

	@RequestMapping(value = LocalURI.UPDATE_EMPL, method = RequestMethod.POST)
	public String showEditEmployeePage(@Valid @ModelAttribute("emplmodelattr") Employee e, BindingResult bindingResult,
			ModelMap model) {

		try {
			if (bindingResult.hasErrors()) {
				ListOptions lo = new ListOptions(dataClient.getOptList(), e.getDepId());
				model.addAttribute("depOpt", lo);
				return "editempl";
			}
			dataClient.updateEmpl(e);
			dataClient.setUpdateEmpl(true);
			dataClient.setUpdateDep(true);

			logger.debug("update employee with id =" + e.getId());

			return "redirect: ../";
		} catch (HttpClientErrorException e1) {
			logger.debug("connection error");
			return "error";
		}
	}

	@RequestMapping(value = LocalURI.DEL_EMPL, method = RequestMethod.GET)
	public String delEmployee(ModelMap model, @RequestParam(value = "id") Integer idx) {

		Long id = dataClient.getEmplId(idx);
		try {
			dataClient.deleteEmpl(id);
			dataClient.setUpdateEmpl(true);
			dataClient.setUpdateDep(true);

			logger.debug("delete employee with id =" + id);
			return "redirect: ../";
		} catch (HttpClientErrorException e) {
			logger.debug("connection error");
			return "error";
		}
	}

}
