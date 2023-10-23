package com.finalprojectbillingapp.invoice;

import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.naming.Context;
import javax.swing.text.html.HTML;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePdfFile {
  public void generatePdf(String inputFile, String outputPDF) {
      try {
          ITextRenderer renderer = new ITextRenderer();

          renderer.setDocument(inputFile);
          renderer.layout();

          try (FileOutputStream stream = new FileOutputStream(outputPDF)) {
              renderer.createPDF(stream);
          } catch (DocumentException e) {
              throw new RuntimeException(e);
          }
          System.out.println("PDF generated: " + outputPDF);
      } catch (IOException exception) {
          exception.printStackTrace();
      }
  }
}
