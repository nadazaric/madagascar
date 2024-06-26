import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
providedIn: 'root'
    })
    export class UserService {

    constructor(private httpClient : HttpClient) { }

    register(registerDTO : any) :Observable<void> {
        return this.httpClient.post<void>(environment.apiHost + "/user/register", registerDTO);
    }
}