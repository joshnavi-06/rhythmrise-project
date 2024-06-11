package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	
	@Autowired
	UsersService userv;
	
	@GetMapping("/payment")
	public String pay() {
		return "payment";
	}

	@SuppressWarnings("finally")
	
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {
		
		int amount = 5000;
		Order order = null;
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_dQHMCSeq8VCOF8", "xjDnccttAo8R0MAacgTybw3c");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount",amount*100); // amount in the smallest currency unit
			orderRequest.put("currency","INR");
			orderRequest.put("receipt", "order_rcptid_11");
			
			order = razorpay.orders.create(orderRequest);

			String mail =  (String) session.getAttribute("email");

			Users u = userv.getUser(mail);
			u.setPremium(true);
			userv.updateUser(u);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			return order.toString();
		}
			
	}
	
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam String orderId,@RequestParam String paymentId,@RequestParam String signature) {
		try {
			
			//Initialize RazorPay client with your API key & secret
			RazorpayClient razorpayClient = new RazorpayClient("rzp_test_dQHMCSeq8VCOF8", "xjDnccttAo8R0MAacgTybw3c");
			// Create a signature verification data string
			String verificationData = orderId+""+paymentId;
			// Use Razorpay's utility function to verify the signature
			boolean isValidSignature =  Utils.verifySignature(verificationData,signature,"xjDnccttAo8R0MAacgTybw3c");
			return isValidSignature;
		} catch (RazorpayException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Payment-success -> Update to premium user
	@GetMapping("Payment successful")
	public String paymentSuccess(HttpSession session) {
		
		String email = (String) session.getAttribute("email");
		Users user = userv.getUser(email);
		user.setPremium(true);
		userv.updateUser(user);
		return "login";
	}
}
