package org.nuxeo.mule;

import java.io.IOException;
import java.net.ServerSocket;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceThreadingModel;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.display.Placement;
import org.mule.api.callback.SourceCallback;
import org.mule.api.callback.StopSourceCallback;
import org.mule.transport.http.HttpConnector;

/**
 * Module to expose a EndPoint that can recieve Http/Json calls.
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * 
 */
// @Module(name = "nuxeoEndPoint", schemaVersion = "1.0-SNAPSHOT")
public class NuxeoEndpointModule extends BaseNuxeoModule {

    /**
     * Username
     */
    @Configurable
    @Placement(group = "Connection")
    protected String username;

    /**
     * Password
     */
    @Configurable
    @Placement(group = "Connection")
    @Password
    protected String password;

    /**
     * Get username used to call remote server to initiate dialog
     * 
     * @return username
     * @since 5.7
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get username used to call remote server to initiate dialog
     * 
     * @param username
     * @since 5.7
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password used to call remote server to initiate dialog
     * 
     * @return the password
     * @since 5.7
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password used to call remote server to initiate dialog
     * 
     * @param password
     * @since 5.7
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Subscribe on the eventBus of a remote Nuxeo server
     * 
     * @param event eventName
     * @param callback callback used to inject in mule flow
     * 
     * @return stop controler
     * 
     * @since 5.7
     */
    @Source(threadingModel = SourceThreadingModel.NONE)
    public StopSourceCallback subscribeEvents(final String event,
            final SourceCallback callback) {

        openAutomationSession(username, password);

        registerToNuxeoServer(callback);

        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                unregisterToNuxeoServer();
                closeAutomationSession();
            }
        };
    }

    protected void registerToNuxeoServer(SourceCallback callback) {

        HttpConnector connector = new HttpConnector(null);
        try {
            ServerSocket server = connector.getServerSocketFactory().createServerSocket(
                    null, 8686, true);
            // server.

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void unregisterToNuxeoServer() {

    }

}
