package com.gps.pruebaTecnica.shared.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DocumentTypeEnum {
    CC("CC", "Cédula de Ciudadanía"),
    TI("TI", "Tarjeta de Identidad"),
    CE("CE", "Cédula de Extranjería"),
    PA("PA", "Pasaporte"),
    NIT("NIT", "Número de Identificación Tributaria"),
    RUT("RUT", "Registro Único Tributario"),
    PE("PE", "Permiso Especial de Permanencia"),
    RC("RC", "Registro Civil de Nacimiento"),
    PEP("PEP", "Permiso Especial de Permanencia"),
    DNE("DNE", "Documento Nacional de Identificación Extranjera");

    private final String code;
    private final String description;

    DocumentTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static DocumentTypeEnum fromValue(String code) {
        for (DocumentTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Código de documento inválido: " + code);
    }
}
