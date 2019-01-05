package edu.mit.adml.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a digital item
 */
@Entity
@Table(name = "ADML") // TODO
public class Item {

    @Transient
    private long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long itemId;

    @Column(name = "Refid")
    private String refid;

    @Column(name = "Format")
    private String format;

    @Column(name = "Transfer_Status")
    private String transferStatus;

    @Column(name = "Transfer_Method")
    private String transferMethod;

    @Column(name = "Transfer_Date")
    private Date transferDate = new Date();

    @Column(name = "Disposition")
    private String disposition;

    @Column(name = "Created")
    private Date created = new Date();

    @Column(name = "Updated")
    private Date updated = new Date();

    @Column(name = "Resource")
    private String resource;

    @Column(name = "Component")
    private String component;

    @Column(name = "Box")
    private String box;

    private String content;

    public Item() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", refid='" + refid + '\'' +
                ", format='" + format + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", transferMethod='" + transferMethod + '\'' +
                ", transferDate=" + transferDate +
                ", disposition='" + disposition + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", resource='" + resource + '\'' +
                ", component='" + component + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
