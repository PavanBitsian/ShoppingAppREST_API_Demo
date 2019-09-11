package model;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OrderClient {
    public OrderClient() {
        super();
    }

    public static void main(String[] args) {
        OrderClient orderClient = new OrderClient();

        String getProducts = "http://localhost:8080/products";
        System.out.println("getProducts:"+getStream(getProducts));
        String postProducts = "{\n" + 
        "\"productId\":\"12345\",\n" + 
        "\"productName\":\"Prestige Popular 3 liter\",\n" + 
        "\"description\":\"Pressure cooker\",\n" + 
        "\"price\":1250\n" + 
        "}\n";
        postStream(getProducts,postProducts);
        System.out.println(getStream(getProducts));
        
        String getInventory = "http://localhost:8080/inventory";
        System.out.println(getStream(getInventory));
        
        String postInventory1="{\n" + 
        "\"productId\":\"12345\",\n" + 
        "\"quantity\":125\n" + 
        "}\n";
        postStream(getInventory,postInventory1);
        System.out.println("getInventory:"+getStream(getInventory));
        
        String postInventory2="{\n" + 
        "\"productId\":\"12346\",\n" + 
        "\"quantity\":126\n" + 
        "}\n";
        postStream(getInventory,postInventory2);
        System.out.println("getInventory:"+getStream(getInventory));
        
        String OrderURL = "http://localhost:8080/orderdetails";
        String postOrder1 ="{\n" + 
        " \"orderId\":\"01\",\n" + 
        "\"prodId\":\"12345\",\n" + 
        " \"acctId\":\"1\",\n" + 
        "\"quantity\":10\n" + 
        "}\n";
        Thread t1= new Thread("Thread1"){
            @Override
            public void run(){
                for(int i=0;i<10;++i){
                    System.out.println("Thread1:"+postStream(OrderURL,postOrder1));
                    System.out.println("Thread1 getOrder:"+getStream(OrderURL));
                    System.out.println("Thread1 getInventory:"+getStream(getInventory));
                }
            }
        };
        
       
        
        String postOrder2 ="{\n" + 
        " \"orderId\":\"02\",\n" + 
        "\"prodId\":\"12345\",\n" + 
        " \"acctId\":\"2\",\n" + 
        "\"quantity\":10\n" + 
        "}\n";
        
        Thread t2= new Thread("Thread2"){
            @Override
            public void run(){
                 for(int i=0;i<10;++i){
                    System.out.println("Thread2:"+postStream(OrderURL,postOrder2));
                    System.out.println("Thread2 getOrder:"+getStream(OrderURL));
                    System.out.println("Thread2 getInventory:"+getStream(getInventory));
                }
            }
        };
        t1.start();
        t2.start();
        String postOrder3 ="{\n" + 
        " \"orderId\":\"02\",\n" + 
        "\"prodId\":\"12347\",\n" + 
        " \"acctId\":\"2\",\n" + 
        "\"quantity\":10\n" + 
        "}\n";
        
        System.out.println(postStream(OrderURL,postOrder3));

    }


    public static String getStream(String restURL) {
        URL url;
        StringBuffer out = new StringBuffer();
        try {
            url = new URL(restURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            InputStream is = (conn.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                out.append(output);
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return out.toString();
    }

    public static String postStream(String restURL,String payload) {
        StringBuffer out = new StringBuffer();
        try {

            URL url = new URL(restURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            
            String input = payload;

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                out.append(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return out.toString();

    }
}
