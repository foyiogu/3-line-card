package com.unionbankng.future.futurejobservice.controllers;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.services.ChatFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class ChatFileController {

    private final ChatFileUploadService chatFileUploadService;

    @PostMapping(value="/v1/upload/chat_file", consumes="multipart/form-data")
    public ResponseEntity<APIResponse> uploadFile(@Valid @RequestParam(value = "details") String details,
                                              @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        return ResponseEntity.ok().body(chatFileUploadService.uploadChatFiles(details,files));
    }

}
