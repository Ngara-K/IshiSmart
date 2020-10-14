package app.web.ishismart.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AppUtils {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    /*morning tea collection reference*/
    public static CollectionReference morningTeaReference = FirebaseFirestore.getInstance()
            .collection("posts").document("morning_tea").collection("tea_collection");

    /*daily poster collection reference*/
    public static CollectionReference dailyPosterReference = FirebaseFirestore.getInstance()
            .collection("posts").document("daily_posters").collection("posters_collection");

    /*editors profile reference*/
    public static CollectionReference editorProfileReference = FirebaseFirestore.getInstance().collection("editors");
}
