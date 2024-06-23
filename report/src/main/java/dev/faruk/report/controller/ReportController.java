package dev.faruk.report.controller;

import dev.faruk.commoncodebase.dto.AppSuccessResponse;
import dev.faruk.commoncodebase.dto.SaleDTO;
import dev.faruk.report.service.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<AppSuccessResponse<List<SaleDTO>>> showSales(
            @RequestParam(value = "p") Integer page,
            @RequestParam(value = "s") Integer size,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderByAsc", required = false) Boolean orderByAsc,
            @RequestParam(value = "dateFilterAfter", required = false) Long dateFilterAfter,
            @RequestParam(value = "dateFilterBefore", required = false) Long dateFilterBefore,
            @RequestParam(value = "cashierFilter", required = false) Long cashierFilterId,
            @RequestParam(value = "receivedMoneyFilterMin", required = false) Double receivedMoneyFilterMin,
            @RequestParam(value = "receivedMoneyFilterMax", required = false) Double receivedMoneyFilterMax
    ) {
        final List<SaleDTO> sales = reportService.showSales(
                page,
                size,
                orderBy,
                orderByAsc,
                dateFilterAfter,
                dateFilterBefore,
                cashierFilterId,
                receivedMoneyFilterMin,
                receivedMoneyFilterMax
        );
        return new AppSuccessResponse<>("All sales are listed successfully", sales).toResponseEntity();
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<AppSuccessResponse<SaleDTO>> showSale(@PathVariable Long saleId) {
        return new AppSuccessResponse<>("Sale is shown successfully", reportService.showSale(saleId))
                .toResponseEntity();
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
