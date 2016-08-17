package org.akalu.webclient;

import javax.validation.Valid;

import org.akalu.dataclient.DataClient;
import org.akalu.model.Department;

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
 * DepController handles requests for the add/edit department page.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@Controller
@RequestMapping(value = LocalURI.DEP_DIR)
public class DepController{

	@Autowired
	private DataClient dataClient;
 

	private static final Logger logger = Logger.getLogger(DepController.class);
 	
	private static final String MSG = "New department was succesfully added";
	private static final String EMPTYMSG = "";



@RequestMapping(value = LocalURI.ADD_NEW_DEP, method = RequestMethod.GET)
public ModelAndView showAddDepartmentPage() {

	ModelAndView model = new ModelAndView("adddep");

	model.addObject("depmodelattr", new Department());
	model.addObject("message", EMPTYMSG);
	
	logger.debug("return adddep page"); 

	return model;
}


@RequestMapping(value = LocalURI.ADD_DEP, method = RequestMethod.POST)
public String addDepartmentPage(@Valid @ModelAttribute("depmodelattr") Department dep, 
		BindingResult bindingResult, ModelMap model) {

	if (bindingResult.hasErrors()) {
		model.addAttribute("message", EMPTYMSG);
		return "adddep";
	}
	try{
		dataClient.addDep(dep);
		dataClient.setUpdateDep(true);
		model.addAttribute("depmodelattr", dep);
		model.addAttribute("message", MSG);
		logger.debug("add new department with id ="+dep.getId()); 

		return "adddep";
	}catch(HttpClientErrorException e){
		logger.debug("connection error"); 
		return "error";
	}
}

@RequestMapping(value = LocalURI.EDIT_DEP, method = RequestMethod.GET)
public ModelAndView showEditDepartmentPage(@RequestParam(value = "id") Integer idx) {

	ModelAndView model = new ModelAndView("editdep");

	Department dep = dataClient.getDepbyIdx(idx);
		model.addObject("depmodelattr", dep);
		model.addObject("message", EMPTYMSG);
		logger.debug("return editdep page"); 

		return model;
	
}

@RequestMapping(value = LocalURI.UPDATE_DEP, method = RequestMethod.POST)
public String showEditDepartmentPage(@Valid @ModelAttribute("depmodelattr") Department dep, 
		BindingResult bindingResult, ModelMap model) {

	if (bindingResult.hasErrors()) {
		return "editdep";
	}
	try{
		dataClient.updateDep(dep);
		dataClient.setUpdateDep(true);
		logger.debug("update department with id ="+dep.getId()); 

		return "redirect: ../";
	}catch(HttpClientErrorException e){
		logger.debug("connection error"); 
		return "error";
	}
}

@RequestMapping(value = LocalURI.DEL_DEP, method = RequestMethod.GET)
public String delDepartment(ModelMap model, @RequestParam(value = "id") Integer idx) {

	Long id = dataClient.getDepId(idx);
	
	try{
		dataClient.deleteDep(id);
		dataClient.setUpdateDep(true);
		logger.debug("delete department with id ="+id); 

		return "redirect: ../";
	}catch(HttpClientErrorException e){
		logger.debug("connection error"); 
		return "error";
	}
}

}	
	