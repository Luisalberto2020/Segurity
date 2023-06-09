package com.memerland.segurity.daos;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.memerland.segurity.model.Transfer;
import com.memerland.segurity.mongo.BasicDao;
import com.mongodb.client.MongoCursor;

public class TransferDao extends BasicDao<Transfer, String> {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TransferDao() {
        super("transfers", Transfer.class);
    }

    public List<Transfer> getTransfers(String name) {
        ArrayList<Transfer> transfers = new ArrayList<>();
        MongoCursor<Document> cursor = database.getCollection(collectionName).find(new Document("payer", name)).iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            Transfer transfer = new Transfer(doc.getString("payer"), doc.getString("receiver"), doc.getInteger("amount")
            ,LocalDateTime.parse(doc.getString("date"),formatter ));
            transfers.add(transfer);
        }
        MongoCursor<Document> cursor2 = database.getCollection(collectionName).find(new Document("receiver", name)).iterator();
        while (cursor2.hasNext()) {
            Document doc = cursor2.next();
            Transfer transfer = new Transfer(doc.getString("payer"), doc.getString("receiver"), doc.getInteger("amount"),LocalDateTime.parse(doc.getString("date"),formatter ));
            transfers.add(transfer);
        }
        

       
        return transfers;
    }


}
