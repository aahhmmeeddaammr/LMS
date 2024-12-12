package com.LMS.LMS.Controllers.ApiResponses;



public abstract class APIResponse {
    public int status;
    public String message;
    public APIResponse(int status ) {
        this.status = status;
        if(status == 200) {
            this.message = "Ok";
        }else if(status == 201) {
            this.message = "Created";
        }
        else if(status == 202) {
            this.message = "Accepted";
        }
        else if(status == 203) {
            this.message = "Non-Authoritative Information";
        }
    }
}
