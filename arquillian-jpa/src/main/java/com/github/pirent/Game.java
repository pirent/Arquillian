package com.github.pirent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	
	@SuppressWarnings("unused")
	private Game() {}
	
	public Game(String title) {
		this.title = title;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	@Size(min = 3, max = 50)
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Game.class.getSimpleName()).append("@").append(this.hashCode());
		sb.append("[id = ").append(id).append("; title = ").append(title).append("]");
		
		return sb.toString();
	}
}
