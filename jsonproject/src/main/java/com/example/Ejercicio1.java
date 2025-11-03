package com.example;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

public class Ejercicio1 {

  // funcion Ejercicio 1
  public static JsonValue predicciones_meteorologicas1(String ciudad) {
    JsonValue datos = Application.leeJSON("https://api.openweathermap.org/data/2.5/weather?q=" + ciudad
        + ",es&lang=es&APPID=8f8dccaf02657071004202f05c1fdce0");
    return datos;
  }

  // funcion ejercicio 2
  public static JsonValue predicciones_meteorologicas2(String latitud, String longitud) {
    JsonValue datos = Application.leeJSON("http://api.openweathermap.org/data/2.5/weather?lat=" + latitud + "&lon="
        + longitud + "&units=metric&units=metric&APPID=a975f935caf274ab016f4308ffa23453");
    return datos;
  }

  // funcion ejercicio 3
  public static JsonValue predicciones_meteorologicas3(String latitud, String longitud, String cantidad) {
    return Application.leeJSON("http://api.openweathermap.org/data/2.5/find?lat=" + latitud + "&lon=" + longitud
        + "&cnt=" + cantidad + "&APPID=a975f935caf274ab016f4308ffa23453");
  }

  // funcion ejercicio 4
  public static long id_ciudad(String nombre_ciudad) {
    JsonValue datos = Application.leeJSON("https://api.openweathermap.org/data/2.5/weather?q=" + nombre_ciudad
        + ",es&lang=es&APPID=8f8dccaf02657071004202f05c1fdce0");
    return datos.asJsonObject().getJsonNumber("id").longValue();
  }

  // funcion ejercicio 5
  // uso la funcion del ejercicio 2 predicciones_meteorologicas2();

  // funcion ejercicio 6
  // uso la funcion del ejercicio 1 predicciones_meteorologicas1();

  // funcion ejercicio 7
  // es la misma que la funcion predicciones_meteorologicas2() pero devolviendo
  // mas cosas;

  // funcion ejercicio 8
  // uso la funcion del ejercicio 3 predicciones_meteorologicas3();

  // funcion ejercicio 9
  public static JsonValue api_externa(String ruta) {
    JsonValue datos = Application.leeJSON(ruta);
    return datos;
  }

