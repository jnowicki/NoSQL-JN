import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.BasicDBObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;


public class MongoDBScript {

    public static void main(String[] args) {
        skrypcior();
    }
    
    private static void skrypcior(){
        try{
        MongoClient mongoClient = new MongoClient( "localhost" );
        DB db = mongoClient.getDB( "db" );
        DBCollection trains = db.getCollection("trains");
        ArrayList<String> tagiUnique = new ArrayList<String>(); //unikalne tagi
        Map tagsFreq = new TreeMap<String, Integer>(); //czestotliwosc tagow
        DBCursor trainsCursor = trains.find();
        long startTime = System.nanoTime();
        long estimatedTime;
        try{
            int i = 0;
            int trainsCount = trainsCursor.count();
            
            while(trainsCursor.hasNext()){
                
                BasicDBObject train = (BasicDBObject) trainsCursor.next();
                
                i++;
                if(i % 10000 == 0){
                    System.out.println(i + "/" + trainsCount); //postęp
                }
                String tempstr;
                try{                 
                    tempstr = train.get("Tags").toString();
                } catch (NullPointerException e){
                    tempstr = "undefined";
                }
                String[] tags = tempstr.split(" ");
                
                // zamiana stringow na tagi
                train.put("Tags", tags);
                trains.save(train);
                
                for(String tag : tags){ //liczenie czestotliwosci
                    Integer count = (Integer)tagsFreq.get(tag);
                    if (count == null) {
                        tagsFreq.put(tag, 1);
                    }
                    else {
                        tagsFreq.put(tag, count + 1);
                    }
                }
                
            }
            
            estimatedTime = System.nanoTime() - startTime;
            
            System.out.println("Częstotliwość wystąpień tagów:");
            
            Iterator<Map.Entry<String, Integer>> entries = tagsFreq.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Integer> entry = entries.next();
                String tag = entry.getKey();
                Integer count = entry.getValue();
                if(count == 1) tagiUnique.add(tag);               
                System.out.println(tag + " - " + count);
                }
            System.out.println("----------\n");
            System.out.println("Unikalne tagi:");
            for(String s: tagiUnique){
                System.out.println(s); // printuj na wyjście wszystkie tagi które wystąpiły tylko raz
            }
            System.out.println("Pobieranie tagów i ich zamiana z bazy trwała " + TimeUnit.SECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS) + " sekund");
        }
        finally {
            trainsCursor.close();
        }        
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}