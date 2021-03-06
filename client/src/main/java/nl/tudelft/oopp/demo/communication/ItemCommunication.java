package nl.tudelft.oopp.demo.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Equipment;
import nl.tudelft.oopp.demo.entities.Item;

public class ItemCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a list of items from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static List<Item> getItems() {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/item/all")).setHeader("Cookie", Authenticator.SESSION_COOKIE).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Item> items = null;
        // TODO handle exception
        try {
            items = mapper.readValue(response.body(), new TypeReference<List<Item>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Get an item by it's name.
     *
     * @param name - the name of the item
     * @return an item with the given namme
     */
    public static Item getItemByName(String name) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(String.format("http://localhost:8080/item/name/%s", name))).setHeader("Cookie", Authenticator.SESSION_COOKIE).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Item item = null;
        // TODO handle exception
        try {
            item = mapper.readValue(response.body(), new TypeReference<Item>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return item;
    }

    /**
     * Adds an item.
     * @param name - the name of the item
     */
    public static String addItem(String name) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Item newItem = new Item(name);
        String jsonItem = "";
        try {
            jsonItem = mapper.writeValueAsString(newItem);
            System.out.println(jsonItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonItem)).uri(URI.create("http://localhost:8080/item/add")).setHeader("Cookie", Authenticator.SESSION_COOKIE).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return "Communication with server failed";
        }
        if (response.statusCode() != 201) {
            System.out.println("Status: " + response.statusCode());
            return "The item \"" + name + "\" already exists.";
        }
        return "Successful";
    }

    /**
     * Updates a Item.
     * @throws Exception if communication with the server fails or if the response is not proper json.
     */
    public static String updateItem(long id, String name) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Item newItem = new Item(name);
        String jsonItem = "";
        try {
            jsonItem = mapper.writeValueAsString(newItem);
            System.out.println(jsonItem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder().header("Content-type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(jsonItem)).uri(URI.create(String.format("http://localhost:8080/item/update/%s", id))).setHeader("Cookie", Authenticator.SESSION_COOKIE).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return "The item \"" + name + "\" already exists.";
        }
        return "Successful";
    }

    /**
     * Removes an item from the database.
     *
     * @param id - the id of the item that will be deleted.
     */
    public static void removeItem(long id) {
        List<Equipment> equipmentList = EquipmentCommunication.getEquipmentByItem(id);
        if (equipmentList != null) {
            for (Equipment e : equipmentList) {
                EquipmentCommunication.removeEquipment(e.getId());
            }
        }

        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(String.format("http://localhost:8080/item/delete/%s", id))).setHeader("Cookie", Authenticator.SESSION_COOKIE).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }
}
