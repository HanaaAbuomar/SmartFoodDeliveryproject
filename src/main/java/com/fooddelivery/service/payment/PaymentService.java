package com.fooddelivery.service.payment;

public class PaymentService {

    /**
     * معالجة والتحقق من الدفع النقدي (Cash Payment)
     * @param totalAmount إجمالي الفاتورة المطلوب دفعه
     * @param cashProvided المبلغ النقدي الذي دفعه العميل
     * @return حالة الدفع (SUCCESS أو FAILED)
     */
    public PaymentStatus processCashPayment(double totalAmount, double cashProvided) {
        if (totalAmount < 0 || cashProvided < 0) {
            throw new IllegalArgumentException("Amounts cannot be negative");
        }
        
        // إذا كان المبلغ المدفوع يغطي إجمالي الفاتورة أو يزيد عنه
        if (cashProvided >= totalAmount) {
            return PaymentStatus.SUCCESS;
        } else {
            return PaymentStatus.FAILED;
        }
    }
}