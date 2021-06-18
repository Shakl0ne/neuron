package com.wanmeizhensuo.http;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Properties;

@Path("/doctor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorResource {

    String syncRecord;

    @GET
    public String get() {
        return syncRecord;
    }

    @POST
    public String post(String syncRecord) {
        this.syncRecord = syncRecord;
        return syncRecord;
    }
}
