import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import java.util.*;


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
        Map tagsFreq = new HashMap<String, Integer>(); //czestotliwosc tagow
        DBCursor trainsCursor = trains.find();
        try{
            int i = 0;
            int trainsCount = trainsCursor.count();
            
            while(trainsCursor.hasNext()){
                
                i++;
                if(i % 10000 == 0){
                    System.out.println(i + "/" + trainsCount); //postęp
                }
                String tempstr;
                try{
                    tempstr = trainsCursor.next().get("Tags").toString();
                } catch (NullPointerException e){
                    tempstr = "undefined";
                }
                String[] temp = tempstr.split(" ");
                for(String s : temp){
                    Integer count = (Integer)tagsFreq.get(s);
                    if (count == null) {
                        tagsFreq.put(s, 1);
                    }
                    else {
                        tagsFreq.put(s, count + 1);
                    }
                }
            }
            Iterator<Map.Entry<String, Integer>> entries = tagsFreq.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Integer> entry = entries.next();
                String tag = entry.getKey();
                Integer count = entry.getValue();
                if(count == 1) tagiUnique.add(tag);
                //System.out.println(tag + " - " + count);
                }
            for(String s: tagiUnique){
                System.out.println(s);
            }
        }
        finally {
            trainsCursor.close();
        }        
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
