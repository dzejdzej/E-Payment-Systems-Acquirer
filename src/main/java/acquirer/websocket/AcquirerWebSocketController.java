package acquirer.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import acquirer.rest.CompletePaymentResponseDTO;

@Service
public class AcquirerWebSocketController {
	
		
	 	@Autowired
	    private SimpMessagingTemplate wsMessageTemplate;
	 

	 // @Scheduled(fixedRate = 5000)
	  public void sendPaymentResponse(CompletePaymentResponseDTO dto) {
		  wsMessageTemplate.convertAndSend("/topic/complete-payment", dto);
	  }
	  
	
	@MessageMapping("/acquirer-ws")
	@SendTo("/topic/complete-payment")
	public CompletePaymentResponseDTO wsEndPoint(CompletePaymentResponseDTO dto) {
		return dto;
		
	}
	

}
