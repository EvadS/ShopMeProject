package com.shopme.admin.controller;

import com.shopme.admin.user.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class UserCsvExporter {

    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        String timestamp = dateFormat.format(new Date());
        String fileName = "user_"+ timestamp + ".csv";

        response.setContentType("text/csv");
        String headerKey =  "Content-Disposition";
        String headerValue = "attachment; filename" + fileName;

        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter() ,
                CsvPreference.STANDARD_PREFERENCE);

        String [] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String [] fieldMapping =  {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);

        userList.forEach(i -> {
            try {
                csvWriter.write(i,fieldMapping);
            } catch (IOException e) {
               log.error(e.getMessage());
            }
        });

        csvWriter.close();

    }
}
