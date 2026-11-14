package org.akua25258;

public class Config
{
    // private final Properties properties = new Properties();

    private boolean isTimerRunning = false;
    private boolean isOnFps = false;
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

    public boolean isTimerRunning() { return this.isTimerRunning; }
    public boolean isOnFps() { return this.isOnFps; }
    public int getTextPosition() { return this.textPosition; }

    public void setIsTimerRunning(boolean bool) { this.isTimerRunning = bool; }
    public void setIsOnFps(boolean bool) { this.isOnFps = bool; }
    public void setTextPosition(int i) { this.textPosition = i; }
}
