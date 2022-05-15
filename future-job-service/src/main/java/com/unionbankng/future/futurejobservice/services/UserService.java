package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.authorizationserver.grpc.SidekiqUserDetailServiceGrpc;
import com.unionbankng.future.authorizationserver.grpc.UserDetailRequest;
import com.unionbankng.future.authorizationserver.grpc.UserDetailResponse;
import com.unionbankng.future.futurejobservice.controllers.JobController;
import com.unionbankng.future.futurejobservice.pojos.User;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @GrpcClient("authService")
    private SidekiqUserDetailServiceGrpc.SidekiqUserDetailServiceBlockingStub sidekiqUserDetailServiceStub;
    Logger logger = LoggerFactory.getLogger(JobController.class);

    public User getUserById(Long userId){
        try {
            UserDetailRequest request = UserDetailRequest.newBuilder().
                    setUserId(userId).build();
            UserDetailResponse response = sidekiqUserDetailServiceStub.getUserDetail(request);
            User user = new User();
            user.setId(response.getId());
            user.setUuid(response.getUuid());
            user.setUmid(response.getUmid());
            user.setFullName(response.getFullName());
            user.setCountry(response.getCountry());
            user.setAddress(response.getAddress());
            user.setEmail(response.getEmail());
            user.setPhoneNumber(response.getPhoneNumber());
            user.setStateOfResidence(response.getStateOfResidence());
            user.setUsername(response.getUsername());
            user.setImg(response.getImg());
            user.setAccountName(response.getAccountName());
            user.setAccountNumber(response.getAccountNumber());
            user.setIsEnabled(response.getIsEnabled());
            user.setCreatedAt(response.getCreatedAt());

            return user;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("An error occurred while connecting to  auth via GRPC");
            return  null;
        }
    }
}

