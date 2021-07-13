package com.example.demo.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

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
    private static final String SERVICE_CREDENTIALS_FILE_PATH = "/Users/nakamurakeigo/Documents/workspace-spring-tool-suite-4-4.10.0.RELEASE/TaskTime/client_secret_705618906495-6m33h9ak3ld3odk81povkie6r4jqn6i4.apps.googleusercontent.com.json";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	private final OAuth2AuthorizedClientService authorizedClientService;
	
	@Value("${security.oauth2.client.registration.google.client-id}")
	private static String client_id;
	
	@Value("${security.oauth2.client.registration.google.client-secret}")
	private static String client_secret;
	
	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
		return this.authorizedClientService.loadAuthorizedClient(
			authentication.getAuthorizedClientRegistrationId(), authentication.getName()
			);
	}
	

    private HttpRequestInitializer getCredentials(final NetHttpTransport HTTP_TRANSPORT, OAuth2AuthenticationToken authentication) throws IOException {

//    	AccessToken accessToken = this.getAuthorizedClient(authentication).getAccessToken();
//    	String refreshToken = this.getAuthorizedClient(authentication).getRefreshToken().toString();
    			
		Credentials credentials = 
				GoogleCredentials.fromStream(new FileInputStream(SERVICE_CREDENTIALS_FILE_PATH))
			    .createScoped(Collections.singleton(CalendarScopes.CALENDAR_EVENTS));
//				new GoogleCredential.Builder()
//    	         .setTransport(HTTP_TRANSPORT) 
//    	         .setJsonFactory(JSON_FACTORY) 
//    	         .setClientSecrets(client_id, client_secret) 
//    	         .build() 
//    	         .setAccessToken(accessToken) 
//    	         .setRefreshToken(refreshToken); 
		
		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
		
        return requestInitializer;
    }
    
    
    
    public String addEvent(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authentication) throws GeneralSecurityException, IOException {
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, authentication))
        .setApplicationName(APPLICATION_NAME)
        .build();
        
        EventDateTime startEventDateTime = new EventDateTime().setDateTime(new DateTime("2021-07-13T20:00:00+09:00")); // イベント開始日時
        EventDateTime endEventDateTime = new EventDateTime().setDateTime(new DateTime("2021-07-13T21:00:00+09:00")); // イベント終了日時
    	
        String summary = "テスト";
        String description = "テスト";
        
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setColorId("2") // green
                .setStart(startEventDateTime)
                .setEnd(endEventDateTime);
        
//        String gcal_id = (String) oauth2User.getAttributes().get("email");
        String gcal_id = "primary";

        event = service.events().insert(gcal_id, event).execute();
        
        return event.getId();
    }
    
}
