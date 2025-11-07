package com.visuall.controller;

import com.visuall.service.AuthService;
import com.visuall.model.dto.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // Corrigido: usando getEmail() e getSenha()
            AuthResponse response = authService.login(request.getEmail(), request.getSenha());
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(401).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            // Corrigido: usando getNome(), getEmail() e getSenha()
            AuthResponse response = authService.register(
                    request.getNome(),
                    request.getEmail(),
                    request.getSenha()
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