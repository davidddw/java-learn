package org.cloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;

/**
 * Hello world!
 *
 */
public class App {

    private static String host = "192.168.1.62";
    private static String user = "cloud";
    private static String passwd = "passwd";
    private static String db = "mydb";
    private static String Collection = "user";

    public static void main(String[] args) {
        // 获取数据库连接对象
        MongoDatabase mongoDatabase = App.getConnect(host, user, passwd, db);
        insertOneTest(mongoDatabase, Collection);
        System.out.println(getCount(mongoDatabase, Collection));
        System.out.println(findOne(mongoDatabase, Collection).toJson());
        System.out.println(findAll(mongoDatabase, Collection));
        System.out.println(update(mongoDatabase, Collection));
        System.out.println(delete(mongoDatabase, Collection));
    }

    public static MongoDatabase getConnect(String host, String user, String password, String database) {
        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
            .credential(credential)
            .applyToClusterSettings(builder -> 
                builder.hosts(Arrays.asList(new ServerAddress(host, 27017))))
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        return mongoDatabase;
    }

    private static void insertOneTest(MongoDatabase mongoDatabase, String collectionString) {
        // 获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        // 要插入的数据
        Document document = new Document("name", "张三").append("sex", "男").append("age", 18);
        // 插入一个文档
        collection.insertOne(document);
    }

    public static long getCount(MongoDatabase mongoDatabase, String collectionString) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        return collection.countDocuments();
    }

    public static Document findOne(MongoDatabase mongoDatabase, String collectionString) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        return collection.find().first();
    }

    public static List<Document> findAll(MongoDatabase mongoDatabase, String collectionString) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        List<Document> list = new ArrayList<>();
        for (Document cur : collection.find()) {
            list.add(cur);
        }
        return list;
    }

    public static UpdateResult update(MongoDatabase mongoDatabase, String collectionString) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        return collection.updateOne(Filters.eq("name", "张三1"), new Document("$set", new Document("age", 40)));
    }

    public static DeleteResult delete(MongoDatabase mongoDatabase, String collectionString) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionString);
        return collection.deleteOne(Filters.eq("name", "张三1"));
    }
}
