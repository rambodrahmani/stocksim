package main.java;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import java.util.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.function.Consumer;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Filters.*;

/**
 * MongoDB Developer Harness test.
 */
public class Main {

    /**
     * Entry point.
     *
     * @param args
     */
    public static void main(final String[] args) {

        //-------------------------------
        //-----Connect to the MongoDB----
        //-------------------------------

        // 1 - Default URI "mongodb://localhost:27017"
        final ConnectionString uri = new ConnectionString("mongodb://172.16.3.94:27017");
        final MongoClient mongoClient = MongoClients.create(uri);


        // 2 - MongoDB Atlas URI
        //final ConnectionString uri = new ConnectionString("mongodb+srv://admin:<password>@largescaleandmultistruc.jhdlw.mongodb.net/<dbname>?retryWrites=true&w=majority");
        //final MongoClient mongoClientAtlas = MongoClients.create(uri);


        //-------------------------------
        //---------Get database----------
        //-------------------------------

        // If the database does not exists, mongoDB will create a new one
        final MongoDatabase db = mongoClient.getDatabase("test");

        // Loop through and print all the available collections
        for (String name : db.listCollectionNames()) {
            System.out.println(name);
        }

        //-------------------------------
        //---Get a specific collection---
        //-------------------------------
        final MongoCollection<Document> myColl = db.getCollection("students");

        // Count number of documents in a collection
        System.out.println(myColl.countDocuments());

        // Empty a collection
        myColl.drop();
        myColl.deleteMany(new Document());

        //-------------------------------
        //--------Insert documents-------
        //-------------------------------

        // 1 - Insert a single document into the collection
        Document student = new Document("name", "Laura")
                .append("age", 25)
                .append("gender", "F")
                .append("grades", Arrays.asList(
                        new Document("mark", 25).append("DateOfExam", new Date()).append("name", "PerformanceEvaluation"),
                        new Document("mark", 30).append("DateOfExam", new Date()).append("name", "ComputerArchitecture"),
                        new Document("mark", 28).append("DateOfExam", new Date()).append("name", "LargeScale")
                ))
                .append("location", new Document("x", 203).append("y", 102));
        myColl.insertOne(student);

        // 2 -Insert multiple documents into the collection
        List<Document> documents = new ArrayList<>();
        List<String> names = Arrays.asList("Gionatan", "Luigi", "Marco", "Federico", "Paolo");
        for (String name : names) {
            int markPE = (int) ((Math.random() * (30 - 18)) + 18);
            int markCA = (int) ((Math.random() * (30 - 18)) + 18);
            int markLS = (int) ((Math.random() * (30 - 18)) + 18);
            student = new Document("name", name)
                    .append("age", 25 + (int) ((Math.random() * 5) - 2))
                    .append("gender", "M")
                    .append("grades", Arrays.asList(
                            new Document("mark", markPE).append("DateOfExam", new Date()).append("name", "PerformanceEvaluation"),
                            new Document("mark", markCA).append("DateOfExam", new Date()).append("name", "ComputerArchitecture"),
                            new Document("mark", markLS).append("DateOfExam", new Date()).append("name", "LargeScale")
                    ))
                    .append("location", new Document("x", 203).append("y", 102));
            documents.add(student);
        }
        myColl.insertMany(documents);


        //-------------------------------
        //---------Find documents--------
        //-------------------------------

        // 1 - Find all the documents in the collection
        try (MongoCursor<Document> cursor = myColl.find().iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }

        // 2 - Find the first document in the collection: might be NULL
        Document firstDoc = myColl.find().first();
        if (firstDoc != null) System.out.println(firstDoc.toJson());

        // 3 - Find documents through filters: might be NULL
        //      Possible filters: eq. lt, lte, gt, gte, and, or, ...
        Document dbDoc = myColl.find(eq("name", "Federico")).first();
        if (dbDoc != null) System.out.println(dbDoc.toJson());

        // a - Define a consumer statically: printDocuments()
        //      --> defined as a private static member function (defined above)
        // b - Define the consumer locally
        Consumer<Document> printFormattedDocuments = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document.toJson(JsonWriterSettings.builder().indent(true).build()));
            }
        };

        //----Lambda version (more compact)----
        //Consumer<Document> printFormattedDocuments2 =
        //        document -> System.out.println(document.toJson(JsonWriterSettings.builder().indent(true).build()));

        // 25 <= age < 27
        myColl.find(and(gte("age", 25), lt("age", 27)))
                .forEach(printFormattedDocuments);

        //-------------------------------
        //--------Update documents-------
        //-------------------------------

        // 1 - Update a single document
        myColl.updateOne(eq("name", "Federico"), set("age", 25));
        Document newGrade = new Document("mark", 27).append("DateOfExam", new Date()).append("name", "Intelligent Systems");
        myColl.updateOne(eq("name", "Federico"), Updates.push("grades", newGrade));
        //myColl.updateOne(eq("name", "Federico"), addToSet("grades", newGrade));

        // 2 - Update many documents
        UpdateResult ur = myColl.updateMany(gt("age", 24), inc("age", 1));
        System.out.println("Modified documents: " + ur.getModifiedCount());
        myColl.updateMany(new Document(), rename("name", "student"));
        myColl.find().forEach(printDocuments());

        //-------------------------------
        //--------Delete documents-------
        //-------------------------------

        // Delete a single document
        myColl.deleteOne(eq("student", "Gionatan"));

        // Delete many documents
        DeleteResult dr = myColl.deleteMany(lt("age", 25));
        System.out.println("Deleted documents: " + dr.getDeletedCount());
        myColl.find().forEach(printDocuments());

        // Create an index
        myColl.createIndex(new Document("age", 1));

        // Execute in the MONGO SHELL
        // myColl.find().sort({"age": 1}).explain().queryPlanner.winningPlan
        // myColl.find().sort({"student": 1}).explain().queryPlanner.winningPlan

        //----------------------------------------
        //-----Close Connection to the MongoDB----
        //----------------------------------------
        mongoClient.close();
    }

    /**
     * Static definition of a consumer.
     *
     * @return
     */
    private final static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }

    /**
     * Static definition of the printFormattedDocuments consumer.
     *
     * @return
     */
    private final static Consumer<Document> printFormattedDocuments() {
        return doc -> System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));
    }
}