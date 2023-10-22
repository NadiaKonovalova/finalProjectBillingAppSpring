package com.finalprojectbillingapp.invoice;


import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.swing.text.html.HTML;
import java.io.*;


public class CreatePdfFile {

    String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "BillingApp");

        return templateEngine.process("invoiceOverview", context);
    }

    public void generatePdfFromHtml(HTML html) throws IOException, DocumentException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(String.valueOf(html));
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}


