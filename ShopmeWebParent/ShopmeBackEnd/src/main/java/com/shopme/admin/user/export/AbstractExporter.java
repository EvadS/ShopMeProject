package com.shopme.admin.user.export;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    /**
     * set response header
     * @param response
     * @param contentType
     * @param extension
     */
    public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        String timestamp = dateFormat.format(new Date());
        String fileName = "user_" + timestamp + extension;

        response.setContentType(contentType);
        String headerKey = HttpHeaders.CONTENT_DISPOSITION;
        String headerValue = "attachment; filename" + fileName;

        response.setHeader(headerKey, headerValue);
    }
}