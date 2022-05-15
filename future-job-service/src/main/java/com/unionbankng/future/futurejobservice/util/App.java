package com.unionbankng.future.futurejobservice.util;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
    public String toString(Object obj){
        try {
            return new Gson().toJson(obj);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return  null;
        }
    }

    public void printX(Object obj){
        try {
            logger.info(new Gson().toJson(obj));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Object copy(Object obj){
        Gson gson= new Gson();
        String tmp = gson.toJson(obj);
        return gson.fromJson(tmp,obj.getClass());
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

    public ObjectMapper getMapper(){
        ObjectMapper mapper= new ObjectMapper();
        return mapper;
    }
    public String toPhoneNumber(String phoneNumber) {
        String userPhone = phoneNumber;
        if (phoneNumber.startsWith("234") || phoneNumber.startsWith("+234")) {
            if (phoneNumber.length() == 13) {
                String str_getMOBILE = phoneNumber.substring(3);
                userPhone = "0" + str_getMOBILE;
            } else if (phoneNumber.length() == 14) {
                String str_getMOBILE = phoneNumber.substring(4);
                userPhone = "0" + str_getMOBILE;
            }
        }
        return userPhone;
    }
    public String getClientDevice() {
        return "Google Chrome";
    }
    public String getClientMACAddress(){
        return "172.17.255.255";
    }
}
