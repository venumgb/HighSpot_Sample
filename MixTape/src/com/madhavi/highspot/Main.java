package com.madhavi.highspot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                Main instance = new Main();
                JSONObject mixtape = instance.readInputFile(args[0]);
                JSONObject changes = instance.readInputFile(args[1]);
                updateMixTape(changes, mixtape);
                WriteToOutputFile(mixtape);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*This function updates the input file mixtape.json with the changes.json file changes based on the
        operation mentioned in the file..It prints the updated json object
    */
    public static void updateMixTape(JSONObject changes, JSONObject mixtape) {

        JSONArray playlists = (JSONArray) mixtape.get("playlists");

        JSONObject playlistChanges = (JSONObject) changes.get("playlists");

        playlistChanges.keySet().forEach(keyStr -> {
            JSONArray inputData = (JSONArray) playlistChanges.get(keyStr);

            //if the operation is Add, then assume the playlist
            // does not exists and add the new playlist
            switch( keyStr.toString() ) {
                case "Add":
                {
                    System.out.println("Adding new playlists:");
                    if (!inputData.isEmpty()) {
                        for (Object obj : inputData) {
                            JSONObject jsonObject = (JSONObject) obj;
                            playlists.add(jsonObject);
                            System.out.println("Added the new playlist: " + jsonObject.toJSONString());
                        }
                    }
                    break;
                }
                //if the operation is Remove, then check if the playlist exists
                // and remove that playlist
                case "Remove": {
                    System.out.println("Removing playlist:");
                    for (Object obj : inputData) {
                        JSONObject jsonObject = (JSONObject) obj;
                        if (playlists.contains(jsonObject)) {
                            playlists.remove(jsonObject);
                        } else {
                            System.out.println("Playlist does not exist: " + jsonObject.toJSONString());
                            break;
                        }
                        System.out.println("Removed playlist from the Playlist: " + jsonObject.toJSONString());
                        break;
                    }
                }
                //if the operation is Update, then check for the existence
                // of the playlist and if found ,update that playlist
                case "Update": {
                    System.out.println("Updating playlist:");
                    for (Object obj : inputData) {
                        JSONObject jsonObject = (JSONObject) obj;
                        int id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                        for (Object o : playlists) {
                            JSONObject playListObj = (JSONObject) o;
                            //check if the playlist with the id specified exists
                            //if it does, then read the object and update the playlist with the songs
                            if (id == Integer.parseInt(playListObj.get("id").toString())) {
                                JSONArray songList = (JSONArray) jsonObject.get("song_ids");
                                JSONArray existingSongList = (JSONArray) playListObj.get("song_ids");
                                existingSongList.addAll(songList);
                                playListObj.put("song_ids", existingSongList);
                                System.out.println("Updated playlist from the Playlist: " + playListObj.toJSONString());
                            }
                        }
                    }
                    break;
                }
            }
        });

        //update the input json with the playlist changes
        mixtape.put("playlists", playlists);
        System.out.println("Output Json: ");
        printJsonObject(mixtape);
    }

    /*
     This function write to output json file
     */
    private static void WriteToOutputFile(JSONObject outputJson) {
        FileWriter outputFile = null;
        try {
            outputFile = new FileWriter("output.json");
            outputFile.write(outputJson.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                outputFile.flush();
                outputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Helper methods to print json
     */
    private static void printJsonArray(JSONArray array) {
        Iterator<JSONObject> objectIterator = array.iterator();
        while (objectIterator.hasNext()) {
            System.out.println(objectIterator.next());
        }
    }

    private static void printJsonObject(JSONObject jsonObj) {

        jsonObj.keySet().forEach(keyStr ->
        {
            Object keyvalue = jsonObj.get(keyStr);
            System.out.println("key: " + keyStr);

            //for nested objects
            if (keyvalue instanceof JSONObject)
                printJsonObject((JSONObject) keyvalue);

            else if (keyvalue instanceof JSONArray)
                printJsonArray((JSONArray) keyvalue);

        });
    }

    public JSONObject readInputFile(String fileName) throws FileNotFoundException {
        JSONParser parser = new JSONParser();

        String path = System.getProperty("user.dir");
        File inputJson = new File(path+ "/"+ fileName);
        JSONObject inputfile = null ;
        try {
            inputfile = (JSONObject) parser.parse(new FileReader(inputJson));

        } catch (IllegalArgumentException | ParseException | IOException ex) {
            ex.printStackTrace();
        }
        if (inputfile != null) {
            printJsonObject(inputfile);
        }
        return inputfile;

    }
}
