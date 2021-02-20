package brown.system.setup.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;

import brown.logging.library.ErrorLogging;
import brown.system.setup.ISetup;

public final class Setup implements ISetup {

  @Override
  public void setup(Kryo kryo) {
    start(kryo);
  }

  /**
   * helper that registers all classes with kryo
   * 
   * @param kryo the Kryo object
   * @return
   */
  public static boolean start(Kryo kryo) {
	kryo.setRegistrationRequired(false);
    return true;
  }

  /**
   * helper that returns every java class starting at a path
   * 
   * @param path the starting path for the search
   * @return every java class starting at path
   * @throws IOException
   */
  public static List<String> getJavaFiles(String path) throws IOException {
    List<String> output = new LinkedList<String>();
    Files.walk(Paths.get(path)).filter(Files::isRegularFile)
        .forEach(s -> output.add(s.toString().replaceAll(path, "")
            .replaceAll(".java", "").replaceAll("/", ".")));
    return output;
  }

}