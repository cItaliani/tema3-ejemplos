/*-
 * =====LICENSE-START=====
 * Java 11 Application
 * ------
 * Copyright (C) 2020 - 2025 Organization Name
 * ------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====LICENSE-END=====
 */

package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.io.FileOutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.net.ssl.HttpsURLConnection;

public class Application {

  public static JsonValue leeJSON(String ruta) {
    try {
      if (ruta.toLowerCase().startsWith("http://")) {
        return leerHttp(ruta);
      } else if (ruta.toLowerCase().startsWith("https://")) {
        return leerHttps(ruta);
      } else {
        return leerFichero(ruta);
      }
    } catch (IOException e) {
      System.out.println("Error procesando documento Json " +
          e.getLocalizedMessage());
      return null;
    }
  }

  public static JsonValue leerFichero(String ruta) throws FileNotFoundException {
    try (JsonReader reader = Json.createReader(new FileReader(ruta))) {
      return reader.read();
      /*
       * JsonStructure jsonSt = reader.read();
       * System.out.println(jsonSt.getValueType());
       * JsonObject jsonObj = reader.readObject();
       * System.out.println(jsonObj.getValueType());
       * JsonArray jsonArr = reader.readArray();
       * System.out.println(jsonArr.getValueType());
       */
    }
  }

  public static JsonValue leerHttp(String direccion) throws IOException {
    URL url = new URL(direccion);
    try (InputStream is = url.openStream();
        JsonReader reader = Json.createReader(is)) {
      return reader.read();
    }
  }

  public static JsonValue leerHttps(String direccion) throws IOException {
    URL url = new URL(direccion);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    try (InputStream is = conn.getInputStream();
        JsonReader reader = Json.createReader(is)) {
      return reader.read();
    } finally {
      conn.disconnect();
    }
  }

  public static void escribeJSON(JsonValue json, File f) throws FileNotFoundException {
    System.out.println("Guardando tipo: " + json.getValueType());
    PrintWriter pw = new PrintWriter(f);
    JsonWriter writer = Json.createWriter(pw);
    // writer.write((JsonStructure) json);
    if (json.getValueType() == JsonValue.ValueType.OBJECT) {
      writer.writeObject(json.asJsonObject());
      // writer.writeObject((JsonObject)json);
    } else if (json.getValueType() == JsonValue.ValueType.ARRAY) {
      writer.writeArray(json.asJsonArray());
      // writer.writeArray((JsonArray)json);
    } else
      System.out.println("No se soporta la escritura");
    writer.close();
  }

  public static JsonArray creaArray() {
    JsonArray array = (JsonArray) Json.createArrayBuilder()
        .add(Json.createObjectBuilder()
            .add("titulo", "El atlas de las nubes")
            .add("año", 2012)
            .add("directores", "Lana Wachowski, Tom Tykwer, Lilly Wachowski")
            .add("interpretes", Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                    .add("nombre", "Tom Hanks")
                    .add("fechaNacimiento", Json.createObjectBuilder()
                        .add("año", "1956")
                        .add("mes", 8)))
                .add(Json.createObjectBuilder()
                    .add("nombre", "Halle Berry")
                    .add("fechaNacimiento", Json.createObjectBuilder()
                        .add("año", "1966")
                        .add("mes", 7)))))
        .add(Json.createObjectBuilder()
            .add("titulo", "La red social")
            .add("año", 2010)
            .add("directores", "David Fincher")
            .add("interpretes", Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                    .add("nombre", "Jesse Eisenberg")
                    .add("fechaNacimiento", Json.createObjectBuilder()
                        .add("año", "1983")
                        .add("mes", 9)))
                .add(Json.createObjectBuilder()
                    .add("nombre", "Andrew Garfield")
                    .add("fechaNacimiento", Json.createObjectBuilder()
                        .add("año", "1983")
                        .add("mes", 7)))))
        .build();
    return array;
  }

  public static void navegarpelis() {
    // JsonValue json=
    // leeJSON("jsonproject\\src\\main\\java\\resources\\pelis.json");
    // //System.out.println(json);
    // JsonArray raiz= json.asJsonArray();
    // //System.out.println(raiz.size());
    JsonArray raiz = creaArray();

    for (JsonValue pelicula : raiz) {
      JsonObject p = pelicula.asJsonObject();
      System.out.print("Título: " + p.getString("titulo"));
      System.out.print("Año: " + p.getInt("año"));
      System.out.println(" | Directores: " + p.getString("directores"));
      JsonArray interpretes = p.getJsonArray("interpretes");
      System.out.println("interpretes:");
      for (JsonValue interprete : interpretes) {
        JsonObject interp = interprete.asJsonObject();
        System.out.println(interp.getString("nombre"));
        // System.out.println(interp.getJsonObject("fechaNacimineto").getInt("año")); //
        // ver por que estas dos no funcionan
        // System.out.println(interp.getJsonObject("fechaNacimineto").getInt("mes"));
        System.out.println(interp.getJsonObject("fechaNacimiento").getJsonString("año") + " - "
            + interp.getJsonObject("fechaNacimiento").getJsonString("mes"));

      }
    }
  }

  public static void generaEndisco(File f) throws FileNotFoundException {
    JsonGenerator generator = Json.createGenerator(new FileOutputStream(f));
    generator.writeStartArray()
        .writeStartObject()
        .write("titulo", "El atlas de las nubes")
        .write("año", 2012)
        .write("directores", "Lana Wachowski, Lilly Wachowski")
        .writeStartArray("intepretes")
        .writeStartObject()
        .write("nombre", "Tom Hanks")
        .writeStartObject("fechaNacimiento")
        .write("año", "1956")
        .write("mes", 8)
        .writeEnd()
        .writeEnd()
        .writeStartObject()
        .write("nombre", "Halle Berry")
        .writeStartObject("fechaNacimiento")
        .write("año", "1966")
        .write("mes", 7)
        .writeEnd()
        .writeEnd()
        .writeEnd()
        .writeEnd()
        .writeEnd()
        .close();
  }

  public static void main(String[] args) throws FileNotFoundException {
    // JsonValue json = leeJSON("https://pokeapi.co/api/v2/pokemon/ditto");
    // escribeJSON(json, new
    // File("jsonproject\\src\\main\\java\\resources\\ditto.json"));
    // navegarpelis();
    generaEndisco(new File("jsonproject\\src\\main\\java\\Resources\\pelisgenerado.json"));
  }
}
