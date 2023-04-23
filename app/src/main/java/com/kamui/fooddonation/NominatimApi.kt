package com.kamui.fooddonation

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class NominatimApi {

    fun search(query: String, countryCode: String): List<String> {
        val url = "https://nominatim.openstreetmap.org/search?q=$query&format=html&countrycodes=$countryCode"
        val doc = Jsoup.connect(url).get()
        val results = mutableListOf<String>()
        for (result in doc.select("div.result")) {
            val address = result.selectFirst("span.addressparts")
            if (address != null) {
                results.add(address.text())
            }
        }
        return results
    }
}

