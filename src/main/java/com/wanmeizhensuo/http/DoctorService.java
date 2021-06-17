package com.wanmeizhensuo.http;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.wanmeizhensuo.streams.flow.ColumnDefine;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Path("/doctors")
@RegisterRestClient(baseUri = "http://localhost:8080", configKey = "orkey")
public interface DoctorService {
    Set<ColumnDefine> defines = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    @POST
    @Produces("application/json")
    default Set<ColumnDefine> postSync(ColumnDefine define) {
        defines.add(define);
        return defines;
    };

}
