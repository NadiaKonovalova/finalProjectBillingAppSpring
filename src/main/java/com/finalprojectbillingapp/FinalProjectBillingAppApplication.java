package com.finalprojectbillingapp;

import com.finalprojectbillingapp.invoice.CreatePdfFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalProjectBillingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectBillingAppApplication.class, args);
        CreatePdfFile createPdfFile = new CreatePdfFile();
        createPdfFile.Example();
    }


}
