package org.water.bluetooth.application.executor.connection.http;

import org.water.bluetooth.application.utils.Logging;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * The HTTPPOST class represents an HTTP POST request thread that sends data received from a remote device via Bluetooth
 * communication to a server using the specified data type.
 *
 * <p>
 * This class extends the Thread class and is designed to be instantiated for each HTTP POST request to the server.
 * It includes methods for constructing and executing the HTTP POST request, handling the response, and determining the
 * appropriate endpoint based on the data type.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public class HTTPPOST extends Thread {
    private String url="http://localhost:8081/api/misuration";
    private String data;
    private String response;

    public HTTPPOST(String data){
        this.data=data;
        this.start();
    }
    public void run(){
        try {
        HttpClient httpClient= HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        System.out.println(data);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        this.response=response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getResponse(){
        return this.response;
    }

}
