package it.redhat.mrt.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

import io.quarkus.mongodb.runtime.MongoClientName;

@ApplicationScoped
public class AssociateService {
    @MongoClientName("mrt")
    @Inject MongoClient mongoClient;

    public List<Associate> list(){
        List<Associate> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("associates").find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Associate associate = Associate.build(document);
                list.add(associate);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public Associate get(String id){
        Document query = new Document("rhid", id);
        Document associate = mongoClient.getDatabase("mrt").getCollection("associates").find(query).first();
        return Associate.build(associate);
    }

    public void add(Associate associate){
        Document document = associate.toDocument();
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("associates").insertOne(document);
        }
    }

}