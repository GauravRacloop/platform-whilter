package com.whilter.audit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317210794718801783L;
	@Id
	protected String id;
	@Indexed
	@CreatedDate
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date createdDate;
	@Indexed
	@CreatedBy
	protected String createdBy;
	@Indexed
	@LastModifiedDate
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date updatedDate;
	@Indexed
	@LastModifiedBy
	protected String updatedBy;
	@Indexed
	protected Status status = Status.ACTIVE;
	@Version
	protected Long version;
	
	public AbstractEntity() {
		this.createdDate = new Date();
		this.createdBy = "SYSTEM";
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public enum Status {
		ACTIVE, INACTIVE
	}
}
