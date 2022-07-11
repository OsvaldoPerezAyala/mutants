package com.osvaldo;

import com.osvaldo.repository.Repository;
import com.osvaldo.service.StatsService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stats")
public class StatsController {

    @Inject
    StatsService statsService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute() {

        return Response.status(Response.Status.OK).entity(statsService.getDNAStats()).build();
    }
}
