
package com.ibm.poc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.poc.report.BirtReportService;
import com.ibm.poc.report.OutputType;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/report")
@CrossOrigin
@Api(value = "Report Controller")
public class BirtReportController {

	@Autowired
	private BirtReportService reportService;

	@PostMapping("/generateReport/{name}")
	public void generateReport(HttpServletResponse response, HttpServletRequest request,
			@PathVariable("name") String name, @RequestBody Map<String, Object> body) {

		System.out.println("Generating full report:{}{}" + name + " " + body.get("outputFormat"));
		OutputType format = OutputType.from(body.get("outputFormat").toString());
		System.out.println(format);
		try {
			reportService.generateReports(name, format, response, request, body);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}
