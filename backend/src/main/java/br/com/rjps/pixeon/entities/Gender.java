package br.com.rjps.pixeon.entities;

import lombok.Getter;

@Getter
public enum Gender {
	
	MALE("Masculino"),
	FAMALE("Feminino"),
	OTHERS("Outros");
	
	private final String displayName;

	private Gender(String displayName) {
		this.displayName = displayName;
	}

}
