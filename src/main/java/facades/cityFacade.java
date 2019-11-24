/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.travelsDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author shpattt
 */
public class cityFacade {
    private static ExecutorService executor = Executors.newFixedThreadPool(14);
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static cityFacade instance;

    private cityFacade() {
    
    }

    public static cityFacade getCityFacade() {
        if (instance == null) {

            instance = new cityFacade();
        }
        return instance;
    }

    
    
    
    public String getTravelData() throws MalformedURLException, IOException{
        URL url = new URL("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query=Copenhagen" );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("x-rapidapi-host","skyscanner-skyscanner-flight-search-v1.p.rapidapi.com");
        con.setRequestProperty("x-rapidapi-key","04df80bf11msh07708f02a1040d1p1d7092jsn6ec6decfc602");
        //con.setRequestProperty("User-Agent", "server"); //remember if you are using SWAPI
        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
          jsonStr = scan.nextLine();
        }
        scan.close();
        return jsonStr;
      }

    public List<travelsDTO> getAll() throws InterruptedException, ExecutionException{
        List<travelsDTO> travels = new ArrayList<>();
        
        Queue<Future<travelsDTO>> queue = new ArrayBlockingQueue(14);
       
       // List<Future<String>> futures = new ArrayList();
        
        for (int i = 1; i <= 14; i++) {
            //final int count = i;
            Future<travelsDTO> future = executor.submit(() -> {
                
                travelsDTO travel = GSON.fromJson(getTravelData(), travelsDTO.class);
                return travel;
            });

            queue.add(future);
        }
            
        
        while (!queue.isEmpty()) {
            Future<travelsDTO> travel = queue.poll();
            if (travel.isDone()) {
                travels.add(travel.get());
            } else {
                queue.add(travel);
            }
        }

        return travels;
    
}
   public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        cityFacade sf = cityFacade.getCityFacade();
        
        List<travelsDTO> travels = sf.getAll();
        
        for (travelsDTO travel : travels) {
            System.out.println(travel);
        }
        
    }
    
}

