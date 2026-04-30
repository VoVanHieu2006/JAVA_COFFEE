package cafemanager.model;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "bill")
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    
    private double totalAmount;
    private String status; // Enum: Pending, Paid, Cancelled
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillDetail> billDetails;
        
    
    public Bill() {}

    public Bill(int billId, Date createdAt, double totalAmount, String status, Account account, List<BillDetail> billDetails) {
        this.billId = billId;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.status = status;
        this.account = account;
        this.billDetails = billDetails;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    
}