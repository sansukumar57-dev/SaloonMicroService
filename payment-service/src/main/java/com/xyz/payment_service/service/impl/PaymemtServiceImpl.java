package com.xyz.payment_service.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.xyz.payment_service.domain.PaymentMethod;
import com.xyz.payment_service.domain.PaymentOrderStatus;
import com.xyz.payment_service.model.PaymentOrder;
import com.xyz.payment_service.payload.dto.BookingDTO;
import com.xyz.payment_service.payload.dto.UserDTO;
import com.xyz.payment_service.payload.response.PaymentLinkResponse;
import com.xyz.payment_service.repository.PaymentOrderRepository;
import com.xyz.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymemtServiceImpl implements PaymentService {


    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Value("${razorpay.api.key}")
    private String razorpayApiKey;
    @Value("${razorpay.api.secret}")
    private String razorpaySecretKey;
    @Override
    public PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException {



     Long amount=(long) booking.getTotalPrice();
     PaymentOrder order=new PaymentOrder();
     order.setAmount(amount);
     order.setPaymentMethod(paymentMethod);
     order.setBookingId(booking.getId());
     order.setSalonId(booking.getSalonId());
     PaymentOrder savedOrder=paymentOrderRepository.save(order);

     PaymentLinkResponse paymentLinkResponse=new PaymentLinkResponse();
     if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
         PaymentLink payment=createRazorpayPaymentLink(user,
                 savedOrder.getAmount(),
                 savedOrder.getId());

String paymentUrl=payment.get("short_url");
String  paymentUrlId=payment.get("id");
paymentLinkResponse.setGetPayment_link_id(paymentUrl);

paymentLinkResponse.setGetPayment_link_id(paymentUrlId);

savedOrder.setPaymentLinkId(paymentUrlId);
paymentOrderRepository.save(savedOrder);
     }else{
         String paymentUrl=createStripePaymentLink(
                 user,
                 savedOrder.getAmount(),
                 savedOrder.getId());

                 paymentLinkResponse.setPayment_link_url(paymentUrl);
     }
     return paymentLinkResponse;

    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder=paymentOrderRepository.findById(id).orElse(null);
        if(paymentOrder==null){
            throw new Exception("Payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {

        return paymentOrderRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO user,

                                                 Long Amount,
                                                 Long orderId) throws RazorpayException {
    Long amount=Amount*100;

        RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpaySecretKey);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());
        paymentLinkRequest.put("customer", customer);

        JSONObject notify=new JSONObject();
        notify.put("email",true);
        paymentLinkRequest.put("notify", notify);
        paymentLinkRequest.put("remainder_enable", true);

        paymentLinkRequest.put("callback_url", "https://localhost:3000/payment-sucsess/"+orderId);
        paymentLinkRequest.put("callback_method", "get");

        return razorpay.paymentLink.create(paymentLinkRequest);
    }
    @Override
    public String createStripePaymentLink(
            UserDTO user,
            Long amount,
            Long orderId) throws StripeException { Stripe.apiKey = stripeSecretKey;


        SessionCreateParams.Builder builder = SessionCreateParams.builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl("https://localhost:3000/payment-sucsess/" + orderId);
        builder.setCancelUrl("https://localhost:3000/payment-cancel");
        builder.addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(amount * 100)
                        .setProductData(SessionCreateParams
                                .LineItem
                                .PriceData
                                .ProductData.builder().setName("salon appoinment booking ")
                                .build()
                        ).build()
                ).build()
        );
        SessionCreateParams params = builder.build();
         Session session = Session.create(params);

        return session.getUrl() ;
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder,
                                  String paymentId,
                                  String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient=new RazorpayClient(razorpayApiKey, razorpaySecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount=payment.get("amount");
                String status=payment.get("status");
                if(status.equals("captured")){

                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }return false;
            }
            else{
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;

            }
        }
        return false;

    }
}
