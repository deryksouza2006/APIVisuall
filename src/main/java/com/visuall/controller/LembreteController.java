package com.visuall.controller;

import com.visuall.service.LembreteService;
import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.model.dto.LembreteResponseDTO;
import com.visuall.model.dto.EspecialistaDTO;
import com.visuall.model.dto.LocalAtendimentoDTO;
import com.visuall.exception.BusinessException;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LembreteController {

    @Inject
    LembreteService lembreteService;

    @GET
    @Path("/teste")
    @Produces(MediaType.TEXT_PLAIN)
    public String teste() {
        return "✅ API VisuAll funcionando!";
    }

    @GET
    @Path("/pacientes/{pacienteId}/lembretes")
    public Response listarLembretesPorPaciente(@PathParam("pacienteId") Integer pacienteId) {
        try {
            List<LembreteResponseDTO> lembretes = lembreteService.listarLembretesPorPaciente(pacienteId);
            return Response.ok(lembretes).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Path("/pacientes/{pacienteId}/lembretes/ativos")
    public Response listarLembretesAtivos(@PathParam("pacienteId") Integer pacienteId) {
        try {
            List<LembreteResponseDTO> lembretes = lembreteService.listarLembretesAtivosPorPaciente(pacienteId);
            return Response.ok(lembretes).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Path("/lembretes/{id}")
    public Response buscarLembretePorId(@PathParam("id") Integer id) {
        try {
            LembreteResponseDTO lembrete = lembreteService.buscarLembretePorId(id);
            return Response.ok(lembrete).build();
        } catch (BusinessException e) {
            return Response.status(404).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/lembretes")
    public Response criarLembrete(LembreteRequestDTO request) {
        try {
            LembreteResponseDTO novoLembrete = lembreteService.criarLembrete(request);
            return Response.status(201).entity(novoLembrete).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/lembretes/{id}")
    public Response atualizarLembrete(@PathParam("id") Integer id, LembreteRequestDTO request) {
        try {
            request.setId(id);
            LembreteResponseDTO atualizado = lembreteService.atualizarLembrete(request);
            return Response.ok(atualizado).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/lembretes/{id}")
    public Response excluirLembrete(@PathParam("id") Integer id) {
        try {
            lembreteService.excluirLembrete(id);
            return Response.ok(new SimpleResponse("Lembrete excluído com sucesso")).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Path("/especialistas")
    public Response listarEspecialistas() {
        try {
            List<EspecialistaDTO> especialistas = lembreteService.listarEspecialistas();
            return Response.ok(especialistas).build();
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorResponse("Erro interno")).build();
        }
    }

    @GET
    @Path("/locais")
    public Response listarLocais() {
        try {
            List<LocalAtendimentoDTO> locais = lembreteService.listarLocais();
            return Response.ok(locais).build();
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorResponse("Erro interno")).build();
        }
    }

  
    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    public static class SimpleResponse {
        public String message;
        public SimpleResponse(String message) { this.message = message; }
    }
}