package com.Salone.service.service;


import com.Salone.service.payload.dto.*;
import com.Salone.service.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloackService {

    private static final String KEYCLOCK_BASE_URL = "https://localhost:8080";
    private static final String KEYCLOCK_ADMIN_API=KEYCLOCK_BASE_URL+"/admin/realms/master/users";

    private static final String TOKEN_URL= KEYCLOCK_BASE_URL+"/realms/master/protocol/openid-connect/token";

    private static final String CLIENT_ID="salon-booking-client";
    private static final String CLIENT_SECRET="D1ZTMX8hcpSCeplKEub2JtHUd0L57QtbngtN4KDY06OBvAP26jrmtib3XjYIk9x5k9FCL2f5APFWsACWtAHo5F";
    private static final String  GRANT_TYPE="password";
    private static final String  scope="openid profile email";
    private static final String username="daily";
    private static final String password="daily";
    private static final String clientId="ad5288be-5c64-4647-b970-ff16e20a44b4";


    private final RestTemplate restTemplate;

    public void createUser(SignupDTO signupDTO ) throws Exception{
             String ACCESS_TOKEN=getAdminAccessToken(username,password,
                     GRANT_TYPE,
                     null
                     ).getAccessToken();

        Credential credential=new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDTO.getPassword());


        UserRequest userRequest=new UserRequest();
        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setEmail(signupDTO.getEmail());
        userRequest.setEnabled(true);
        userRequest.setLastName(signupDTO.getLastName());
        userRequest.setFirstName(signupDTO.getFirstName());



        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);


        HttpEntity<UserRequest> requestEntity=new HttpEntity<>(userRequest,headers);

        ResponseEntity<String> response=restTemplate.exchange(
                KEYCLOCK_ADMIN_API,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if(response.getStatusCode()==HttpStatus.CREATED){
            System.out.println("User created successfully");
            KeycloakUserDTO user=fetchFirstUserByUsername(signupDTO.getUsername(),ACCESS_TOKEN);
            KeyclockRole role=getRoleByName(clientId,ACCESS_TOKEN,signupDTO.get)
            assignRole(user.getId() ,
                    clientId,
                    roles,
                    ACCESS_TOKEN);
        }
    }

    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken

                                             ){
        return new TokenResponse();
    }

    public KeyclockRole getRoleByName(String clientId,
                                      String token,
                                      String role){
        return null;
    }
    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token){
        return null;
    }

    public void assignRole( String userId,String clientId,
                            List<KeyclockRole> roles ,
                            String token
                            ){
    }


}
