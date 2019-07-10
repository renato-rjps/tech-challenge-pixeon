package br.com.rjps.pixeon.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
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

	@OneToMany(mappedBy = "healthcareInstitution", fetch = LAZY, cascade = ALL)
	public Set<Exam> exams;

}
