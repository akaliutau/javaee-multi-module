package org.akalu.webclient;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.akalu.dataclient.DataClient;
import org.akalu.dataclient.ListOption;
import org.akalu.dataclient.ListOptions;
import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MainController handles requests for the application home page. The sole
 * purpose of this class is to display a list of departments and a list of
 * employees in chosen department.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Controller
public class MainController {
	@Autowired
	private DataClient dataClient;

	private static final Logger logger = Logger.getLogger(MainController.class);

	private static Long selectedDepId = -1L;

	/**
	 * Method forms all the necessary lists and variables needed for web page
	 * components.
	 * 
	 * <p>
	 * NB: In real production system exception HttpClientErrorException must be
	 * caught in try-catch block any time when the attempt of access to remote
	 * server is made.
	 * 
	 * <p>
	 * If the server is off-line, an error page with appropriate message is
	 * displayed. All other http-like errors are caught by container.
	 * 
	 * @param did
	 *            - contains id of chosen department (usually this parameter set
	 *            by Javascript)
	 * @param page
	 *            - serve as relative index of the partial list-page (usually
	 *            this parameter set by Javascript)
	 * 
	 * @return index.jsp
	 * @return error.jsp (on some condition)
	 */

	@RequestMapping(value = LocalURI.INDEX, method = RequestMethod.GET)
	public ModelAndView getdata(@RequestParam(value = "depid", required = false, defaultValue = "-1") Long did,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {

		try {
			// choose page if needed
			if (page != 0) {
				switch (page) {
				case 2:
					if (dataClient.getEmplPageDescriptor().goNext())
						dataClient.setUpdateEmpl(true);
					break;
				case -2:
					if (dataClient.getEmplPageDescriptor().goPrevious())
						dataClient.setUpdateEmpl(true);
					break;
				case 1:
					if (dataClient.getDepPageDescriptor().goNext())
						dataClient.setUpdateDep(true);
					break;
				case -1:
					if (dataClient.getDepPageDescriptor().goPrevious())
						dataClient.setUpdateDep(true);
					break;
				}
			}

			if (dataClient.getDepPageDescriptor() == null)
				dataClient.init();

			// get all lists from server
			List<Department> list1 = dataClient.getdepList();

			List<ListOption> lst = dataClient.getOptList();

			// Analysis Of Variants

			if (lst != null)
				if (selectedDepId == -1 && page == 0) {
					// page loaded 1st time or clear reload
					if (!lst.isEmpty())
						selectedDepId = lst.get(0).getId();
				} else {
					if (did != -1) {
						selectedDepId = did;// call from js
						dataClient.setUpdateEmpl(true);
						dataClient.init2(selectedDepId);
					}
				}

			ListOptions lo = new ListOptions(lst, selectedDepId);
			if (dataClient.getEmplPageDescriptor() == null)
				dataClient.init2(selectedDepId);

			List<Employee> list2 = dataClient.getemplList(selectedDepId);

			ModelAndView model = new ModelAndView("index");
			model.addObject("depList", list1);
			model.addObject("emplList", list2);
			model.addObject("depOpt", lo);
			model.addObject("depPage", dataClient.getDepPageDescriptor());
			model.addObject("emplPage", dataClient.getEmplPageDescriptor());

			logger.debug("return index page");

			return model;

		} catch (HttpClientErrorException e) {
			ModelAndView model = new ModelAndView("error");
			logger.debug("connection error");
			return model;
		}

	}

}