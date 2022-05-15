package com.unionbankng.future.futurejobservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionbankng.future.futurejobservice.entities.*;
import com.unionbankng.future.futurejobservice.enums.*;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.repositories.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatFileUploadService {

    private final AppService appService;
    private final ChatFileUploadRepository repository;
    private final FileStoreService fileStoreService;
    private Logger logger = LoggerFactory.getLogger(ChatFileUploadService.class);


    public APIResponse uploadChatFiles(String details, MultipartFile[] files) throws IOException {
        try {
            String chat_file_names = null;
            ChatFile[] chatFiles = new ObjectMapper().readValue(details, ChatFile[].class);
            List<ChatFile> uploadedFiles=new ArrayList<>();
            //save files if not null
            if (files != null)
                chat_file_names = this.fileStoreService.storeFiles(files, "chat-file");

            if (chat_file_names != null) {
                String[] urlBase = chat_file_names.split(",");

                for (int i = 0; i < chatFiles.length; i++) {
                    if (urlBase[i] != null) {
                        chatFiles[i].setLink(urlBase[i]);
                        chatFiles[i].setStatus(ChatFileUploadStatus.Uploaded);
                        chatFiles[i].setCreatedAt(new Date());
                        uploadedFiles.add(chatFiles[i]);
                    }
                }
            }
            if(uploadedFiles.stream().count()>0) {
                List<ChatFile> uploadResponse = repository.saveAll(uploadedFiles);
                if (uploadResponse != null)
                    return new APIResponse("Uploaded", true, uploadResponse);
                else
                    return new APIResponse("Unable to upload file", false, null);
            }else{
                return new APIResponse("No File Found to Upload", false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse("Unable to upload file", false, null);
        }
    }
}
