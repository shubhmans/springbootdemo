package com.envision.demo.dao;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseDao {
	
	@Column(updatable = false)
	@CreationTimestamp
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;
	
    @UpdateTimestamp
    @LastModifiedDate
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date modifiedAt;
}
