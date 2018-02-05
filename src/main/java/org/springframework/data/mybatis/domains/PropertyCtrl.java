package org.springframework.data.mybatis.domains;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class PropertyCtrl<PK extends Serializable> implements Persistable<PK>  {
	
	@Override
	public boolean isNew() {
		return saveable;
	}
	private boolean saveable;
		
	@Transient
	public boolean isSaveable() {
		return saveable;
	}
	public void setSaveable(boolean saveable) {
		this.saveable = saveable;
	}
}
