package com.example.demo.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.service.TaskService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Builder;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

@Service
@RequiredArgsConstructor
public class GoogleCalendarAPI {

    private static final String APPLICATION_NAME = "TaskTime";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final OAuth2AuthorizedClientService authorizedClientService;
//    OAuth2AuthenticationToken authenticationToken;

    // @Value("${security.oauth2.client.registration.google.client-id}")
    // private static String client_id;

    // @Value("${security.oauth2.client.registration.google.client-secret}")
    // private static String client_secret;
    
    
//    private OAuth2AuthorizedClient client = this.getAuthorizedClient(authenticationToken);
    

    private HttpRequestInitializer getCredentials(OAuth2AuthenticationToken authenticationToken) {
    	OAuth2AuthorizedClient client =
    			authorizedClientService.loadAuthorizedClient(
    					authenticationToken.getAuthorizedClientRegistrationId(),
    					authenticationToken.getName());
    	
    	OAuth2AccessToken oauth2AccessToken = client.getAccessToken();
    	
        AccessToken accessToken = new AccessToken(oauth2AccessToken.getTokenValue(), Date.from(oauth2AccessToken.getExpiresAt()));
        GoogleCredentials credentials = GoogleCredentials.create(accessToken);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        return requestInitializer;
    }

    //GoogleCalendar　イベント追加test
    public String addEventTest(OAuth2AuthenticationToken authenticationToken) throws GeneralSecurityException, IOException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Calendar service = new Calendar
            .Builder(HTTP_TRANSPORT, 
                        JSON_FACTORY, 
                        getCredentials(authenticationToken))
            .setApplicationName(APPLICATION_NAME)
            .build();

        EventDateTime startEventDateTime = new EventDateTime().setDateTime(new DateTime("2021-07-14T20:00:00+09:00")); // イベント開始日時
        EventDateTime endEventDateTime = new EventDateTime().setDateTime(new DateTime("2021-07-14T21:00:00+09:00")); // イベント終了日時

        String summary = "テスト";
        String description = "テスト";

        Event event = new Event()
            .setSummary(summary)
            .setDescription(description)
            .setColorId("2") // green
            .setStart(startEventDateTime)
            .setEnd(endEventDateTime);

        String gcal_id = "primary";

        event = service.events().insert(gcal_id, event).execute();

        return event.getId();
    }

}
