package com.Salone.service.service;


import com.Salone.service.payload.dto.*;
import com.Salone.service.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        userRequest.setLastName(signupDTO.getFullName());
        userRequest.getCredential().add(credential);




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
            KeyclockRole role=getRoleByName(clientId,ACCESS_TOKEN,signupDTO.getRole().toString());

            List<KeyclockRole> roles=new ArrayList<>();
            roles.add(role);




            assignRole(user.getId() ,
                    clientId,
                    roles,
                    ACCESS_TOKEN);
        }
        else{
            System.out.println("User creation failed");
            throw  new Exception(response.getBody());
        }
    }

    public TokenResponse getAdminAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken

                                             ) throws Exception {





        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        MultiValueMap<String, String> requestBody=new LinkedMultiValueMap<>();
        requestBody.add("grant_type",GRANT_TYPE);
        requestBody.add("username",username);
        requestBody.add("password",password);
        requestBody.add("refresh_token",refreshToken);
        requestBody.add("client_id",CLIENT_ID);
        requestBody.add("client_secret",CLIENT_SECRET);
        requestBody.add("scope",scope);



        HttpEntity<MultiValueMap<String,String>> requestEntity=
                new HttpEntity<>(requestBody,headers);


        ResponseEntity<TokenResponse> response=restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                TokenResponse.class
        );

        if(response.getStatusCode()==HttpStatus.OK&& response.getBody()!=null){
            return  response.getBody();
        }
        throw new Exception("Failed to obtain token");


    }

    public KeyclockRole getRoleByName(String clientId,
                                      String token,
                                      String role) {

        String url=KEYCLOCK_BASE_URL+"/admin/realms/master/clients/"+clientId+"/roles/"+role;

        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);




        HttpEntity<Void> requestEntity=
                new HttpEntity<>(headers);


        ResponseEntity<KeyclockRole> response=restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                KeyclockRole.class
        );
        return  response.getBody();




    }
    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) throws Exception {

        String url=KEYCLOCK_BASE_URL+"/admin/realms/master/users?username="+username;

        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);




        HttpEntity<String> requestEntity=
                new HttpEntity<>(headers);


        ResponseEntity<KeycloakUserDTO[]> response=restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                KeycloakUserDTO[].class
        );
        KeycloakUserDTO[] users=response.getBody();
        if(users.length>0&&users!=null){
            return users[0];

        }
       throw new Exception("user not found with username "+username);

    }

    public void assignRole( String userId,String clientId,
                            List<KeyclockRole> roles ,
                            String token
                            ) throws Exception {

        String url=KEYCLOCK_BASE_URL+"/admin/realms/master/users/"+userId+"/role-mappings/clients/"+clientId;

        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);




        HttpEntity<List<KeyclockRole>> requestEntity=
                new HttpEntity<>(roles,headers);



        try{
            ResponseEntity<String> response=restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

        }catch (Exception e){
            throw new Exception("Failed to assign new role"+e.getMessage());
        }

    }


    public KeycloakUserDTO fetchUserProfileByjwt( String  token
    ) throws Exception {

        String url=KEYCLOCK_BASE_URL+"/realms/master/users/protocol/openid-connect/userinfo";

        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization",token);
        headers.setContentType(MediaType.APPLICATION_JSON);




        HttpEntity<String> requestEntity=
                new HttpEntity<>(headers);



        try{
            ResponseEntity<KeycloakUserDTO> response=restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    KeycloakUserDTO.class
            );
            return response.getBody();
        }catch (Exception e){
            throw new Exception("Failed to get user"+e.getMessage());
        }

    }


}
