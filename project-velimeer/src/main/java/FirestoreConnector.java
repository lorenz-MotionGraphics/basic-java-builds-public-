import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreConnector {
    private static Firestore db;
    private static boolean initialized = false;
private static FirebaseApp app;

public static void initializeFirebase() throws IOException {
    if (!initialized) {
        FileInputStream serviceAccount = new FileInputStream("furmart-firebase-adminsdk-fbsvc-a44a371e5b.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://furmart-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        app = FirebaseApp.initializeApp(options);
        initialized = true;
    }
}

public static Firestore getDb() throws IOException {
    if (db == null) {
        initializeFirebase();
        db = FirestoreClient.getFirestore(app);
    }
    return db;
}

public static FirebaseDatabase getRealtimeDb() throws IOException {
    initializeFirebase();
    return FirebaseDatabase.getInstance(app);
}

}
 

//FileInputStream serviceAccount = new FileInputStream("furmart-firebase-adminsdk-fbsvc-a44a371e5b.json");