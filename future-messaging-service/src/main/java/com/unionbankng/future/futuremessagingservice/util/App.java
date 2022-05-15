package com.unionbankng.future.futuremessagingservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.uuid.Generators;
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
    public boolean validImage(String fileName)
    {
        String regex = "(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)$";
        Pattern p = Pattern.compile(regex);
        if (fileName == null) {
            return false;
        }
        Matcher m = p.matcher(fileName);
        return m.matches();
    }

    public boolean validEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public boolean validNumber(String number) {
        if (number.startsWith("+234"))
           number= number.replace("+234", "0");
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
