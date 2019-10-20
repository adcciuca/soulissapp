package it.angelic.soulissclient.model;

import android.content.Context;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.angelic.soulissclient.model.db.SoulissDBLauncherHelper;
import it.angelic.soulissclient.util.LauncherElementEnum;

/**
 * Created by shine@angelic.it on 06/10/2015.
 */
public class LauncherElement implements ILauncherTile, Serializable {
    private LauncherElementEnum componentEnum;
    private String desc;
    private Long id;
    private boolean isFullSpan;
    private ISoulissObject linkedObject;
    private short order;
    private String title;

    public LauncherElement() {
        super();

    }

    public LauncherElement(LauncherElementEnum componentEnum) {
        super();
        this.componentEnum = componentEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LauncherElement that = (LauncherElement) o;

        if (id != null && that.id != null)
            return id.equals(that.id);

        return getComponentEnum().equals(((LauncherElement) o).getComponentEnum());


    }

    public LauncherElementEnum getComponentEnum() {
        return componentEnum;
    }

    public void setComponentEnum(LauncherElementEnum componentEnum) {
        this.componentEnum = componentEnum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    //fa il DB (autoincrement)
    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public
    @Nullable
    ISoulissObject getLinkedObject() {
        return linkedObject;
    }

    public void setLinkedObject(ISoulissObject linkedObject) {
        this.linkedObject = linkedObject;
    }

    public short getOrder() {
        return order;
    }

    public void setOrder(short order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isFullSpan() {
        return isFullSpan;
    }

    protected void persist(Context cnt) throws SoulissModelException {
        SoulissDBLauncherHelper db = new SoulissDBLauncherHelper(cnt);
        db.updateLauncherElement(this);
    }

    public void setIsFullSpan(boolean isFullSpan) {
        this.isFullSpan = isFullSpan;
    }
}
