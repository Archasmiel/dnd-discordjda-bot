package net.archasmiel.dndbot.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.archasmiel.dndbot.database.json.BeautifulJson;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

public class ManaController {

  public static final String FILENAME = "mana.json";
  public static final Map<String, ManaUser> USERS = new HashMap<>();

  public static void readUsers() {
    try {
      File file = new File(FILENAME);
      if (file.createNewFile()){
        try (FileWriter writer = new FileWriter(file)) {
          writer.append("{\n").append("}");
        }
      }

      try (FileInputStream is = new FileInputStream(file)) {
        JSONObject jsonObject = new JSONObject(new JSONTokener(is));
        jsonObject.keys().forEachRemaining(e -> {
            JSONObject jsonObj = jsonObject.getJSONObject(e);
            USERS.put(
                e, new ManaUser(
                    jsonObj.getInt("max"),
                    jsonObj.getInt("curr"),
                    jsonObj.getString("class"),
                    jsonObj.getInt("level"),
                    jsonObj.getInt("param")
                )
            );
          }
        );
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeUsers() {
    try {
      File file = new File(FILENAME);
      if (file.createNewFile()){
        try (FileWriter writer = new FileWriter(file)) {
          writer.append("{\n").append("}");
        }
      }

      try (FileOutputStream os = new FileOutputStream(file)) {
        StringWriter writer = new StringWriter();
        JSONWriter jsonWriter = new JSONWriter(writer);
        jsonWriter.object();

        for (Map.Entry<String, ManaUser> entry: USERS.entrySet()) {
          jsonWriter
            .key(entry.getKey())
            .object()
            .key("max").value(entry.getValue().getMaxMana())
            .key("curr").value(entry.getValue().getCurrMana())
            .key("class").value(entry.getValue().getClassName())
            .key("level").value(entry.getValue().getLevel())
            .key("param").value(entry.getValue().getParameter())
            .endObject();
        }
        jsonWriter.endObject();
        os.write(
            BeautifulJson.beautifulJSON(writer.toString()).getBytes()
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Optional<ManaUser> getUser(String id) {
    return !USERS.containsKey(id) ? Optional.empty()
        : (!USERS.get(id).isFinished() ? Optional.empty() : Optional.of(USERS.get(id)));
  }

  public static boolean hasUser(String id) {
    return USERS.containsKey(id);
  }

  public static boolean isFinished(String id) {
    if (!hasUser(id)) return false;
    return USERS.get(id).isFinished();
  }


}