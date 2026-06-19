package com.xyz.payment_service.controller;


import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.xyz.payment_service.domain.PaymentMethod;
import com.xyz.payment_service.model.PaymentOrder;
import com.xyz.payment_service.payload.dto.BookingDTO;
import com.xyz.payment_service.payload.dto.BookingRequest;
import com.xyz.payment_service.payload.dto.UserDTO;
import com.xyz.payment_service.payload.response.PaymentLinkResponse;
import com.xyz.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping ("/create" )
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO booking,
            @RequestParam PaymentMethod paymentMethod
            ) throws RazorpayException, StripeException {
        UserDTO user = new UserDTO();
        user.setFullName("John");
        user.setEmail("john@example.com");
        user.setId(1L);

        PaymentLinkResponse res=paymentService.createOrder(user,booking,paymentMethod);

        return ResponseEntity.ok(res);
    }

    @GetMapping ("/{paymentOrderId}" )
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
           @PathVariable Long paymentOrderId

               ) throws Exception {



        PaymentOrder res=paymentService.getPaymentOrderById(paymentOrderId);

        return ResponseEntity.ok(res);
    }


    @PatchMapping ("/{proceed}" )
    public ResponseEntity<Boolean> proceedPayment(
           @RequestParam String paymentId,
           @RequestParam String paymentLinkId

    ) throws Exception {


        PaymentOrder paymentOrder=paymentService.getPaymentOrderByPaymentId
                (paymentLinkId);

        Boolean res=paymentService.proceedPayment(paymentOrder,paymentId,paymentLinkId);


        return ResponseEntity.ok(res);
    }

}
