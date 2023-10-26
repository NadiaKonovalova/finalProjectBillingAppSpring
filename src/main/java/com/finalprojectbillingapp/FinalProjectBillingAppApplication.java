package com.finalprojectbillingapp;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class FinalProjectBillingAppApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(FinalProjectBillingAppApplication.class, args);
//        HtmlConverter.convertToPdf(new File("./invoiceOverview.html"),
//                new File("demo-html.pdf"));
    }

}
