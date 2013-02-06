package org.nuxeo.mule;

import java.util.Map;

import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

/**
 * Stupid code to Map Operation already hard-coded in DocumentService.
 * 
 * This class could typically be generated from a list of Operation !
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * 
 */
public class BaseDocumentService {

    protected DocumentService docService;

    public BaseDocumentService() {
        super();
    }

    /**
     * Get a Document from Nuxeo repository
     * 
     * @param docRef the DocumentRef
     * @return a Document Object
     * @throws Exception in case of error
     */
    @Processor
    public Document getDocument(@Placement(group = "operation parameters")
    @FriendlyName("Document Reference (docRef)")
    String docRef) throws Exception {
        return docService.getDocument(docRef);
    }

    /**
     * Get the root Document of Nuxeo Repository
     * 
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document getRootDocument() throws Exception {
        return getDocument("/");
    }

    /**
     * Create a Document
     * 
     * @param parentRef reference of the Parent document
     * @param docType Document Type
     * @param docName name of the target Document
     * @param properties Metadata
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document createDocument(@Placement(group = "operation parameters")
    @FriendlyName("Parent document reference")
    String parentRef, @Placement(group = "operation parameters")
    @FriendlyName("Document Type")
    String docType, @Placement(group = "operation parameters")
    @FriendlyName("Name of the Document")
    String docName, @Placement(group = "operation parameters")
    Map<String, Object> properties) throws Exception {

        PropertyMap map = new PropertyMap(properties);
        return docService.createDocument(new DocRef(parentRef), docType,
                docName, map);
    }

    /**
     * Deletes a Document
     * 
     * @param ref reference of the Document to delete
     * @throws Exception
     */
    @Processor
    public void remove(@Placement(group = "operation parameters")
    String ref) throws Exception {
        docService.remove(ref);
    }

    /**
     * Copy a Document
     * 
     * @param src reference of the source document
     * @param targetParent reference of the destination document
     * @param docName name of the copied document
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document copy(@Placement(group = "operation parameters")
    String src, @Placement(group = "operation parameters")
    String targetParent, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String docName) throws Exception {
        if (docName == null || docName.isEmpty()) {
            return docService.copy(new DocRef(src), new DocRef(targetParent));
        } else {
            return docService.copy(new DocRef(src), new DocRef(targetParent),
                    docName);
        }
    }

    /**
     * Move a Document
     * 
     * @param src the reference of the document to move
     * @param targetParent the reference of thr target parent
     * @param docName the name of the document after move
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document move(@Placement(group = "operation parameters")
    String src, @Placement(group = "operation parameters")
    String targetParent, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String docName) throws Exception {
        if (docName == null || docName.isEmpty()) {
            return docService.move(new DocRef(src), new DocRef(targetParent));
        } else {
            return docService.move(new DocRef(src), new DocRef(targetParent),
                    docName);
        }
    }

    /**
     * Retrieves children of a Document
     * 
     * @param docRef Reference of the parent Document
     * @return a Documents List
     * @throws Exception
     */
    @Processor
    public Documents getChildren(@Placement(group = "operation parameters")
    String docRef) throws Exception {
        return docService.getChildren(new DocRef(docRef));
    }

    /**
     * Get a children
     * 
     * @param docRef reference of the parent Document
     * @param docName name of the child to fetch
     * @return a Document Objects
     * @throws Exception
     */
    @Processor
    public Document getChild(@Placement(group = "operation parameters")
    String docRef, @Placement(group = "operation parameters")
    String docName) throws Exception {
        return docService.getChild(new DocRef(docRef), docName);
    }

    /**
     * Get Parent Document
     * 
     * @param docRef reference of the Document
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document getParent(@Placement(group = "operation parameters")
    String docRef) throws Exception {
        return docService.getParent(new DocRef(docRef));
    }

    /**
     * Runs a NXQL Query against repository
     * 
     * @param query NXQL Query
     * @return a Documents List
     * @throws Exception
     */
    @Processor
    public Documents query(@Placement(group = "operation parameters")
    String query) throws Exception {
        return docService.query(query);
    }

    /**
     * Set Permission
     * 
     * @param doc reference of the target Document
     * @param user username or groupname to give permission to
     * @param permission permissionname
     * @param acl ACL
     * @param granted grant/deny flag
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document setPermission(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String user, @Placement(group = "operation parameters")
    String permission, @Placement(group = "operation parameters")
    String acl, @Placement(group = "operation parameters")
    boolean granted) throws Exception {
        return docService.setPermission(new DocRef(doc), user, permission, acl,
                granted);
    }

    /**
     * Removes an ACL
     * 
     * @param doc reference of the target Document
     * @param acl ACL
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document removeAcl(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String acl) throws Exception {
        return docService.removeAcl(new DocRef(doc), acl);
    }

    /**
     * Set Lifecycle State
     * 
     * @param doc reference to the target Document
     * @param state LifeCycle State
     * @return a Document Object
     * @throws Exceptions
     */
    @Processor
    public Document setState(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String state) throws Exception {
        return docService.setState(new DocRef(doc), state);

    }

