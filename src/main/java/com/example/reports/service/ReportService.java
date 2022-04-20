package com.example.reports.service;

import javax.servlet.http.HttpServletResponse;


public interface ReportService {

    void makeReport(String documentName, String format, String templateName, HttpServletResponse response);
}
