package com.visuall.controller;

import com.visuall.service.AuthService;
import com.visuall.model.dto.LoginRequest;
import com.visuall.model.dto.RegisterRequest;
import com.visuall.model.dto.AuthResponse;
import com.visuall.exception.BusinessException;

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
            AuthResponse response = authService.login(request.getCpf(), request.getSenha());
            return Response.ok(response).build();
        } catch (BusinessException e) {
            return Response.status(401).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            AuthResponse response = authService.register(
                    request.getCpf(),
                    request.getSenha(),
                    request.getIdPaciente()
            );
            return Response.status(201).entity(response).build();
        } catch (BusinessException e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}