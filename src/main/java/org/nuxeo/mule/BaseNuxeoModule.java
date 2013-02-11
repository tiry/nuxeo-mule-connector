package org.nuxeo.mule;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.Placement;
import org.nuxeo.ecm.automation.client.AutomationClient;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;

public class BaseNuxeoModule {

    protected Session session;

    /**
     * Nuxeo Server name (IP or DNS name)
     */
    @Configurable
    @Placement(group = "Connection")
    protected String serverName;

    /**
     * Port used to connect to Nuxeo Server
     */
    @Configurable
    @Placement(group = "Connection")
    protected String port;

    /**
     * Context Path for Nuxeo instance
     */
    @Configurable
    @Placement(group = "Connection")
    protected String contextPath = "nuxeo";

    /**
     * get Nuxeo Server Name
     * 
     * @return Nuxeo Server Name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * get Nuxeo Server Port
     * 
     * @return Nuxeo Server Port
     */
    public String getPort() {
        return port;
    }

    /**
     * get Nuxeo Server Context pat
     * 
     * @return Nuxeo Server Context path
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * set Nuxeo Server name
     * 
     * @param serverName
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * set port used to connect to Nuxeo Server
     * 
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * set Context path of the target Nuxeo Server
     * 
     * @param contextPath
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    protected String getServerUrl() {
        return "http://" + serverName + ":" + port + "/" + contextPath
                + "/site/automation";
    }

    protected Session openAutomationSession(String username, String password) {
        AutomationClient client = new HttpAutomationClient(getServerUrl());
        session = client.getSession(username, password);
        return session;
    }

    protected void closeAutomationSession() {
        if (session != null) {
            session.close();
        }
    }
}
