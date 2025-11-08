package com.visuall.controller;

import com.visuall.service.AuthService;
import com.visuall.model.dto.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // CORREÇÃO: Acesse os campos diretamente (não use getters)
            AuthResponse response = authService.login(request.email, request.senha);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(401).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            // CORREÇÃO: Acesse os campos diretamente
            AuthResponse response = authService.register(
                    request.nome,
                    request.email,
                    request.senha
            );
            return Response.status(201).entity(response).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}