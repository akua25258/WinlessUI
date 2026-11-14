package org.akua25258;

import java.util.Properties;

public class Config
{
    private final Properties properties = new Properties();

    private int textPosition = 5;

//    public Config()
//    {
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
//            if (input == null) throw new RuntimeException("設定ファイルが見つからない");
//            properties.load(input);
//        } catch (IOException e) { throw new RuntimeException("設定ファイルの読み込みに失敗", e); }
//    }

//    public String getString(String key) { return properties.getProperty(key); }
//    public boolean getBoolean(String key) { return Boolean.parseBoolean(properties.getProperty(key)); }
//    public int getInt(String key)
//    {
//        String value = properties.getProperty(key);
//        return (value != null) ? Integer.parseInt(value) : -1;
//    }

    public int getTextPosition() { return this.textPosition; }

    public void setTextPosition(int i) { this.textPosition = i; }
}
