package com.ku6.cdn.dispatcher.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.ku6.cdn.dispatcher.common.entity.PersistenceObject;


@Entity
@IdClass(HostSpeed.class)
@Table(name = "t_host_speed")
public class HostSpeed extends PersistenceObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6059237731292165987L;
	@Id
	private Long srcId;
	@Id
	private Long destId;
	private Long speed;
	
	public HostSpeed() {}

	@Column(name = "src_id", length = 11, nullable = false, unique = true)
	public Long getSrcId() {
		return srcId;
	}

	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}

	@Column(name = "dest_id", length = 11, nullable = false, unique = true)
	public Long getDestId() {
		return destId;
	}

	public void setDestId(Long destId) {
		this.destId = destId;
	}

	@Column(name = "speed", length = 11, nullable = false, unique = false)
	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

}
