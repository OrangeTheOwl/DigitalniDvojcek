/*import android.util.Log
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document

class MongoDBConnection {
    //private val connectionString = "mongodb://rri:rriDigitalniDvojcek@cluster0-shard-00-00.1aaw6qs.mongodb.net:27017,cluster0-shard-00-01.1aaw6qs.mongodb.net:27017,cluster0-shard-00-02.1aaw6qs.mongodb.net:27017/?ssl=true&replicaSet=atlas-1aaw6qs-shard-0&authSource=admin&retryWrites=true&w=majority"
    //private val connectionString = "mongodb://rri%40admin:rri@cluster0.1aaw6qs.mongodb.net:27017/?retryWrites=true&w=majority"
    //private val connectionString = "mongodb://rri:rri@cluster0-shard-00-00.1aaw6qs.mongodb.net:27017,cluster0-shard-00-01.1aaw6qs.mongodb.net:27017,cluster0-shard-00-02.1aaw6qs.mongodb.net:27017/?ssl=true&replicaSet=atlas-1aaw6qs-shard-0&authSource=admin&retryWrites=true&w=majority"
    //private val connectionString = "mongodb://rri:rriDigitalniDvojcek@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    //private val connectionString = "mongodb://rri:rriDigitalniDvojcek@ac-mze5yfq-shard-00-00.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-01.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-02.1aaw6qs.mongodb.net:27017/?replicaSet=ac-mze5yfq-shard-0&authSource=admin&retryWrites=true&w=majority"
    //private val connectionString = "mongodb+srv://rri:rriDigitalniDvojcek@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    private val connectionString = "mongodb://rri:rriDigitalniDvojcek@ac-mze5yfq-shard-00-00.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-01.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-02.1aaw6qs.mongodb.net:27017/?replicaSet=ac-mze5yfq-shard-0&authSource=admin&retryWrites=true&w=majority&tls=true"


    private val client by lazy {
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()

        MongoClient.create(settings)
    }

    suspend fun getAllData(): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                val database = client.getDatabase("website")
                val collection: MongoCollection<Document> = database.getCollection("crowdData")
                collection.find().toList()
            } catch (e: Exception) {
                Log.e("MongoDB", "Error fetching data", e)
                emptyList()
            }
        }
    }
}
*/
/*
import android.util.Log
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bson.Document

class MongoDBConnection {

}

fun main() {
    // Povezovalni niz za vaš MongoDB Atlas grozd
    val uri = "mongodb+srv://rri:rriDigitalniDvojcek@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    // Ustvari odjemalca MongoDB
    val mongoClient = MongoClient.create(uri)
    val database = mongoClient.getDatabase("website")
    val collection = database.getCollection<Document>("crowdData") // Spremenjeno na `Document` za splošne podatke

    // Funkcija za pridobitev vseh podatkov
    suspend fun getAllData(): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                collection.find().toList() // Pretvori rezultate v seznam
            } catch (e: Exception) {
                Log.e("MongoDB", "Error fetching data", e)
                emptyList()
            }
        }
    }

    // Zaženi asinhrono pridobivanje podatkov
    runBlocking {
        val allData = getAllData()
        if (allData.isNotEmpty()) {
            println("All Data: $allData")
        } else {
            println("DATABASE data: !!! No data found in collection.!!!")
        }
    }

    // Zapri odjemalca MongoDB
    mongoClient.close()
}
*/
/*
import android.util.Log
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document

class MongoDBConnection {

    // Povezovalni niz za vaš MongoDB Atlas grozd
    private val uri = "mongodb+srv://rri:rriDigitalniDvojcek@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    // Ustvari odjemalca MongoDB
    private val mongoClient: MongoClient = MongoClient.create(uri)
    private val database = mongoClient.getDatabase("website")
    private val collection: MongoCollection<Document> = database.getCollection("crowdData")

    // Funkcija za pridobitev vseh podatkov iz zbirke `crowdData`
    suspend fun getAllData(): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                collection.find().toList() // Pretvori rezultate v seznam
            } catch (e: Exception) {
                Log.e("MongoDB", "Error fetching data", e)
                emptyList()
            }
        }
    }

    // Funkcija za zaprtje odjemalca MongoDB
    fun closeConnection() {
        mongoClient.close()
    }
}
*/
import android.util.Log
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document

class MongoDBConnection {

    private var mongoClient: MongoClient? = null
    private var collection: MongoCollection<Document>? = null

    init {
        val uri = "mongodb://rri:rriDigitalniDvojcek@ac-mze5yfq-shard-00-00.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-01.1aaw6qs.mongodb.net:27017,ac-mze5yfq-shard-00-02.1aaw6qs.mongodb.net:27017/?replicaSet=atlas-1aaw6qs-shard-0&authSource=admin&retryWrites=true&w=majority&tls=true&connectTimeoutMS=20000&socketTimeoutMS=20000"

        try {
            val settings = MongoClientSettings.builder()
                .applyConnectionString(com.mongodb.ConnectionString(uri))
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                .build()

            mongoClient = MongoClient.create(settings)
            val database = mongoClient!!.getDatabase("website")
            collection = database.getCollection("crowdData")
        } catch (e: Exception) {
            Log.e("MongoDBConnection", "Error initializing MongoDB connection", e)
        }
    }

    suspend fun getAllData(): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                collection?.find()?.toList() ?: emptyList()
            } catch (e: Exception) {
                Log.e("MongoDBConnection", "Error fetching data", e)
                emptyList()
            }
        }
    }

    fun closeConnection() {
        try {
            mongoClient?.close()
        } catch (e: Exception) {
            Log.e("MongoDBConnection", "Error closing MongoDB connection", e)
        }
    }
}





