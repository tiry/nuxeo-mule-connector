package org.nuxeo.mule;

import java.util.Map;

import org.mule.api.annotations.Category;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.InboundHeaders;
import org.mule.api.annotations.param.Optional;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.OperationInput;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.mule.blob.NuxeoBlob;
import org.nuxeo.mule.mapper.MapUnmangler;

/**
 * Stupid code to Map Operation already hard-coded in DocumentService.
 *
 * This class could typically be generated from a list of Operation !
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class BaseDocumentService {

    public static final String AUTOMATION_CONTEXT_KEY = "automationContext";

    protected DocumentService docService;

    protected Session session;

    public BaseDocumentService() {
        super();
    }

    /**
     * Get a Document from Nuxeo repository
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-document}
     *
     * @param docRef the DocumentRef
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Processor
    @Category(name = "CRUD", description = "fetch a Document")
    public Document getDocument(@Placement(group = "operation parameters")
    @FriendlyName("Document Reference (docRef)")
    String docRef) throws Exception {
        return docService.getDocument(docRef);
    }

    /**
     * Get the root Document of Nuxeo Repository
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:get-root-document}
     *
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Category(name = "CRUD", description = "fetch the Root Document")
    @Processor
    public Document getRootDocument() throws Exception {
        return getDocument("/");
    }

    /**
     * Create a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:create-document}
     *
     * @param parentRef reference of the Parent document
     * @param docType Document Type
     * @param docName name of the target Document
     * @param properties Metadata
     * @return a Document Object
     * @throws Exception if operation can not be executed
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

        PropertyMap map = MapUnmangler.unMangle(properties);
        return docService.createDocument(new DocRef(parentRef), docType,
                docName, map);
    }

    /**
     * Deletes a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:remove}
     *
     * @param ref reference of the Document to delete
     * @throws Exception if operation can not be executed
     */
    @Processor
    public void remove(@Placement(group = "operation parameters")
    String ref) throws Exception {
        docService.remove(ref);
    }

    /**
     * Copy a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:copy}
     *
     * @param src reference of the source document
     * @param targetParent reference of the destination document
     * @param docName name of the copied document
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:move}
     *
     * @param src the reference of the document to move
     * @param targetParent the reference of thr target parent
     * @param docName the name of the document after move
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-children}
     *
     * @param docRef Reference of the parent Document
     * @return a Documents List
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Documents getChildren(@Placement(group = "operation parameters")
    String docRef) throws Exception {
        return docService.getChildren(new DocRef(docRef));
    }

    /**
     * Get a child by it's name from a container
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-child}
     *
     * @param docRef reference of the parent Document
     * @param docName name of the child to fetch
     * @return a Document Objects
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-parent}
     *
     * @param docRef reference of the Document
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document getParent(@Placement(group = "operation parameters")
    String docRef) throws Exception {
        return docService.getParent(new DocRef(docRef));
    }

    /**
     * Set Permission
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:set-permission}
     *
     * @param doc reference of the target Document
     * @param user username or groupname to give permission to
     * @param permission permissionname
     * @param acl ACL
     * @param granted grant/deny flag
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:remove-acl}
     *
     * @param doc reference of the target Document
     * @param acl ACL
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:set-state}
     *
     * @param doc reference to the target Document
     * @param state LifeCycle State
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:lock}
     *
     * @param doc target Document
     * @param lock lock info (can be null)
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:unlock}
     *
     * @param doc reference to the target Document
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document unlock(@Placement(group = "operation parameters")
    String doc) throws Exception {
        return docService.unlock(new DocRef(doc));
    }

    /**
     * Change a property on a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:set-property}
     *
     * @param doc reference to the target Document
     * @param key property Name
     * @param value property Value
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:remove-property}
     *
     * @param doc reference to the target Document
     * @param key property name
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:update}
     *
     * @param doc reference to the target Document
     * @param properties Map of properties to set on document
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document update(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    Map<String, Object> properties) throws Exception {
        return docService.update(new DocRef(doc),
                MapUnmangler.unMangle(properties));
    }

    /**
     * Updates a Document with DataSense
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:updateds}
     *
     * @param type the doc type
     * @param doc reference to the target Document
     * @param properties Map of properties to set on document
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    /*
     * @Processor public Document updateds(@MetaDataKeyParam @Placement(group =
     * "operation parameters") @FriendlyName("doc type") String type,
     * @Placement(group = "operation parameters") String doc, @Placement(group =
     * "operation parameters") Map<String, Object> properties) throws Exception
     * { return docService.update(new DocRef(doc), new PropertyMap(properties));
     * }
     */

    /**
     * Publish a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:publish}
     *
     * @param doc reference to the target Document
     * @param section reference of the publish target
     * @param override flag to control override
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:create-relation}
     *
     * @param subject reference to the target Document
     * @param predicate predicate of the relation
     * @param object reference on the target related Document
     * @return a Document Object
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document createRelation(@Placement(group = "operation parameters")
    String subject, @Placement(group = "operation parameters")
    String predicate, @Placement(group = "operation parameters")
    DocRef object) throws Exception {
        return docService.createRelation(new DocRef(subject), predicate, object);
    }

    /**
     * get Relations on the target Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-relations}
     *
     * @param doc reference to the target Document
     * @param predicate predicate to search for
     * @param outgoing flag to indicate of relations processed must be outgoing
     *            or incoming
     * @return list of linked Document Objects
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:set-blob}
     *
     * @param doc reference to the target Document
     * @param blob Blob to attach
     * @param xpath Xpath of the target property
     * @throws Exception if operation can not be executed
     */
    @Processor
    public void setBlob(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    NuxeoBlob blob, @Optional
    @Default("")
    @Placement(group = "operation parameters")
    String xpath) throws Exception {

        OperationRequest req = session.newRequest(docService.SetBlob).setInput(
                blob).set("document", doc);
        if (xpath != null) {
            req.set("xpath", xpath);
        }
        req.setHeader(Constants.HEADER_NX_VOIDOP, "true");
        req.execute();
    }

    /**
     * Remove a Blob from a Document
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:remove-blob}
     *
     * @param doc reference to the target Document
     * @param xpath xpath of the target Blob
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:get-blob}
     *
     * @param doc reference to the target Document
     * @param xpath xpath of the target Blob
     * @return a FileBlob object
     * @throws Exception if operation can not be executed
     */
    @Processor
    public NuxeoBlob getBlob(@Placement(group = "operation parameters")
    String doc, @Placement(group = "operation parameters")
    @Optional
    @Default("")
    String xpath) throws Exception {
        OperationRequest req = session.newRequest(docService.GetBlob).setInput(
                doc);
        if (xpath != null) {
            req.set("xpath", xpath);
        }
        return new NuxeoBlob((Blob) req.execute());
    }

    /**
     * Creates a version
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:create-version}
     *
     * @param doc reference to the target Document
     * @param increment increment policy (minor/major)
     * @return a Document Object
     * @throws Exception if operation can not be executed
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
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:fire-event}
     *
     * @param event name of the event to raise
     * @param doc reference to the document to attach to the event
     * @throws Exception if operation can not be executed
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

    protected OperationInput getInput(Object blobOrDoc) {
        OperationInput in = null;
        if (blobOrDoc instanceof String) {
            in = DocRef.newRef((String) blobOrDoc);
        } else if (blobOrDoc instanceof Document) {
            in = DocRef.newRef(((Document) blobOrDoc).getId());
        } else if (blobOrDoc instanceof Blob) {
            in = (Blob) blobOrDoc;
        }
        return in;
    }

    /**
     * convert the input Blob as PDF
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:blob-to-pdf}
     *
     * @param blobOrDoc blob or Document to convert
     * @return the PDF conversion
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public NuxeoBlob blobToPdf(@Placement(group = "operation parameters")
    Object blobOrDoc) throws Exception {
        OperationRequest req = session.newRequest("Blob.ToPDF").setInput(
                getInput(blobOrDoc));
        return new NuxeoBlob((Blob) req.execute());
    }

    /**
     * resize the input Blob Picture
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:resize-picture}
     *
     * @param blobOrDoc blob or document that contains the picture
     * @param maxWidth target maximum Width in pixels
     * @param maxHeight target maximum Height in pixels
     *
     * @return the resized Picture
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public NuxeoBlob resizePicture(@Placement(group = "operation parameters")
    Object blobOrDoc, @Placement(group = "operation parameters")
    @Optional
    Integer maxWidth, @Placement(group = "operation parameters")
    @Optional
    Integer maxHeight) throws Exception {
        OperationRequest req = session.newRequest("Picture.resize").setInput(
                getInput(blobOrDoc));
        if (maxWidth != null) {
            req.set("maxWidth", maxWidth);
        }
        if (maxHeight != null) {
            req.set("maxHeight", maxHeight);
        }
        return new NuxeoBlob((Blob) req.execute());
    }

    /**
     * create a Document from a file
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:import-file}
     *
     * @param blobOrDoc blob or document that contains the picture
     * @param overwrite define if existing document should be overriden
     * @param inbound inbound headers that can hold some automation Context info
     *
     * @return the created Document
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document importFile(@Placement(group = "operation parameters")
    NuxeoBlob blobOrDoc, @Placement(group = "operation parameters")
    @Optional
    @Default("false")
    boolean overwrite, @InboundHeaders("*")
    final Map<String, Object> inbound) throws Exception {

        OperationRequest req = session.newRequest("FileManager.Import").setInput(
                getInput(blobOrDoc));
        propagateAutomationContext(inbound, req);
        // XXX typo !
        req.set("overwite", overwrite);
        return (Document) req.execute();
    }

    /**
     *
     * Starts the workflow with the given model id on the input documents
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:start-workflow}
     *
     * @param docRef target Document the Workflow should be started on
     * @param workflowId uuid of the workflow to be started
     * @param start start flag
     * @param wfVars workflow variables
     *
     * @return the doc
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document startWorkflow(@Placement(group = "operation parameters")
    String docRef, @Placement(group = "operation parameters")
    String workflowId, @Placement(group = "operation parameters")
    @Optional
    @Default("false")
    boolean start, @Placement(group = "operation parameters")
    @Optional
    Map<String, Object> wfVars) throws Exception {

        OperationRequest req = session.newRequest("Context.StartWorkflow").setInput(
                DocRef.newRef(docRef));
        req.set("Id", workflowId);
        req.set("start", start);
        req.set("variables", MapUnmangler.unMangle(wfVars));
        return (Document) req.execute();
    }

    /**
     * Cancel the workflow with the given id,
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample
     * nuxeo:cancel-workflow}
     *
     * @param workflowId the id of the Document that represents the target
     *            Workflow
     *
     * @return the input Doc
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document cancelWorkflow(@Placement(group = "operation parameters")
    String workflowId) throws Exception {
        OperationRequest req = session.newRequest("Context.CancelWorkflow");
        req.set("Id", workflowId);
        return (Document) req.execute();
    }

    /**
     * Completes the input task document.
     *
     * {@sample.xml ../../../doc/Nuxeo-connector.xml.sample nuxeo:complete-task}
     *
     * @param docRef target Task document
     * @param comment optional comment
     * @param status optional status
     *
     * @return the task document
     *
     * @throws Exception if operation can not be executed
     */
    @Processor
    public Document completeTask(@Placement(group = "operation parameters")
    String docRef, @Placement(group = "operation parameters")
    @Optional
    String comment, @Placement(group = "operation parameters")
    @Optional
    String status) throws Exception {
        OperationRequest req = session.newRequest(
                "Workflow.CompleteTaskOperation").setInput(
                DocRef.newRef(docRef));
        if (comment != null) {
            req.set("comment", comment);
        }
        if (status != null) {
            req.set("status", status);
        }
        return (Document) req.execute();
    }

    protected void propagateAutomationContext(Map<String, Object> inbound,
            OperationRequest req) {
        if (inbound != null && inbound.containsKey(AUTOMATION_CONTEXT_KEY)) {
            Map<String, Object> ctx = (Map<String, Object>) inbound.get(AUTOMATION_CONTEXT_KEY);
            for (String key : ctx.keySet()) {
                req.setContextProperty(key, ctx.get(key));
            }
        }
    }

}