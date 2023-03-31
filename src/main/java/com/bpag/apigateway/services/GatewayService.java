package com.bpag.apigateway.services;

import com.bpag.apigateway.security.configurations.GatewayBasicResponseConfiguration;
import com.bpag.apigateway.web.dtos.responses.BaseResponse;
import com.fasterxml.jackson.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GatewayService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GatewayBasicResponseConfiguration baseResponse;

    @RabbitListener(queues = "queue.users_responses")
    public void createUserResponse(String userCreatedResponse) throws IOException {
        BaseResponse payload = baseResponse.baseResponse(userCreatedResponse);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(), "/private/users", payload);
    }

    @RabbitListener(queues = "queue.reservation_responses")
    public void createReservationResponse(String reservationCreatedResponse) throws IOException {
        BaseResponse payload = baseResponse.baseResponse(reservationCreatedResponse);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(), "/private", payload);
    }

    @RabbitListener(queues = "queue.reservation_list_response")
    public void yaporfavorListReservationsOfTheDay(String pay) throws JsonProcessingException {
        BaseResponse payload = baseResponse.baseResponse(pay);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(), "/reservations", payload);
    }

    @RabbitListener(queues = "queue.wallet_response")
    public void walletResponse(String walletResponse) throws IOException {
        BaseResponse payload = baseResponse.baseResponse(walletResponse);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(),"/private", payload);
    }

    @RabbitListener(queues = "queue.wallet_esp_response")
    public void walletCreateResponse(String payloadString) throws JsonProcessingException {
        BaseResponse payload = baseResponse.baseResponse(payloadString);
        simpMessagingTemplate.convertAndSend("/esp32", payload);
    }

    @RabbitListener(queues = "queue.wallet_cash_response")
    public void cashResponse(String payloadString) throws JsonProcessingException {
        BaseResponse payload = baseResponse.baseResponse(payloadString);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(),"/cash", payload);
    }

    @RabbitListener(queues = "queue.get_slots_response")
    public void getSlotResponse(String getSlotResponse) throws IOException {
        BaseResponse payload = baseResponse.baseResponse(getSlotResponse);
        simpMessagingTemplate.convertAndSendToUser(payload.getSessionId(), "/private", payload);
    }

}