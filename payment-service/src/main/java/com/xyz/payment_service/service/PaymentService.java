package com.xyz.payment_service.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.xyz.payment_service.domain.PaymentMethod;
import com.xyz.payment_service.payload.response.PaymentLinkResponse;
import com.xyz.payment_service.payload.dto.BookingDTO;
import com.xyz.payment_service.payload.dto.UserDTO;
import com.xyz.payment_service.model.PaymentOrder;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO user, BookingDTO bookingDTO, PaymentMethod paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorpayPaymentLink(
            UserDTO user,
            Long amount,
            Long orderId
    ) throws RazorpayException;

    String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException;
    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId,
                           String  paymentLinkId) throws RazorpayException;
}
