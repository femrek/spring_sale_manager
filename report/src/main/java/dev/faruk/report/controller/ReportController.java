package dev.faruk.report.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/")
    public AppSuccessResponse<List<SaleDTO>> showSales() {
        return new AppSuccessResponse<>("All sales are listed successfully", reportService.showSales());
    }

    @GetMapping("/{saleId}")
    public AppSuccessResponse<SaleDTO> showSale(@PathVariable Long saleId) {
        return new AppSuccessResponse<>("Sale is shown successfully", reportService.showSale(saleId));
    }

    @GetMapping("/{saleId}/receipt-html")
    public ResponseEntity<byte[]> showReceiptHtml(@PathVariable Long saleId) {
        final String report = reportService.createReportHtml(saleId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=receipt.html")
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(report.getBytes());
    }

    @GetMapping("/{saleId}/receipt")
    public ResponseEntity<byte[]> showReceipt(@PathVariable Long saleId) {
        final byte[] report = reportService.createReport(saleId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=receipt.pdf")
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .body(report);
    }
}
