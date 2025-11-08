package com.visuall.controller;

import com.visuall.service.LembreteService;
import com.visuall.model.dto.LembreteRequestDTO; // IMPORT ADICIONADO
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/lembretes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LembreteController {

    LembreteService lembreteService = new LembreteService();

    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarLembretes(@PathParam("usuarioId") Integer usuarioId) {
        try {
            var lembretes = lembreteService.listarPorUsuario(usuarioId);
            return Response.ok(lembretes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response criarLembrete(LembreteRequest request) {
        try {
            // CONVERTE LembreteRequest para LembreteRequestDTO
            LembreteRequestDTO requestDTO = new LembreteRequestDTO();
            requestDTO.setNomeMedico(request.getNomeMedico());
            requestDTO.setDataConsulta(LocalDate.parse(request.getDataConsulta())); // Converte String para LocalDate
            requestDTO.setHoraConsulta(request.getHoraConsulta());
            requestDTO.setObservacoes(request.getObservacoes());
            requestDTO.setUsuarioId(request.getUsuarioId());

            var lembrete = lembreteService.criarLembrete(requestDTO);
            return Response.ok(lembrete).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarLembrete(@PathParam("id") Integer id, LembreteRequest request) {
        try {
            // CONVERTE LembreteRequest para LembreteRequestDTO
            LembreteRequestDTO requestDTO = new LembreteRequestDTO();
            requestDTO.setNomeMedico(request.getNomeMedico());
            requestDTO.setDataConsulta(LocalDate.parse(request.getDataConsulta())); // Converte String para LocalDate
            requestDTO.setHoraConsulta(request.getHoraConsulta());
            requestDTO.setObservacoes(request.getObservacoes());
            requestDTO.setUsuarioId(request.getUsuarioId());

            var lembrete = lembreteService.atualizarLembrete(id, requestDTO);
            return Response.ok(lembrete).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluirLembrete(@PathParam("id") Integer id) {
        try {
            lembreteService.excluirLembrete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    // Classe interna do Controller (mantida para receber JSON)
    public static class LembreteRequest {
        public String nomeMedico;
        public String dataConsulta; // Mantém como String para receber do JSON
        public String horaConsulta;
        public String observacoes;
        public Integer usuarioId;

        public String getNomeMedico() { return nomeMedico; }
        public String getDataConsulta() { return dataConsulta; }
        public String getHoraConsulta() { return horaConsulta; }
        public String getObservacoes() { return observacoes; }
        public Integer getUsuarioId() { return usuarioId; }
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}