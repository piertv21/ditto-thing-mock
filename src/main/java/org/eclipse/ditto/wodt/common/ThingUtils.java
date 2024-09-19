package org.eclipse.ditto.wodt.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.eclipse.ditto.client.DittoClient;
import org.eclipse.ditto.things.model.Thing;
import org.eclipse.ditto.things.model.ThingId;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThingUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ThingUtils.class);

    private static final String AMBULANCE_THING_ID = "io.eclipseprojects.ditto:ambulance";
    private static final String AMBULANCE_THING_MODEL = "https://gist.githubusercontent.com/piertv21/6e71cdceba5463f28b6b1352fff56471/raw/df781b4b4f577584391d8435b0777a931402109e/ambulance-1.0.0.tm.jsonld";
    
    private static final String TRAFFIC_LIGHT_THING_ID = "io.eclipseprojects.ditto:trafficlight";
    private static final String TRAFFIC_LIGHT_THING_MODEL = "https://gist.githubusercontent.com/piertv21/3b11deb764b62c2775dd9a5a4303fe08/raw/1b27ce5dba3f4a5b2553b7ffc8ae2dbe9352891c/trafficlight-1.0.0.tm.jsonld";    
    
    private static final String DITTO_BASE_URL = "http://localhost:8080/api/2/things/";
    private static final String BASIC_AUTH = "ditto:ditto";
    
    private static final DittoClient client = new DittoBase().getClient();

    public ThingUtils() {}

    public void onStart() {
        this.checkAndCreateThing(AMBULANCE_THING_ID, AMBULANCE_THING_MODEL, "Ambulance",
        () -> {
            this.editAttribute(AMBULANCE_THING_ID, "rel-is_approaching", "http://localhost:3003/");
            this.editAttribute(AMBULANCE_THING_ID, "busy", false);
            this.editAttribute(AMBULANCE_THING_ID, "fuelLevel", 21.0);
            LOGGER.info("Initial state for the Ambulance was set.");
        });
        this.checkAndCreateThing(TRAFFIC_LIGHT_THING_ID, TRAFFIC_LIGHT_THING_MODEL, "Traffic light",
        () -> {
            this.editAttribute(TRAFFIC_LIGHT_THING_ID, "is-on", false);
            LOGGER.info("Initial state for the Traffic light was set.");
        });
    }

    public void simulatePAChanges() {
        double randomFuelLevel = Math.round(Math.random() * 1000) / 10.0;
        this.editAttribute(AMBULANCE_THING_ID, "fuelLevel", randomFuelLevel);
        LOGGER.info("Sent new Fuel level for the Ambulance: " + randomFuelLevel);
        
        boolean isFuelLevelEven = ((int) randomFuelLevel % 2 == 0);
        this.editAttribute(TRAFFIC_LIGHT_THING_ID, "is-on", isFuelLevelEven);
        LOGGER.info("Sent new Traffic light state: " + isFuelLevelEven);
    }

    private void editAttribute(String thingId, String attributeName, Object attributeValue) {
        try {
            if (attributeValue instanceof String) {
                client.twin().forId(ThingId.of(thingId))
                    .putAttribute(attributeName, (String) attributeValue)
                    .toCompletableFuture().join();
            } else if (attributeValue instanceof Integer) {
                client.twin().forId(ThingId.of(thingId))
                    .putAttribute(attributeName, (Integer) attributeValue)
                    .toCompletableFuture().join();
            } else if (attributeValue instanceof Long) {
                client.twin().forId(ThingId.of(thingId))
                    .putAttribute(attributeName, (Long) attributeValue)
                    .toCompletableFuture().join();
            } else if (attributeValue instanceof Boolean) {
                client.twin().forId(ThingId.of(thingId))
                    .putAttribute(attributeName, (Boolean) attributeValue)
                    .toCompletableFuture().join();
            } else if (attributeValue instanceof Double) {
                client.twin().forId(ThingId.of(thingId))
                    .putAttribute(attributeName, (Double) attributeValue)
                    .toCompletableFuture().join();
            } else {
                throw new IllegalArgumentException("Unsupported attribute value type");
            }
        } catch (IllegalArgumentException e) {
        }
    }    
    
    private void checkAndCreateThing(String thingId, String thingModel, String thingName, Runnable onCreationAction) {
        if (checkThingExists(thingId)) {
            LOGGER.info(thingName + " thing already exists, skipping creation...");
        } else {
            createThing(thingId, thingModel);
            LOGGER.info(thingName + " does not exist and has been created!");
            onCreationAction.run();
        }
    }

    private boolean checkThingExists(String thingId) {
        try {
            Thing thing = client.twin().forId(ThingId.of(thingId))
                .retrieve().toCompletableFuture().join();
            return thing != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean createThing(String thingId, String thingModel) {
        try {
            String jsonBody = "{\"definition\": \"" + thingModel + "\"}";
            String encodedAuth = Base64.getEncoder()
                .encodeToString(BASIC_AUTH.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DITTO_BASE_URL + thingId))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + encodedAuth)
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == HttpStatus.CREATED_201;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
