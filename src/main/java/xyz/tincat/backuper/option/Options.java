package xyz.tincat.backuper.option;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


public class Options implements IOptions, Serializable {
    private static Logger LOGGER = LoggerFactory.getLogger(Options.class);
    private static String env;
    private static Map<String, Options> instanceMap = new HashMap<>();
    private final HashMap<String, String> propMap;

    private Options(String fileName) {
        propMap = new HashMap<>();
        init(fileName);
    }

    public synchronized static Options getInstance(String fileName) {
        if (env == null) {
            env = System.getProperty("env");
            LOGGER.info("System.getProperty(\"env\") = "+env);
            if (env == null || env.equals("")) {
                InputStream is = Options.class.getClassLoader().getResourceAsStream("env.propMap");
                Properties props = new Properties();
                try {
                    props.load(is);
                    env = props.getProperty("env");
                } catch (Exception e) {
                    LOGGER.warn("env.propMap does not exist");
                    env = "";
                }
                LOGGER.warn("env from env.propMap = " + env);
            } else {
                LOGGER.warn("env from System.getProperty(\"env\")= " + env);
            }
        }
        if (!instanceMap.containsKey(fileName)) {
            instanceMap.put(fileName, new Options(getEnvFileName(fileName)));
        }
        return instanceMap.get(fileName);
    }

    private static String getEnvFileName(String fileName) {
        if (env == null || Objects.equals(env, "")) {
            return fileName;
        } else {
            return fileName.replace(".propMap", "-" + env + ".propMap");
        }
    }

    @Override
    public Map<String, String> getMap() {
        return this.propMap;
    }

    private void init(String fileName) {
        try {
            InputStream is = Options.class.getClassLoader().getResourceAsStream(fileName);
            BufferedInputStream in = new BufferedInputStream(is);
            Properties props = new Properties();
            props.load(new InputStreamReader(in, "gbk"));
            props.stringPropertyNames().forEach(s -> propMap.put(s, readValue(props.getProperty(s))));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String readValue(String s) {
        if (s.startsWith("${") && s.endsWith("}")) {
            return System.getenv(s.substring(2, s.length() - 1));
        }else{
            return s;
        }
    }

    public String get(String key) {
        return propMap.get(key);
    }

    @Override
    public Properties getPropMap() {
        Properties properties = new Properties();
        properties.putAll(this.propMap);
        return properties;
    }
}