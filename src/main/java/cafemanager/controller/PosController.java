package cafemanager.controller;

import cafemanager.dao.ProductDAO;
import cafemanager.dto.POSOrderItem;
import cafemanager.model.Bill;
import cafemanager.model.BillDetail;
import cafemanager.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PosController {

    private final ProductDAO productDAO;
    private final BillController billController;
    private final List<POSOrderItem> orderItems;
    private List<Product> cachedProducts;

    public PosController() {
        this.productDAO = new ProductDAO();
        this.billController = new BillController();
        this.orderItems = new ArrayList<>();
        this.cachedProducts = new ArrayList<>();
    }

    // Load tất cả sản phẩm đang hoạt động
    public List<Product> loadAllActiveProducts() {
        cachedProducts = productDAO.findAllActive();
        return cachedProducts;
    }

    // Lọc sản phẩm theo tên
    public List<Product> filterProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return cachedProducts;
        }
        
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Product> filteredProducts = new ArrayList<>();
        
        for (Product product : cachedProducts) {
            if (product.getProductName() != null && product.getProductName().toLowerCase().contains(lowerKeyword)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // Thêm món vào hóa đơn (hoặc cộng dồn số lượng nếu đã có)
    public void addOrUpdateOrderItem(Product product, int quantity) {
        POSOrderItem existingItem = findOrderItem(product.getProductId());
        if (existingItem != null) {
            existingItem.addQuantity(quantity);
        } else {
            orderItems.add(new POSOrderItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    quantity));
        }
    }

    // Tìm món trong hóa đơn
    private POSOrderItem findOrderItem(int productId) {
        for (POSOrderItem item : orderItems) {
            if (item.getProductId() == productId) {
                return item;
            }
        }
        return null;
    }

    // Xóa món khỏi hóa đơn theo index
    public void removeOrderItem(int index) {
        if (index >= 0 && index < orderItems.size()) {
            orderItems.remove(index);
        }
    }

    // Hủy/Xóa toàn bộ hóa đơn
    public void clearOrder() {
        orderItems.clear();
    }

    // Lấy danh sách các món đang order
    public List<POSOrderItem> getOrderItems() {
        return orderItems;
    }

    // Kiểm tra hóa đơn có trống không
    public boolean isOrderEmpty() {
        return orderItems.isEmpty();
    }

    // Tính tổng tiền
    public BigDecimal calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (POSOrderItem item : orderItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    // Xử lý thanh toán
    public boolean processPayment(int accountId) {
        if (isOrderEmpty()) {
            return false;
        }

        Bill bill = new Bill();
        bill.setAccountId(accountId);
        bill.setStatus("PAID");

        BigDecimal total = BigDecimal.ZERO;
        List<BillDetail> details = new ArrayList<>();
        
        for (POSOrderItem item : orderItems) {
            BillDetail detail = new BillDetail(
                    item.getProductId(),
                    item.getProductName(),
                    item.getUnitPrice(),
                    item.getQuantity());
            details.add(detail);
            total = total.add(detail.getSubtotal());
        }

        bill.setTotalAmount(total);
        bill.setBillDetails(details);

        boolean success = billController.pay(bill);
        if (success) {
            clearOrder(); // Dọn dẹp giỏ hàng sau khi thanh toán thành công
        }
        return success;
    }
}