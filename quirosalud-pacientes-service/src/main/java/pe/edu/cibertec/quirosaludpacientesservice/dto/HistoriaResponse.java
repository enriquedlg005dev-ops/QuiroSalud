package pe.edu.cibertec.quirosaludpacientesservice.dto;

import pe.edu.cibertec.quirosaludpacientesservice.model.HistoriaClinica;
public record HistoriaResponse(
        String motivoConsulta, Integer escalaDolor, String antecedenteTrauma,
        String antecedenteCirugia, boolean tieneAsma, boolean tieneDiabetes,
        boolean tieneAlergias, String observaciones) {
    public static HistoriaResponse from(HistoriaClinica h) {
        return new HistoriaResponse(h.getMotivoConsulta(), h.getEscalaDolor(), h.getAntecedenteTrauma(),
                h.getAntecedenteCirugia(), h.isTieneAsma(), h.isTieneDiabetes(), h.isTieneAlergias(), h.getObservaciones());
    }
}
