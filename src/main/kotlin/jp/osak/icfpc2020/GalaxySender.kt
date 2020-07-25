package jp.osak.icfpc2020

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GalaxySender {
    private val modem = GalaxyModem()

    fun send(data: Term, engine: GalaxyEngine): Term {
        val payload = modem.modulate(data, engine)
        println("Send: ${payload}")

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.pegovka.space/aliens/send?apikey=decffdda9f2d431792a37fbfb770f825"))
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println("Status code: ${response.statusCode()}")
        val body = response.body()
        println("Received: ${body}")

        return modem.demodulate(body)
    }
}