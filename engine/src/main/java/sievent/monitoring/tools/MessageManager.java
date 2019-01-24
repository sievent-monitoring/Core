package sievent.monitoring.tools;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {
    private ResourceBundle messages;

    public MessageManager(Locale locale, String bundleBaseName) {
        messages =  ResourceBundle.getBundle(bundleBaseName,locale);
    }

    public String getMessage(String key) {
        if ( messages != null) {
            String message =  messages.getString(key);
            if ( message == null) {
                return "< Message not found >";
            }
            return message;
        }
        return "< Bundle not found >";
    }

    public String getFormatMessage(String key,Object ... params)
    {
        return String.format(getMessage(key),params);
    }

}
