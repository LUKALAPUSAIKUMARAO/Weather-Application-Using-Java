import java.awt.BorderLayout;
import javax.swing.*;

public class WeatherApp {
    private UserPreferences userPreferences = new UserPreferences();
    private ForecastHistory forecastHistory = new ForecastHistory();
    private NotificationManager notificationManager = new NotificationManager();
    private WeatherAPIManager apiManager = new WeatherAPIManager(userPreferences); // Pass userPreferences


    public void displayWeather(Location location) {
        WeatherData currentWeather = apiManager.getCurrentWeather(location);
        forecastHistory.addForecast(currentWeather);
        showWeatherInWindow(currentWeather, "Current Weather for " + location.getCity());

        // Check for alerts based on weather data
        notificationManager.checkAndSendWeatherAlert(currentWeather);
    }

    public void displayForecast(Location location, int days) {
        WeatherData[] forecast = apiManager.getForecast(location, days);
        for (WeatherData data : forecast) {
            forecastHistory.addForecast(data);
            // Check each day's weather for alerts
            notificationManager.checkAndSendWeatherAlert(data);
        }
        showForecastInWindow(forecast, "Weather Forecast for " + location.getCity());
    }

    public void updateUserPreferences() {
        // Prompt to choose unit
        String[] unitsOptions = {"Metric", "Imperial"};
        int unitsChoice = JOptionPane.showOptionDialog(null, "Choose preferred units:", "Preferences",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, unitsOptions, unitsOptions[0]);
    
        if (unitsChoice == 0) {
            userPreferences.setPreferredUnits("metric");
        } else if (unitsChoice == 1) {
            userPreferences.setPreferredUnits("imperial");
        }
    
        // Prompt to enable or disable notifications
        int notificationsChoice = JOptionPane.showConfirmDialog(null, "Enable notifications?", "Preferences", JOptionPane.YES_NO_OPTION);
        userPreferences.setNotificationsEnabled(notificationsChoice == JOptionPane.YES_OPTION);
    
        // Confirm saved preferences
        JOptionPane.showMessageDialog(null, "Preferences updated:\nUnits: " + userPreferences.getPreferredUnits() +
                "\nNotifications: " + (userPreferences.isNotificationsEnabled() ? "Enabled" : "Disabled"));
    }
    

    private void showWeatherInWindow(WeatherData weatherData, String title) {
        JFrame frame = new JFrame(title);
        JTextArea textArea = new JTextArea(weatherData.toString());
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void showForecastInWindow(WeatherData[] forecast, String title) {
        JFrame frame = new JFrame(title);
        JTextArea textArea = new JTextArea();
        for (WeatherData data : forecast) {
            textArea.append(data.toString() + "\n\n");
        }
        textArea.setEditable(false);

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        WeatherApp app = new WeatherApp();
        String city = JOptionPane.showInputDialog("Enter location city:");
        Location location = new Location(city);

        String[] options = {"Current Weather", "Weather Forecast", "Update Preferences"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Weather App",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            app.displayWeather(location);
        } else if (choice == 1) {
            String daysString = JOptionPane.showInputDialog("Enter the number of days for forecast:");
            int days = Integer.parseInt(daysString);
            app.displayForecast(location, days);
        } else if (choice == 2) {
            app.updateUserPreferences();
        }
    }
}
