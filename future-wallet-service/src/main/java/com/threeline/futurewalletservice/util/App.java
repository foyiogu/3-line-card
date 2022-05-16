package com.threeline.futurewalletservice.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.threeline.futurewalletservice.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class App {

    private final Logger logger = LoggerFactory.getLogger(App.class);
    private final WalletRepository walletRepository;

    public void log(String message) {
        logger.info(message);
    }
    public void print(Object obj){
        try {
            ObjectMapper myObjectMapper= new ObjectMapper();
            myObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            logger.info(myObjectMapper.writeValueAsString(obj));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String makeUIID() {
        UUID referenceId = Generators.timeBasedGenerator().generate();
        return   referenceId.toString().replaceAll("-", "");

    }

    public String generateTransactionReference() {
        return   "TRN"+makeUIID();
    }

    public String generateAccountNumber() {
        String accountNo = ("31" + System.currentTimeMillis());
        print(accountNo);
        while(walletRepository.existsByAccountNumber(accountNo))
            accountNo = ("31" + System.currentTimeMillis());
        print("Account number found");
        return accountNo;
    }

    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }


}
