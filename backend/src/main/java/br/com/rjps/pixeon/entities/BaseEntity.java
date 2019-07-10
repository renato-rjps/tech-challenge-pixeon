package br.com.rjps.pixeon.entities;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@MappedSuperclass
public class BaseEntity {

	private static final String PRIMARY_KEY = "PRIMARY_KEY";

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = PRIMARY_KEY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
}
