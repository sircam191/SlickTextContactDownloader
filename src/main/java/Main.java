import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class Main {

    private static final String APPLICATION_NAME = "slicktext1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static void main (String[] args) throws GeneralSecurityException, IOException {


        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "*********************";

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();


        Unirest.config().setDefaultBasicAuth("pub_************", "*****************************");

        final HttpResponse<JsonObject> response = Unirest
                .get("https://api.slicktext.com/v1/contacts")
                .queryString("limit", 10000)
                .asObject(JsonObject.class);

        System.out.println(response.getBody().toString());

        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("First Name", "Last Name", "Number", "City", "Subscribed Date", "Opt-In Method")
                ));

        AppendValuesResponse appendResult = service.spreadsheets().values()
                .append(spreadsheetId, "Sheet1!1:2", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

        for(int i = response.getBody().getAsJsonArray("contacts").size() - 1; i >= 0; i--) {

            String number = "null";
            String fName = "null";
            String lName = "null";
            String city = "null";
            String subscribedDate = "null";
            String optInMethod = "null";

            if(response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().has("firstName")) {
                fName = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("firstName").toString();
            }

            if(response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().has("lastName")) {
                lName = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("lastName").toString();
            }


            if(response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().has("number")) {
                number = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("number").toString();
            }

            if(response.getBody().getAsJsonArray("contacts").get(0).getAsJsonObject().has("city")) {
                city = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("city").toString();
            }

            if(response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().has("subscribedDate")) {
                subscribedDate = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("subscribedDate").toString();
            }

            if(response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().has("optInMethod")) {
                optInMethod = response.getBody().getAsJsonArray("contacts").get(i).getAsJsonObject().get("optInMethod").toString();
            }

            ValueRange appendBodyValues = new ValueRange()
                    .setValues(Arrays.asList(
                            Arrays.asList(fName, lName, number, city, subscribedDate, optInMethod)
                    ));

            AppendValuesResponse appendResultvalues = service.spreadsheets().values()
                    .append(spreadsheetId, "Sheet1", appendBodyValues)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .setIncludeValuesInResponse(true)
                    .execute();




        }




    }
    }