    /**
     * Locks a Document
     * 
     * @param doc target Document
     * @param lock lock info (can be null)
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document lock(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String lock) throws Exception {
        if (lock == null || lock.isEmpty()) {
            return docService.lock(new DocRef(doc));
        } else {
            return docService.lock(new DocRef(doc), lock);
        }
    }

    /**
     * Unlocks a Document
     * 
     * @param doc reference to the target Document
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document unlock(@Placement(group = "operation parameters")
    String doc) throws Exception {
        return docService.unlock(new DocRef(doc));
    }

    /**
     * Change a property on a Document
     * 
     * @param doc reference to the target Document
     * @param key property Name
     * @param value property Value
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document setProperty(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String key, @Placement(group = "operation parameters")
    String value) throws Exception {
        return docService.setProperty(new DocRef(doc), key, value);
    }

    /**
     * Remove a Property on a Document
     * 
     * @param doc reference to the target Document
     * @param key property name
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document removeProperty(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String key) throws Exception {
        return docService.removeProperty(new DocRef(doc), key);
    }

    /**
     * Updates a Document
     * 
     * @param doc reference to the target Document
     * @param properties Map of properties to set on document
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document update(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    Map<String, Object> properties) throws Exception {
        return docService.update(new DocRef(doc), new PropertyMap(properties));
    }

    /**
     * Publish a Document
     * 
     * @param doc reference to the target Document
     * @param section reference of the publish target
     * @param override flag to control override
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document publish(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String section, @Placement(group = "operation parameters")
    @Optional
    @Default("false")
    boolean override) throws Exception {
        return docService.publish(new DocRef(doc), new DocRef(section),
                override);
    }

    /**
     * Create a Relation
     * 
     * @param subject reference to the target Document
     * @param predicate predicate of the relation
     * @param object reference on the target related Document
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document createRelation(@Placement(group = "operation parameters")
    String subject, @Placement(group = "operation parameters")
    String predicate, @Placement(group = "operation parameters")
    DocRef object) throws Exception {
        return docService.createRelation(new DocRef(subject), predicate, object);
    }

    /**
     * get Relations
     * 
     * @param doc reference to the target Document
     * @param predicate predicate to search for
     * @param outgoing flag to indicate of relations processed must be outgoing
     *            or incoming
     * @return list of linked Document Objects
     * @throws Exception
     */
    @Processor
    public Documents getRelations(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    String predicate, @Placement(group = "operation parameters")
    boolean outgoing) throws Exception {
        return docService.getRelations(new DocRef(doc), predicate, outgoing);
    }

    /**
     * Attach a Blob to a Document
     * 
     * @param doc reference to the target Document
     * @param blob Blob to attach
     * @param xpath Xpath of the target property
     * @throws Exception
     */
    @Processor
    public void setBlob(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    FileBlob blob, @Optional
    @Default("")
    @Placement(group = "operation parameters")
    String xpath) throws Exception {

        if (xpath == null || xpath.isEmpty()) {
            docService.setBlob(new DocRef(doc), blob);
        } else {
            docService.setBlob(new DocRef(doc), blob, xpath);
        }
    }

    /**
     * Remove a Blob from a Document
     * 
     * @param doc reference to the target Document
     * @param xpath xpath of the target Blob
     * @throws Exception
     */
    @Processor
    public void removeBlob(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String xpath) throws Exception {

        if (xpath == null || xpath.isEmpty()) {
            docService.removeBlob(new DocRef(doc));
        } else {
            docService.removeBlob(new DocRef(doc), xpath);
        }
    }

    /**
     * get the Blob associated to a Document
     * 
     * @param doc reference to the target Document
     * @param xpath xpath of the target Blob
     * @return a FileBlob object
     * @throws Exception
     */
    @Processor
    public FileBlob getBlob(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String xpath) throws Exception {

        if (xpath == null || xpath.isEmpty()) {
            return docService.getBlob(new DocRef(doc));
        } else {
            return docService.getBlob(new DocRef(doc), xpath);
        }
    }

    /**
     * Creates a version
     * 
     * @param doc reference to the target Document
     * @param increment increment policy (minor/major)
     * @return a Document Object
     * @throws Exception
     */
    @Processor
    public Document createVersion(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String increment) throws Exception {

        if (increment == null || increment.isEmpty()) {
            return docService.createVersion(new DocRef(doc));
        } else {
            return docService.createVersion(new DocRef(doc), increment);
        }
    }

    /**
     * Fire an Event
     * 
     * @param event name of the event to raise
     * @param doc reference to the document to attach to the event
     * @throws Exception
     */
    @Processor
    public void fireEvent(@Placement(group = "operation parameters")
    String event, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String doc) throws Exception {
        if (doc == null || doc.isEmpty()) {
            docService.fireEvent(event);
        } else {
            docService.fireEvent(new DocRef(doc), event);
        }
    }

}