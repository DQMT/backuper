package xyz.tincat.backuper.option;


import java.util.Map;
import java.util.Properties;

public interface IOptions {
    Map<String, String> getMap();

    String get(String key);

    Properties getProperties();
}