  public static String unixTimeToString(long unixTime) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return Instant.ofEpochSecond(unixTime).atZone(ZoneId.of("GMT+1")).format(formatter);
  }

  // ejercicio 11
  public static void informacion_detallada_lugar(String ruta) {
    System.out.println("informacion detallada de lugares: ");
    JsonObject datosdevueltos = api_externa(ruta).asJsonObject();
    JsonObject primerobjeto = datosdevueltos.getJsonObject("_embedded");
    JsonArray array_eventos = primerobjeto.getJsonArray("events");
    for (int i = 0; i < array_eventos.size(); i++) {
      JsonObject nuevo_embedded = array_eventos.getJsonObject(i).getJsonObject("_embedded");
      JsonArray array_venues = nuevo_embedded.getJsonArray("venues");
      for (int j = 0; j < array_venues.size(); j++) {
        System.out.println("---inicio evento " + (i + 1) + "---");
        System.out.println("Nombre: " + array_venues.getJsonObject(j).getString("name"));
        System.out.println("Ciudad: " + array_venues.getJsonObject(j).getJsonObject("city").getString("name"));
        System.out.println("Pais: " + array_venues.getJsonObject(j).getJsonObject("country").getString("name"));
        System.out.println("Direccion: " + array_venues.getJsonObject(j).getJsonObject("address").getString("line1"));
        System.out.print(
            "Localización: lat: " + array_venues.getJsonObject(j).getJsonObject("location").getString("longitude"));
        System.out.println(" | long: " + array_venues.getJsonObject(j).getJsonObject("location").getString("latitude"));
        System.out.println("---fin evento " + (i + 1) + "---\n");
      }
    }

  }

  public static void informacion_detallada_lugar12(String ruta) {
    System.out.println("\n ejercicio 12");
    JsonObject datosdevueltos = api_externa(ruta).asJsonObject();
    JsonObject primer_obj = datosdevueltos.getJsonObject("_embedded");
    JsonArray primer_array = primer_obj.getJsonArray("events");
    for (int i = 0; i < primer_array.size(); i++) {
      JsonString nombre = primer_array.getJsonObject(i).getJsonString("name");
      JsonObject segundo_obj = primer_array.getJsonObject(i).getJsonObject("_embedded");
      JsonArray segundo_array = segundo_obj.getJsonArray("venues");
      for (int j = 0; j < segundo_array.size(); j++) {
        JsonObject ciudad = segundo_array.getJsonObject(j).getJsonObject("city");
        String nombre_ciudad = ciudad.getJsonString("name").toString();
        String nombre_ciudad_tratado = nombre_ciudad.replace("\"", "").replace(" ", "%20");
        JsonValue prediccion = predicciones_meteorologicas1(nombre_ciudad_tratado);
        JsonObject prediccion_obj = prediccion.asJsonObject();
        String tiempo = prediccion_obj.getJsonArray("weather").getJsonObject(0).getString("description");
        System.out.println(
            "el evento: " + nombre + "en la ciudad de: " + nombre_ciudad_tratado + " tendrá un clima: " + tiempo);
      }
    }
  }

  public static void informacion_detallada_evento(String ruta) {
    // nombre fecha y hora
    System.out.println("informacion detallada de eventos: ");
    JsonObject datosdevueltos = api_externa(ruta).asJsonObject();
    JsonObject primerObjeto = datosdevueltos.getJsonObject("_embedded");
    JsonArray array_eventos = primerObjeto.getJsonArray("events");
    for (int i = 0; i < array_eventos.size(); i++) {
      JsonString nombre = array_eventos.getJsonObject(i).getJsonString("name");
      JsonObject segundo_objeto = array_eventos.getJsonObject(i).getJsonObject("dates");
      JsonObject tercer_objeto = segundo_objeto.getJsonObject("start");
      JsonString fecha = tercer_objeto.getJsonString("localDate");
      JsonString hora = tercer_objeto.getJsonString("localTime");
      System.out
          .println("el evento: " + nombre + " | tendrá lugar el dia: " + fecha + " | a las : " + hora + " horas");
    }
  }

  public static void main(String[] args) {

    String ciudad = "vigo";
    String cantidad = "3";
    // latitud y longitud de vigo
    String latitud = "42.2333";
    String longitud = "-8.7222";

    //ejercicio 1
    JsonValue datos1 = predicciones_meteorologicas1(ciudad);
    System.out.println("ejercicio 1");
    System.out.println(datos1);

    // ejercicio 2
    JsonValue datos2 = predicciones_meteorologicas2(latitud, longitud);
    System.out.println("ejercicio 2");
    System.out.println(datos2);

    // ejercicio 3
    // lat y log son mismas que el ejercicio 2
    JsonValue datos3 = predicciones_meteorologicas3(latitud, longitud, cantidad);
    System.out.println("ejercicio 3");
    System.out.println(datos3);

    // ejercicio 4
    System.out.println("ejercicio 4");
    long datos4 = id_ciudad(ciudad);
    System.out.println("para la ciudad: \"" + ciudad + "\" el id es: " + datos4);

    // ejercicio 5
    JsonValue datos5 = predicciones_meteorologicas2(latitud, longitud);
    System.out.println("ejercicio 5");
    System.out.println("el nombre de la ciudad es: " +
    datos5.asJsonObject().getString("name"));

    // ejercicio 6
    JsonValue datos6 = predicciones_meteorologicas1(ciudad);

    double longitud6 =
    (datos6.asJsonObject().getJsonObject("coord").getJsonNumber("lon").doubleValue());
    double latitud6 =
    (datos6.asJsonObject().getJsonObject("coord").getJsonNumber("lat").doubleValue());
    System.out.println("ejercicio 6");
    System.out.println("longitud: " + longitud6 + " | latitud: " + latitud6);

    // ejercicio 7
    JsonValue datos_devueltos = predicciones_meteorologicas2(latitud, longitud);
    String ciudad7 = datos_devueltos.asJsonObject().getString("name");
    long fecha = datos_devueltos.asJsonObject().getInt("dt");
    String date = unixTimeToString(fecha);
    double temperatura =
    datos_devueltos.asJsonObject().getJsonObject("main").getJsonNumber("temp").doubleValue();
    long humedad =
    datos_devueltos.asJsonObject().getJsonObject("main").getJsonNumber("humidity").longValue();
    long nubes =
    datos_devueltos.asJsonObject().getJsonObject("clouds").getJsonNumber("all").longValue();
    long velocidad_viento =
    datos_devueltos.asJsonObject().getJsonObject("wind").getJsonNumber("speed").longValue();
    String pronostico =
    datos_devueltos.asJsonObject().getJsonArray("weather").getJsonObject(0)
    .getString("description");
    System.out.println("ejercicio 7");
    System.out
    .print("ciudad: " + ciudad7 + " | fecha: " + date + " | temperatura: " +
    temperatura + " | humedad: " + humedad
    + " | probabilidad de cielo con nubes: " + nubes + " | velocidad del viento:
    " + velocidad_viento
    + " | pronostico del tiempo: " + pronostico + "\n");

    // ejercicio 8
    JsonObject datos_devueltos8 = predicciones_meteorologicas3(latitud, longitud,
    cantidad).asJsonObject();
    JsonArray lista = datos_devueltos8.getJsonArray("list");
    System.out.println("ejercicio 8");
    for (int i = 0; i < lista.size(); i++) {
    JsonObject datos = lista.getJsonObject(i);
    String ciudad8 = datos.asJsonObject().getString("name");
    long fecha8 = datos.asJsonObject().getInt("dt");
    String date8 = unixTimeToString(fecha8);
    double temperatura8 =
    datos.asJsonObject().getJsonObject("main").getJsonNumber("temp").doubleValue();
    int humedad8 = datos.asJsonObject().getJsonObject("main").getInt("humidity");
    int nubes8 = datos.asJsonObject().getJsonObject("clouds").getInt("all");
    int velocidad_viento8 =
    datos.asJsonObject().getJsonObject("wind").getInt("speed");
    String pronostico8 =
    datos.asJsonObject().getJsonArray("weather").getJsonObject(0).getString("description");

    System.out.print(
    "ciudad: " + ciudad8 + " | fecha: " + date8 + " | temperatura: " +
    temperatura8 + " | humedad: " + humedad8
    + " | probabilidad de cielo con nubes: " + nubes8 + " | velocidad del viento:
    " + velocidad_viento8
    + " | pronostico del tiempo: " + pronostico8 + "\n");
    }

    // ejercicio 9
    String ruta =
    "https://opentdb.com/api.php?amount=20&category=12&difficulty=hard&type=multiple";
    JsonObject datos_devueltos9 = api_externa(ruta).asJsonObject();
    JsonArray lista9 = datos_devueltos9.getJsonArray("results");
    System.out.println("ejercicio 9");
    for (int i = 0; i < lista9.size(); i++) {
    JsonObject datos = lista9.getJsonObject(i);
    String pregunta = datos.asJsonObject().getString("question");
    System.out.println("QUESTION " + (i + 1) + ": " + pregunta);
    String respuesta_true = datos.asJsonObject().getString("correct_answer");
    System.out.println("[*] correcta: " + respuesta_true);
    JsonArray respuestas_false = datos.getJsonArray("incorrect_answers");
    for (int j = 0; j < respuestas_false.size(); j++) {
    String respuestaMala = respuestas_false.getString(j);
    System.out.println("error " + (j + 1) + ": " + respuestaMala);
    }
    System.out.println();
    }

    //ejercicio 10
    System.out.println("ejercicio 10");
    String tipo_evento = "sports";
    String nombre_evento;
    String ruta10 = "https://app.ticketmaster.com/discovery/v2/events.json?classificationName=" + tipo_evento
        + "&countryCode=ES&apikey=AMXR5Rf8zlr7oGucsebGKvDCLOQmGUGE";
    JsonObject datos_devueltos10 = api_externa(ruta10).asJsonObject();
    JsonObject principal = datos_devueltos10.getJsonObject("_embedded");
    JsonArray lista10 = principal.getJsonArray("events");
    for (int i = 0; i < lista10.size(); i++) {
      JsonObject datos10 = lista10.getJsonObject(i);
      nombre_evento = datos10.asJsonObject().getString("name");
      System.out.println("evento" + (i + 1) + ":" + nombre_evento);
    }
    //ejercicio11
    System.out.println("\n ejercicio11");
    informacion_detallada_lugar(ruta10);
    informacion_detallada_evento(ruta10);

   // ejercicio 12
    informacion_detallada_lugar12(ruta10);
  }

}
