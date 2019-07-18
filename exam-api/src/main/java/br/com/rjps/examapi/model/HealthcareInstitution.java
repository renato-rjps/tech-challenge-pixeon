package br.com.rjps.examapi.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "PRIMARY_KEY", sequenceName = "INSTITUTION_ID_SEQ", allocationSize = 1)
public class HealthcareInstitution extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	@Column(length = 255, nullable = false)
	private String name;

	@CNPJ
	@Size(max = 20)
	@Column(length = 20, nullable = false, unique = true)
	private String cnpj;

	@JsonBackReference()
	@OneToMany(mappedBy = "healthcareInstitution", fetch = LAZY, cascade = ALL)
	private Collection<Exam> exams;
	
	@JsonProperty(access = Access.READ_ONLY)
	@NotNull
	private Integer coins;

}
