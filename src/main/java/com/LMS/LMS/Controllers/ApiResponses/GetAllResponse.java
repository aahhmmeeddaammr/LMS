package com.LMS.LMS.Controllers.ApiResponses;

import java.util.Collection;

public class GetAllResponse<T> extends APIResponse {
    public Collection<T> Data;
    public GetAllResponse(int status , Collection<T> data) {
        super(status);
        this.Data = data;
    }
}
