package com.example.controller;

import com.example.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.resteasy.reactive.RestQuery;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/task")
public class TaskController {
    @GET
    public List<Task> getAll(@RestQuery() String developer){
        if(developer == null){
            return Task.listAll();
        }
        return Task.list("developer", developer);
    }
    @Path("/{id}")
    @GET
    public Response getById(Long id){
        return Task.findByIdOptional(id).map(task -> Response.ok(task).build())
                .orElse(Response.status(Response.Status.NO_CONTENT).build());
    }
    @POST
    @Transactional
    public void create(Task task){
        Task.persist(task);
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    public void deleteById(Long id){
        Task.deleteById(id);
    }
    @GET
    @Path("/developer/{developer}")
    public Response findByProject(String developer){
        return Task.findByDeveloper(developer)
                .map(task -> Response.ok(task).build())
                .orElse(Response.status(Response.Status.NO_CONTENT).build());
    }

}
