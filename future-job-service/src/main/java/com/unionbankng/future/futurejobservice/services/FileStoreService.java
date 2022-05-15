package com.unionbankng.future.futurejobservice.services;
import com.google.protobuf.ByteString;
import com.unionbankng.future.futureutilityservice.grpcserver.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class FileStoreService {
    Logger logger = LoggerFactory.getLogger(FileStoreService.class);
    @GrpcClient("blobStorageService")
    private  BlobStorageServiceGrpc.BlobStorageServiceBlockingStub blobStorageServiceBlockingStub;

    public String storeFiles(MultipartFile[] files, String prefix){
        if (files.length > 0) {
            StringBuilder fileNames = new StringBuilder();
            Arrays.asList(files).stream().forEach(file -> {
                String name = null;
                try {
                    name = this.storeFile(file, prefix, BlobType.IMAGE);
                    fileNames.append(name).append(",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fileNames.deleteCharAt(fileNames.length() - 1).toString();
            return fileNames.toString();
        }
        return  null;
    }

    public String storeFile(MultipartFile file, String prefix,BlobType blobType) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String fileName = prefix + "_" + System.currentTimeMillis() + fileExtension;

        StorageUploadRequest storageUploadRequest = StorageUploadRequest.newBuilder().setFileName(fileName)
                .setBlobType(blobType).setFileByte(ByteString.copyFrom(file.getBytes())).build();

        StorageUploadResponse response = blobStorageServiceBlockingStub.upload(storageUploadRequest);

        return response.getUrl();

    }

    public int deleteFileFromStorage(String source,BlobType blobType) {
        String originalFileName = source.substring(source.lastIndexOf("/") + 1);
        logger.info("Delete from Blob storage where blob client : {}",originalFileName);
        StorageDeleteRequest storageDeleteRequest = StorageDeleteRequest.newBuilder().setIdentifier(originalFileName)
                .setBlobType(blobType).build();

        StorageDeleteResponse response = blobStorageServiceBlockingStub.delete(storageDeleteRequest);

        return response.getCode();

    }
}

