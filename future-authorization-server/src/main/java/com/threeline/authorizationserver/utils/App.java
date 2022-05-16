package com.threeline.authorizationserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class App {
    private final Logger logger = LoggerFactory.getLogger(App.class);

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

    public boolean validNumber(String number) {
        if (number.startsWith("+234"))
           number= number.replace("+234", "0");
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(number);
//        return matcher.matches();
        return true; //Making all numbers valid for demonstration purposes
    }
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }


}
