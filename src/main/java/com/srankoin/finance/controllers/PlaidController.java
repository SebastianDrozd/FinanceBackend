package com.srankoin.finance.controllers;

import com.srankoin.finance.dto.CreateLinkTokenRequestDto;
import com.srankoin.finance.dto.PublicTokenDto;
import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.repositories.UserClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;
import com.plaid.client.model.Products;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")
public class PlaidController {

    @Autowired
    UserClassRepository userClassRepository;

    @PostMapping("/api/plaid/create-link-token")
    public ResponseEntity<?> createLinkToken(@RequestBody CreateLinkTokenRequestDto linkToken) throws IOException {
            final String CLIENT_ID ="61b7d4adc7d8e5001b30327b";
            final String SECRET = "bbe613d8817bb6affe9b2c52f966d1";
             HashMap<String, String> apiKeys = new HashMap<String, String>();
             apiKeys.put("clientId", CLIENT_ID);
             apiKeys.put("secret", SECRET);
            ApiClient apiClient = new ApiClient(apiKeys);
              apiClient.setPlaidAdapter(ApiClient.Development);
        PlaidApi plaidClient = apiClient.createService(PlaidApi.class);
        UserClass userModel = userClassRepository.findByUsername(linkToken.getUsername()).get();
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
             .clientUserId(String.valueOf(userModel.getId()));
        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
               .user(user)
                .clientName("Plaid Test App")
               .products(Arrays.asList(Products.fromValue("auth")))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en")
               .webhook("http://localhost:3000");
        Response<LinkTokenCreateResponse> response = plaidClient
               .linkTokenCreate(request)
               .execute();
        return ResponseEntity.ok(response.body());
    }
    @PostMapping("/api/plaid/set-access-token")
    public ResponseEntity<?> setAccessToken(@RequestBody PublicTokenDto publicTokenDto){
      UserClass userClass = userClassRepository.findByUsername(publicTokenDto.getUsername()).get();
      userClass.setPublicAccessToken(publicTokenDto.getToken());
      userClassRepository.save(userClass);
        return ResponseEntity.ok(userClass);
    }

}
