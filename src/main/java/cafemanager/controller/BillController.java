package cafemanager.controller;

import cafemanager.dao.BillDAO;
import cafemanager.model.Bill;
import cafemanager.model.BillDetail;
import java.time.LocalDate;
import java.util.List;

public class BillController {

    private final BillDAO billDAO;

    public BillController() {
        this.billDAO = new BillDAO();
    }

    public boolean pay(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ.");
        }

        if (bill.getBillDetails() == null || bill.getBillDetails().isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn chưa có sản phẩm nào.");
        }

        bill.setStatus("PAID");

        return billDAO.insertBillWithDetails(bill);
    }

    public List<Bill> getBills(LocalDate fromDate, LocalDate toDate, String status) {
        return billDAO.findBills(fromDate, toDate, status);
    }

    public List<BillDetail> getBillDetails(int billId) {
        if (billId <= 0) {
            throw new IllegalArgumentException("Mã hóa đơn không hợp lệ.");
        }
        return billDAO.findBillDetails(billId);
    }
}