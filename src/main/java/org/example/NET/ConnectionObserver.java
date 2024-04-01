package org.example.NET;

import java.io.IOException;

public interface ConnectionObserver {

    //Возможные события в соединении

    void onConnectionReady(Connection connection);
    void onReceiveString(Connection connection, String value);
    void registeredClient(Connection connection, String name);
    void onDisconnect(Connection connection);
    void onException(Connection connection, Exception e);

}
