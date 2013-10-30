/*
 * (C) Copyright 2006-2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Julien Anguenot
 *     Thierry Delprat
 *     Florent Guillaume
 */

package org.nuxeo.mule.poll;

import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.nuxeo.ecm.automation.client.model.DateUtils;

public class NuxeoSimpleEvent  {

    private static final long serialVersionUID = 3037187381843636097L;

    private long id;

    private String principalName;

    private String eventId;

    private Date eventDate;

    private Date logDate;

    private String docUUID;

    private String docType;

    private String docPath;

    private String category;

    private String comment;

    private String docLifeCycle;

    private String repositoryId;


    public NuxeoSimpleEvent (JsonNode event) {
        id = event.get("id").getLongValue();
        principalName = event.get("principalName").getTextValue();
        eventId = event.get("eventId").getTextValue();
        docUUID = event.get("docUUID").getTextValue();
        docType = event.get("docType").getTextValue();
        docLifeCycle = event.get("docLifeCycle").getTextValue();
        docPath = event.get("docPath").getTextValue();
        category = event.get("category").getTextValue();
        comment = event.get("comment").getTextValue();
        repositoryId = event.get("repositoryId").getTextValue();
        eventDate = DateUtils.parseDate(event.get("eventDate").getTextValue());
    }

    public long getId() {
        return id;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public String getEventId() {
        return eventId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Date getLogDate() {
        return logDate;
    }

    public String getDocUUID() {
        return docUUID;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocPath() {
        return docPath;
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public String getDocLifeCycle() {
        return docLifeCycle;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(this.id);
        sb.append(" ");
        sb.append(this.category);
        sb.append(" ");
        sb.append(this.eventId);
        sb.append(" ");
        sb.append(this.docUUID);
        sb.append(" ");
        sb.append(this.principalName);
        sb.append(" ");
        sb.append(this.eventDate.toString());

        return sb.toString();
    }


}
