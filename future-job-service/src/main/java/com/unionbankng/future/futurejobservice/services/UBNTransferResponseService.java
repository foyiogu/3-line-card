package com.unionbankng.future.futurejobservice.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UBNTransferResponseService {

    Map<String, String> responses=new HashMap<>();
    String getResponseMessage(String responseCode, String responseMessage){

        responses.put("00","SUCCESSFUL");
        responses.put("03","INVALID_SENDER");
        responses.put("05","DO_NOT_HONOR");
        responses.put("06","DORMANT_ACCOUNT");
        responses.put("07","INVALID_ACCOUNT");
        responses.put("08","ACCOUNT_NAME_MISMATCH");
        responses.put("09","REQUEST_PROCESSING_IN_PROGRESS");
        responses.put("12","INVALID_TRANSACTION");
        responses.put("13","INVALID_AMOUNT");
        responses.put("14","INVALID_BATCH_NUMBER");
        responses.put("15","INVALID_SESSION_OR_RECORD_ID");
        responses.put("16","UNKNOWN_BANK_CODE");
        responses.put("17","INVALID_CHANNEL");
        responses.put("18","WRONG_METHOD_CALL");
        responses.put("21","NO_ACTION_TAKEN");
        responses.put("25","UNABLE_TO_LOCATE_RECORD");
        responses.put("26","DUPLICATE_RECORD");
        responses.put("30","FORMAT_ERROR");
        responses.put("34","SUSPECTED_FRAUD");
        responses.put("35","CONTACT_SENDING_BANK");
        responses.put("51","NO_SUFFICIENT_FUNDS");
        responses.put("57","TRANSACTION_NOT_PERMITTED_TO_SENDER");
        responses.put("58","TRANSACTION_NOT_PERMITTED_ON_CHANNEL");
        responses.put("61","TRANSFER_LIMIT_EXCEEDED");
        responses.put("63","SECURITY_VIOLATION");
        responses.put("65","EXCEEDS_WITHDRAWAL_FREQUENCY");
        responses.put("68","RESPONSE_RECEIVED_TOO_LATE");
        responses.put("91","BENEFICIARY_BANK_NOT_AVAILABLE");
        responses.put("92","ROUTING_ERROR");
        responses.put("94","DUPLICATE_TRANSACTION");
        responses.put("96","SYSTEM_MALFUNCTION");
        responses.put("97","TIMEOUT_WAITING_FOR_RESPONSE_FROM_DESTINATION");
        responses.put("99","REPEAT_REQUEST");

        String message =responseMessage==null?"Unknown Error":responseMessage;
        String foundMessage=responses.get(responseCode);
        if(foundMessage!=null)
            message=foundMessage.replaceAll("_"," ");

        return  message;
    }
}
