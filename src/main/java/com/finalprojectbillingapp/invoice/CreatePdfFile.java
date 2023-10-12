package com.finalprojectbillingapp.invoice;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CreatePdfFile {
    public static Object Example() {
        try {
            Document document = new Document();

            //Create OutputStream instance.
            OutputStream outputStream =
                    new FileOutputStream(new File("example.pdf"));

            //Create PDFWriter instance.
            PdfWriter.getInstance(document, outputStream);

            //Open the document.
            document.open();

            //Add content to the document.
            document.add(new Paragraph("Hello world, " +
                    "this is another file."));

            //Close document and outputStream.
            document.close();
            outputStream.close();

            System.out.println("Pdf created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
