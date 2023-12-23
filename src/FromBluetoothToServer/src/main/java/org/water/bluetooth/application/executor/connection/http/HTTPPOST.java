package org.water.bluetooth.application.executor.connection.http;

import org.water.bluetooth.application.utils.Logging;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This class represents an HTTP POST request thread that sends data received from a remote device via Bluetooth
 * communication to a server using the specified data type.
 */
public class HTTPPOST extends Thread {

    /**
     * The data coming from the remote device via Bluetooth communication and to be used for the HTTP POST request to
     * the server.
     */
    private String data;

    /**
     * Type of data to be used for the HTTP POST request to the server.
     */
    private DataType dataType;

    /**
     * The endpoint for the HTTP POST request.
     */
    private String endPoint;

    /**
     * The complete URL of the endpoint.
     */
    private String endPointURL;

    /**
     * The URL object representing the complete endpoint URL.
     */
    private URL url;

    /**
     * The HttpsURLConnection object for establishing the connection.
     */
    private HttpsURLConnection httpsURLConnection;

    /**
     * The HTTP response code received from the server.
     */
    private int responseCode;


    /**
     * Constructs an HTTPPOST object with the specified data and data type.
     *
     * @param data     The data coming from the remote device via Bluetooth communication and to be used for the HTTP
     *                 POST request to the server.
     * @param dataType Type of data to be used for the HTTP POST request to the server.
     * @throws IllegalArgumentException If the data type is undefined.
     * @throws MalformedURLException    If the URL is malformed.
     * @throws IOException              If an I/O error occurs.
     */
    public HTTPPOST(String data, DataType dataType) throws
            IllegalArgumentException,
            MalformedURLException,
            IOException {
        this.data = data;
        this.dataType = dataType;
        endPoint = getEndPoint(dataType);
        endPointURL = getEndPointURL(endPoint);
        url = new URL(endPointURL);
        httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setDoOutput(true);
    }

    /**
     * Executes the HTTP POST request to the server with the provided data and data type.
     */
    @Override
    public void run() {

        try {

            try (DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream())) {
                byte[] postDataBytes = data.getBytes(StandardCharsets.UTF_8);
                dataOutputStream.write(postDataBytes);
                dataOutputStream.flush();
            }

            responseCode = httpsURLConnection.getResponseCode();
            Logging.msg("HTTP POST Response Code: " + responseCode);

            // You can read the response from the server if needed:
            // InputStream inputStream = connection.getInputStream();
            // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // String line;
            // while ((line = reader.readLine()) != null) {
            //     Logging.msg(line);
            // }

            httpsURLConnection.disconnect();

        } catch (MalformedURLException e) {
            Logging.msg(
                    "Malformed URL."
            );
        } catch (IOException e) {
            Logging.msg(
                    "Error during HTTP POST request to the server."
            );
        }

    }

    /**
     * Returns the endpoint corresponding to the specified data type for the HTTP POST request.
     *
     * @param dataType Type of data to be used for the HTTP POST request to the server.
     * @return Endpoint relating to the HTTP POST request corresponding to the dataType data.
     * @throws IllegalArgumentException If the provided data type is undefined.
     */
    private String getEndPoint(DataType dataType) throws IllegalArgumentException {

        if (dataType == DataType.TEMPERATURE) {
            return "temperature";
        } else if (dataType == DataType.DISSOLVED_METALS) {
            return "dissolved-metals";
        } else if (dataType == DataType.PH) {
            return "ph";
        } else {
            throw new IllegalArgumentException("Undefined DataType.");
        }

    }

    /**
     * Constructs and returns the complete URL for the given endpoint.
     *
     * @param endPoint The endpoint for the HTTP POST request.
     * @return The complete URL for the given endpoint.
     */
    private String getEndPointURL(String endPoint) {

        return "https://example.com/api/" + endPoint;
        
    }

}
