package pe.edu.cibertec.quirosaludpacientesservice.dto;

import jakarta.validation.constraints.*;
public record HistoriaRequest(
    @Size(max = 500) String motivoConsulta,
    @Min(0) @Max(10) Integer escalaDolor,
    @Size(max = 500) String antecedenteTrauma,
    @Size(max = 500) String antecedenteCirugia,
    boolean tieneAsma, boolean tieneDiabetes, boolean tieneAlergias,
    @Size(max = 1000) String observaciones
) {}
