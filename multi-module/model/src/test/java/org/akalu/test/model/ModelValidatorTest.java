package org.akalu.test.model;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * This class contains the simple form validation tests, just to be sure that
 * validator is working
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

public class ModelValidatorTest {

	private LocalValidatorFactoryBean localValidatorFactory;
	private Department dep;
	private Employee empl;

	/**
	 * Before any tests an instance of LocalValidatorFactoryBean class is
	 * created and configured.
	 * <p>
	 * NB: error messages in all test-cases are comparing by code; messages
	 * themselves are not being compared because of isolated testing
	 * methodology.
	 * 
	 * 
	 */
	@Before
	public void setup() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setUseCodeAsDefaultMessage(true);
		localValidatorFactory = new LocalValidatorFactoryBean();
		localValidatorFactory.setProviderClass(HibernateValidator.class);
		localValidatorFactory.setValidationMessageSource(messageSource);
		localValidatorFactory.afterPropertiesSet();
	}

	private Department getDep() {
		return new Department(0L, "", 1.0d);
	}

	private Employee getEmpl() {
		return new Employee(0L, "aa", "bb", "cc", new Date(), 1L, 0d);
	}

	@Test
	public void testNullDepTitleError() {
		dep = getDep();
		dep.setTitle(null);
		Set<ConstraintViolation<Department>> constraintViolations = localValidatorFactory.validate(dep);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.NotNull.message",
				constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testMinSizeDepTitleError() {
		dep = getDep();
		dep.setTitle("a");
		Set<ConstraintViolation<Department>> constraintViolations = localValidatorFactory.validate(dep);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.Size.message", constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testMaxSizeDepTitleError() {
		String block = new String("aaaaaaaaaa");
		String title = "";
		for (int i = 0; i < 26; i++)
			title.concat(block);
		dep = getDep();
		dep.setTitle(title);
		Set<ConstraintViolation<Department>> constraintViolations = localValidatorFactory.validate(dep);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.Size.message", constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testNormalSizeDepTitle() {
		dep = getDep();
		dep.setTitle("aaa");
		Set<ConstraintViolation<Department>> constraintViolations = localValidatorFactory.validate(dep);
		assertEquals(0, constraintViolations.size());

	}

	@Test
	public void testNullEmplNamesError() {
		empl = getEmpl();
		empl.setFirstName(null);
		empl.setSecondName(null);
		empl.setSurname(null);
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(3, constraintViolations.size());
		assertEquals("javax.validation.constraints.NotNull.message",
				constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testNullEmplDepIdError() {
		empl = getEmpl();
		empl.setDepId(null);
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.NotNull.message",
				constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testSizeEmplNamesError() {
		empl = getEmpl();
		empl.setFirstName("a");
		empl.setSecondName("b");
		empl.setSurname("");
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(3, constraintViolations.size());
		assertEquals("javax.validation.constraints.Size.message", constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testDateEmplError1() {
		empl = getEmpl();
		empl.setDob(null);
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.NotNull.message",
				constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testDateEmplError2() {
		empl = getEmpl();
		empl.setDob(Date.from(Instant.now().plusSeconds(86500)));
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.Past.message", constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testSalaryEmplError1() {
		empl = getEmpl();
		empl.setSalary(-1d);
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.Min.message", constraintViolations.iterator().next().getMessage());

	}

	@Test
	public void testSalaryEmplError2() {
		empl = getEmpl();
		empl.setSalary(100000000000d);
		Set<ConstraintViolation<Employee>> constraintViolations = localValidatorFactory.validate(empl);
		assertEquals(1, constraintViolations.size());
		assertEquals("javax.validation.constraints.Max.message", constraintViolations.iterator().next().getMessage());

	}

}