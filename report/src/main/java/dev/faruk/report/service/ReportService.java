package dev.faruk.report.service;

import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.commoncodebase.entity.Sale;
import dev.faruk.commoncodebase.error.AppHttpError;
import dev.faruk.commoncodebase.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final SaleRepository saleRepository;
    private final ReportPdfGenerator reportPdfGenerator;

    @Autowired
    public ReportService(SaleRepository saleRepository, ReportPdfGenerator reportPdfGenerator) {
        this.saleRepository = saleRepository;
        this.reportPdfGenerator = reportPdfGenerator;
    }

    /**
     * fetches all sales from the database and returns them as a list of SaleDTOs
     *
     * @return List of SaleDTOs
     */
    public List<SaleDTO> showSales() {
        List<Sale> sales = saleRepository.findAll();
        List<SaleDTO> saleDTOs = new ArrayList<>();
        for (Sale sale : sales) {
            saleDTOs.add(new SaleDTO(sale));
        }
        return saleDTOs;
    }

    /**
     * fetches a sale from the database by its id and returns it as a SaleDTO
     *
     * @param saleId id of the sale to be fetched
     * @return SaleDTO
     */
    public SaleDTO showSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId);
        if (sale == null) throw new AppHttpError.NotFound("Sale not found with id: %d".formatted(saleId));
        return new SaleDTO(sale);
    }

    /**
     * creates a report in html format for the sale with the given id
     *
     * @param saleId id of the sale to be reported
     * @return the html content of the report in form of report_template_tr.html
     */
    public String createReportHtml(Long saleId) {
        final Sale sale = saleRepository.findById(saleId);
        final SaleDTO saleDTO = new SaleDTO(sale);
        return reportPdfGenerator.createReportHtml(saleDTO);
    }

    /**
     * creates a report in pdf format for the sale with the given id
     *
     * @param saleId id of the sale to be reported
     * @return the byte array of the pdf report
     */
    public byte[] createReport(Long saleId) {
        final Sale sale = saleRepository.findById(saleId);
        final SaleDTO saleDTO = new SaleDTO(sale);
        return reportPdfGenerator.generatePdf(saleDTO);
    }
}
