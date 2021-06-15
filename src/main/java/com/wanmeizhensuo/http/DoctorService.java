package com.wanmeizhensuo.http;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.wanmeizhensuo.streams.flow.WorkFlow;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/doctors")
@RegisterRestClient(baseUri = "http://localhost:8080", configKey = "orkey")
public interface DoctorService {

    @POST
    @Produces("application/json")
    Uni<WorkFlow> postSync(WorkFlow flow);

}
