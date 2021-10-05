
package com.ibm.poc.report;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BirtReportService {

  void generateReports(String name, OutputType format, HttpServletResponse response, HttpServletRequest request,
      Map<String, Object> body);

}
