package com.finalprojectbillingapp.invoice;



import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.swing.text.html.HTML;
import java.io.*;
import com.itextpdf.html2pdf.HtmlConverter;

/*public class CreatePdfFile {
    public void convertToPdf(){
        try {
            String htmlContent = "<body>\n" +
                    "<main class=\"container\">\n" +
                    "    <h3 style=\"text-align: center; margin: 80px 0px 50px 0px; color: #333333;\">Invoice Overview</h3>\n" +
                    "    <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <div class=\"invoice-info\">\n" +
                    "                    <p>Invoice No.: &nbsp;<span th:text=\"${invoice.invoiceNumber}\"></span></p>\n" +
                    "                    <p>Issued on: &nbsp;&nbsp;&nbsp;<span th:text=\"${invoice.issuedAt}\"></span></p>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <table style=\"width: 100%;\">\n" +
                    "                    <tr>\n" +
                    "                        <td style=\"width: 50%;\">\n" +
                    "                            <div class=\"user-info\">\n" +
                    "                                <p class=\"section-title\" style=\"font-weight: bold;\">Seller:</p>\n" +
                    "                                <p>Name: <span th:text=\"${invoice.user.name}\"></span></p>\n" +
                    "                                <p>Taxpayer number: <span th:text=\"${invoice.user.taxpayerNo}\"></span></p>\n" +
                    "                                <p>Address: <span th:text=\"${invoice.user.legalAddress}\"></span></p>\n" +
                    "                                <p>Country: <span th:text=\"${invoice.user.country.getDisplayCountryName()}\"></span></p>\n" +
                    "                            </div>\n" +
                    "                        </td>\n" +
                    "                        <td style=\"width: 50%;\">\n" +
                    "                            <div class=\"customer-info\">\n" +
                    "                                <p class=\"section-title\" style=\"font-weight: bold;\">Buyer:</p>\n" +
                    "                                <p>Customer name: <span th:text=\"${invoice.customer.name}\"></span></p>\n" +
                    "                                <p>Taxpayer number: <span th:text=\"${invoice.customer.taxpayerNo}\"></span></p>\n" +
                    "                                <p>Address: <span th:text=\"${invoice.customer.legalAddress}\"></span></p>\n" +
                    "                                <p>Country: <span th:text=\"${invoice.customer.country.getDisplayCountryName()}\"></span></p>\n" +
                    "                            </div>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "            <div class=\"product-service-info\">\n" +
                    "                <p class=\"section-title\" style=\"font-weight: bold;\">Product/Service:</p>\n" +
                    "                <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                    "                    <tr>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Name</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Quantity</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Unit</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Unit price</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Currency</th>\n" +
                    "                        <th style=\"border: 1px solid #ccc;\">Total per item</th>\n" +
                    "                    </tr>\n" +
                    "                    <tr th:each=\"product : ${products}\">\n" +
                    "                        <td th:text=\"${product.name}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.quantity,1,2,'COMMA')}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${product.unit}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.unitPrice,1, 2,'COMMA')}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${product.currency}\" style=\"border: 1px solid #ccc;\"></td>\n" +
                    "                        <td th:text=\"${#numbers.formatDecimal(product.totalPerProduct,1, 2,'COMMA')}\" style=\"border: 1px solid #ccc;\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td></td>\n" +
                    "                        <td style=\"font-weight: bold;\">Total:</td>\n" +
                    "\n" +
                    "                        <td style=\"border: 1px solid #ccc; text-align: left;\"\n" +
                    "                            th:text=\"${#numbers.formatDecimal(invoice.totalPrice,1,2,'COMMA')}\"\n" +
                    "                        </td>\n" +
                    "<!--                        <td style=\"border: 1px solid #ccc; text-align: left;\"-->\n" +
                    "<!--                            th:text=\"${#numbers.formatDecimal(invoice.totalPrice,1,2,'COMMA') + ' ' + product.currency}\"-->\n" +
                    "<!--                    </td>-->\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        </tr>\n" +
                    "<!--        <tr>-->\n" +
                    "<!--            <td style=\"padding: 10px; border: 1px solid #ccc; text-align: right;\">-->\n" +
                    "<!--                <div class=\"totalPrice\">-->\n" +
                    "<!--                    <p>Total: <span th:text=\"${invoice.totalPrice}\"  style=\"text-align: right;\"></span></p>-->\n" +
                    "<!--                </div>-->\n" +
                    "<!--            </td>-->\n" +
                    "<!--        </tr>-->\n" +
                    "\n" +
                    "        <tr>\n" +
                    "            <td style=\"padding: 10px; border: 1px solid #ccc;\">\n" +
                    "                <div class=\"invoice-data\">\n" +
                    "                    <p>Notes: <span th:text=\"${invoice.notes}\"></span></p>\n" +
                    "                    <p>Issued on: <span th:text=\"${invoice.issuedAt}\"></span></p>\n" +
                    "                    <p>Due by: <span th:text=\"${invoice.dueBy}\"></span></p>\n" +
                    "                    <p>Signature: <span th:text=\"${invoice.methodOfSigning.getDisplaySignatureName()}\"></span></p>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "    <section style=\"text-align: center; margin-top: 20px; margin-bottom: 20px;\">\n" +
                    "        <form th:action=\"@{/confirm-invoice}\" method=\"post\" style=\"display: inline-block;\">\n" +
                    "            <button type=\"submit\" class=\"btn btn-dark text-white p-3\" style=\"font-weight: bold;\">Confirm Invoice</button>\n" +
                    "        </form>\n" +
                    "        <form th:action=\"@{/cancel-invoice}\" method=\"post\" style=\"display: inline-block; margin-right: 10px;\">\n" +
                    "            <button type=\"submit\" class=\"btn btn-dark text-white p-3\" style=\"font-weight: bold;\">Cancel Invoice</button>\n" +
                    "        </form>\n" +
                    "<!--        <form th:action=\"@{/generate-pdf-invoice}\" method=\"get\" style=\"display: inline-block;\">-->\n" +
                    "<!--            <button type=\"submit\" class=\"btn btn-dark text-white p-3\" style=\"font-weight: bold;\">Create PDF</button>-->\n" +
                    "<!--        </form>-->\n" +
                    "    </section>\n" +
                    "</main>\n" +
                    "</body>";
            // Замените этот HTML на ваш HTML-код

            // Указываем путь к файлу PDF, куда будет сохранен результат
            String pdfFilePath = "invoice.pdf";

            // Создаем файл для сохранения PDF
            File pdfFile = new File(pdfFilePath);
            pdfFile.createNewFile();

            // Открываем поток для записи PDF-файла
            FileOutputStream fos = new FileOutputStream(pdfFile);

            // Настройки конвертера
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setBaseUri("file:/path/to/your/html/"); // Путь к вашим ресурсам (стили, изображения)

            // Конвертируем HTML в PDF
            HtmlConverter.convertToPdf(htmlContent, fos, converterProperties);

            // Закрываем поток
            fos.close();

            System.out.println("HTML успешно сконвертирован в PDF: " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

 /*   String parseThymeleafTemplate() {
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

    public class CreatePdfFile {

      //  public static void main(String[] args) throws IOException {

            HtmlConverter.convertToPdf(new File("./pdf-input.html"),new File("demo-html.pdf"));
        }
}*/

