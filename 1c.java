    MongoClient mongoClient = new MongoClient( "localhost" );
    DB db = mongoClient.getDB( "db" );
    DBCollection trains = db.getCollection("trains");
    ArrayList<String> tagi = new ArrayList<String>();
    DBCursor trainsCursor = trains.find();
    try{
        while(trainsCursor.hasNext()){
            tagi.add(trainsCursor.next().get("tags").toString());
        };
        tagi.size(); // wszystkie tagi
        HashSet tagiRozne = new HashSet();
        tagiRozne.addAll(tagi); // rozne tagi
        }
        finally {
            trainsCursor.close();
    }