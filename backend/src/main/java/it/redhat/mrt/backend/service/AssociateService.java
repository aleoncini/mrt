package it.redhat.mrt.backend.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

import io.quarkus.mongodb.runtime.MongoClientName;
import it.redhat.mrt.backend.model.Associate;
import it.redhat.mrt.backend.model.serializer.AssociateSerializer;

@ApplicationScoped
public class AssociateService {
    @MongoClientName("mrt")
    @Inject MongoClient mongoClient;

    @Inject
    AssociateSerializer<Document> associateSerializer;
    
    public List<Associate> list(){
        List<Associate> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("associates").find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Associate associate = associateSerializer.deserialize(document);
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
        return associateSerializer.deserialize(associate);
    }

    public void add(Associate associate){
        Document document = associateSerializer.serialize(associate);
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("associates").insertOne(document);
        }
    }

}