package com.semyon.cucon

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI

fun requestCryptoRates(): JSONObject?{
    return(request("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH,DASH,ZEC,LTC,WAVES,EOS&tsyms=USD,EUR,RUB&api_key=$apiCryptoKey"))
}

fun requestRate(pair: String): Float? {
    val result = request("https://free.currencyconverterapi.com/api/v6/convert?q=" + pair + "&compact=ultra")
    try {
        return result!!.get(pair).toString().toFloat()
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        return null
    }
}

fun requestCurrencies(): Iterator<String>? {
    val currencies: Iterator<String>
    val result = request("https://free.currencyconverterapi.com/api/v6/currencies")
    return try {
        val result2 = result!!.getJSONObject("results")
        currencies = result2.keys()
        currencies
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        null
    }
}

fun request(url: String): JSONObject? {
    return try {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.connect()
        val text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        (JSONObject(text))
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        null
    }
}

fun isInternet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}