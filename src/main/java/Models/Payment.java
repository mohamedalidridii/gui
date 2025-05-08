package Models;

import java.time.LocalDateTime;

public class Payment {
        private int id;
        private int bookingId;
        private double amount;
        private LocalDateTime paymentDate;
        private String status;

        public Payment(int id, int bookingId, double amount, LocalDateTime paymentDate, String status) {
            this.id = id;
            this.bookingId = bookingId;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.status = status;
        }


        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public LocalDateTime getPaymentDate() { return paymentDate; }
        public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
}
