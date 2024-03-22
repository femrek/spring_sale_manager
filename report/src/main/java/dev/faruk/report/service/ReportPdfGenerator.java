package dev.faruk.report.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.report.dto.CompanyInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;

@Service
public class ReportPdfGenerator {
    private final SpringTemplateEngine springTemplateEngine;

    @Autowired
    public ReportPdfGenerator(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    /**
     * creates the html report for the given sale and default company info
     *
     * @param saleDTO sale to be reported
     * @return the html content of the report in form of report_template_tr.html
     */
    public String createReportHtml(SaleDTO saleDTO) {
        Context dataContext = new Context();
        dataContext.setVariable("sale", saleDTO);
        dataContext.setVariable("company", CompanyInfoDTO.initialCompanyInfo);
        return springTemplateEngine.process("report_template_tr", dataContext);
    }

    /**
     * generates a pdf report for the given sale by {@link #createReportHtml(SaleDTO) createReportHtml}
     *
     * @param saleDTO sale to be reported
     * @return the byte array of the pdf report
     */
    public byte[] generatePdf(SaleDTO saleDTO) {
        String html = createReportHtml(saleDTO);
        return htmlToPdf(html);
    }

    /**
     * converts the given html content to a pdf byte array. renders the html content twice for determining the height
     * of the pdf page.
     *
     * @param processedHtml html content to be converted
     * @return the byte array of the pdf
     */
    private byte[] htmlToPdf(String processedHtml) {
        final float pageWidth = PageSize.A6.getWidth();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            DefaultFontProvider defaultFont = new DefaultFontProvider(
                    false,
                    false,
                    true);
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFont);

            // render the html content to determine the height of the content
            PdfDocument pdfDocumentTemp = new PdfDocument(pdfWriter);
            pdfDocumentTemp.setDefaultPageSize(new PageSize(pageWidth, PageSize.A10.getHeight()));
            HtmlConverter.convertToPdf(processedHtml, pdfDocumentTemp, converterProperties);

            // read the rendered content to determine the height of the content
            PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            PdfDocument pdfDocumentReader = new PdfDocument(pdfReader);

            // calculate the total height of the content
            final float verticalIndent = 36f; // vertical margin of the rendered pdf
            float totalHeight = 0;
            for (int i = 1; i <= pdfDocumentReader.getNumberOfPages(); i++) {
                final float _height = pdfDocumentReader.getPage(i).getPageSize().getHeight();
                totalHeight += _height - verticalIndent * 2f;
            }
            totalHeight += verticalIndent * 2f;

            // configure result pdf document with the calculated height
            PageSize pageSize = new PageSize(pageWidth, totalHeight);
            ByteArrayOutputStream baosResult = new ByteArrayOutputStream();
            PdfWriter pdfWriterResult = new PdfWriter(baosResult);
            PdfDocument pdfDocumentResult = new PdfDocument(pdfWriterResult);
            pdfDocumentResult.setDefaultPageSize(pageSize);

            // render the result pdf
            HtmlConverter.convertToPdf(processedHtml, pdfDocumentResult, converterProperties);

            // close the documents and writers
            pdfDocumentTemp.close();
            pdfWriter.close();
            pdfReader.close();
            pdfDocumentReader.close();
            pdfDocumentResult.close();
            pdfWriterResult.close();

            // return the result pdf as byte array
            return baosResult.toByteArray();
        } catch (Exception e) {
            throw new AppHttpError.InternalServerError("Error while generating pdf: %s".formatted(e.getMessage()));
        }
    }
}
