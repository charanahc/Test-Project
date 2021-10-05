
package com.ibm.poc.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class BirtReportServiceImpl implements BirtReportService {

	private static final Logger LOGGER = LogManager.getLogger(BirtReportServiceImpl.class);

	private static Map<String, IReportRunnable> reports = new HashMap<>();

	@Autowired
	private IReportEngine birtEngine;

	public BirtReportServiceImpl(IReportEngine birtEngine) {
		super();
		this.birtEngine = birtEngine;
	}

	public void loadReports() {
		File folder = null;
		try {
			folder = new ClassPathResource("report/").getFile();
			for (String file : Objects.requireNonNull(folder.list())) {
				if (!file.endsWith(".rptdesign")) {
					continue;
				}
				reports.put(file.replace(".rptdesign", ""),
						birtEngine.openReportDesign(folder.getAbsolutePath() + File.separator + file));
			}
		} catch (RuntimeException | IOException | BirtException e) {
			LOGGER.error("Error in Loading Reports :: {}", e);
		}
	}

	public void generateReports(String reportName, OutputType output, HttpServletResponse response,
			HttpServletRequest request, Map<String, Object> body) {
		if (!reports.containsKey(reportName)) {
			loadReports();
		}
		System.out.println();
		switch (output) {
		case PDF:
			generatePDFReport(reports.get(reportName), response, request, body);
			break;
		case GENERAL:
			IReportRunnable report = reports.get(reportName);
			generateGeneralReport(report, response, request, body);
			break;
		case DOC:
			generateDOCReport(reports.get(reportName), response, request, body);
			break;
		default:
			throw new IllegalArgumentException("Output type not recognized:" + output);
		}
	}

	public void generateGeneralReport(IReportRunnable report, ServletResponse response, HttpServletRequest request,
			Map<String, Object> body) {
		IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
		response.setContentType(birtEngine.getMIMEType("html"));
		Map<String, Object> data = (Map<String, Object>) body.get("extraParams");
		if (runAndRenderTask != null) {
			for (Entry<String, Object> set : data.entrySet()) {
				runAndRenderTask.setParameterValue(set.getKey(), set.getValue());
			}
			IRenderOption options = new RenderOption();
			runAndRenderTask.setRenderOption(options);
			runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request);
			try {
				options.setOutputStream(response.getOutputStream());
				runAndRenderTask.run();
			} catch (RuntimeException | EngineException | IOException e) {
				LOGGER.error("Error while generateGeneralReport due to : ", e);
			} finally {
				runAndRenderTask.close();
			}
		}

	}

	public void generatePDFReport(IReportRunnable report, ServletResponse response, HttpServletRequest request,
			Map<String, Object> body) {
		IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
		response.setContentType(birtEngine.getMIMEType("pdf"));
		Map<String, Object> data = (Map<String, Object>) body.get("extraParams");
		for (Entry<String, Object> set : data.entrySet()) {
			LOGGER.info(" key:{},value:{}", set.getKey(), set.getValue());
			runAndRenderTask.setParameterValue(set.getKey(), set.getValue());
		}
		IRenderOption options = new RenderOption();
		PDFRenderOption pdfRenderOption = new PDFRenderOption(options);
		pdfRenderOption.setOutputFormat("pdf");
		runAndRenderTask.setRenderOption(pdfRenderOption);
		runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, request);

		try {
			pdfRenderOption.setOutputStream(response.getOutputStream());
			runAndRenderTask.run();
		} catch (RuntimeException | IOException | EngineException e) {
			LOGGER.error("Error while generateGeneralReport due to : ", e);
		} finally {
			runAndRenderTask.close();
		}
	}
	
	public void generateDOCReport(IReportRunnable report, ServletResponse response, HttpServletRequest request,
			Map<String, Object> body) {
		IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
		response.setContentType(birtEngine.getMIMEType("doc"));
		Map<String, Object> data = (Map<String, Object>) body.get("extraParams");
		for (Entry<String, Object> set : data.entrySet()) {
			LOGGER.info(" key:{},value:{}", set.getKey(), set.getValue());
			runAndRenderTask.setParameterValue(set.getKey(), set.getValue());
		}
		IRenderOption pdfRenderOption = new RenderOption();
		pdfRenderOption.setOutputFormat("doc");

		try {
			pdfRenderOption.setOutputStream(response.getOutputStream());
			runAndRenderTask.setRenderOption(pdfRenderOption);
			runAndRenderTask.run();
		} catch (RuntimeException | IOException | EngineException e) {
			LOGGER.error("Error while generateGeneralReport due to : ", e);
		} finally {
			runAndRenderTask.close();
		}
		
//		IRunAndRenderTask task = birtEngine.createRunAndRenderTask(report);
//		try {
//
//			RenderOption options = new PDFRenderOption();
//			//RenderOption options = new RenderOption();
//			options.setOutputFormat("odt");
//			options.setOutputStream(response.getOutputStream());
//			task.setRenderOption(options);
//			task.run();
//		} catch (IOException | EngineException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			if(task!=null){
//				task.close();
//			}
//		}
		
		
	}
}
