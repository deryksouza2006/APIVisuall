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

@Path("/api/lembretes")
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
    @Path("/usuario/{usuarioId}")
    public Response listarLembretes(@PathParam("usuarioId") Integer usuarioId) {
        try {
            List<LembreteResponseDTO> lembretes = lembreteService.listarPorUsuario(usuarioId);
            return Response.ok(lembretes).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/ativos")
    public Response listarLembretesAtivos(@PathParam("usuarioId") Integer usuarioId) {
        try {
            List<LembreteResponseDTO> lembretes = lembreteService.listarAtivosPorUsuario(usuarioId);
            return Response.ok(lembretes).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarLembretePorId(@PathParam("id") Integer id) {
        try {
            LembreteResponseDTO lembrete = lembreteService.buscarLembretePorId(id);
            return Response.ok(lembrete).build();
        } catch (BusinessException e) {
            return Response.status(404).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    public Response criarLembrete(LembreteRequestDTO request) {
        try {
            LembreteResponseDTO novoLembrete = lembreteService.criarLembrete(request);
            return Response.status(201).entity(novoLembrete).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarLembrete(@PathParam("id") Integer id, LembreteRequestDTO request) {
        try {
            LembreteResponseDTO atualizado = lembreteService.atualizarLembrete(id, request);
            return Response.ok(atualizado).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluirLembrete(@PathParam("id") Integer id) {
        try {
            lembreteService.excluirLembrete(id);
            return Response.ok(new SimpleResponse("Lembrete excluído com sucesso")).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("/{id}/concluir")
    public Response marcarComoConcluido(@PathParam("id") Integer id) {
        try {
            lembreteService.marcarComoConcluido(id);
            return Response.ok(new SimpleResponse("Lembrete marcado como concluído")).build();
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