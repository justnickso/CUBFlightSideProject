package tw.nick.cubflying.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import tw.nick.cubflying.proto.CurrencyPreferences
import java.io.InputStream
import java.io.OutputStream

object CurrencyPreferenceSerializer : Serializer<CurrencyPreferences> {
    override val defaultValue: CurrencyPreferences = CurrencyPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrencyPreferences {
        return try {
            CurrencyPreferences.parseFrom(input)
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: CurrencyPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.currencyDataStore: DataStore<CurrencyPreferences> by dataStore(
    fileName = "preferences_currency.pb",
    serializer = CurrencyPreferenceSerializer
)

class CurrencyDataStore(dataStore: DataStore<CurrencyPreferences>) :
    BaseDataStore<CurrencyPreferences>(dataStore, CurrencyPreferences.getDefaultInstance()) {

        suspend fun setCurrency(currency: String, rate: Double) {
            dataStore.updateData { preferences ->
                preferences.toBuilder()
                    .setCurrencyName(currency)
                    .setCurrencyRate(rate)
                    .build()
            }
        }

        suspend fun getCurrency(): CurrencyPreferences = getPreferences()

        suspend fun getCurrencyName(): String = getPreferences().currencyName
}