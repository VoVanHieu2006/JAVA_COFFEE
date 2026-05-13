package cafemanager.controller;

import cafemanager.dao.BillDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatsController {
    private final BillDAO billDAO = new BillDAO();

    public BigDecimal getTotalRevenue(LocalDate fromDate, LocalDate toDate) throws Exception {
        return billDAO.getTotalRevenue(normalizeFrom(fromDate), normalizeTo(toDate));
    }

    public int getTotalBills(LocalDate fromDate, LocalDate toDate) throws Exception {
        return billDAO.getTotalBills(normalizeFrom(fromDate), normalizeTo(toDate));
    }

    public Map<LocalDate, BigDecimal> getRevenueByDateRange(LocalDate fromDate, LocalDate toDate) throws Exception {
        return billDAO.getRevenueByDateRange(normalizeFrom(fromDate), normalizeTo(toDate));
    }

    public List<Object[]> getTopSellingProducts(LocalDate fromDate, LocalDate toDate, int limit) throws Exception {
        return billDAO.getTopSellingProducts(normalizeFrom(fromDate), normalizeTo(toDate), Math.max(1, limit));
    }

    private LocalDate normalizeFrom(LocalDate fromDate) {
        return fromDate == null ? LocalDate.now().withDayOfMonth(1) : fromDate;
    }

    private LocalDate normalizeTo(LocalDate toDate) {
        return toDate == null ? LocalDate.now() : toDate;
    }
}
