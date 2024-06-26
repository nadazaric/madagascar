import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UtilService } from './util.service';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  path: string = '';

  constructor(private http: HttpClient, private utilService: UtilService) {
    this.utilService.recieveCurrentPath().subscribe((value) => {
      this.path = value;
    })
  }

  createFile(file: any): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.post<any>(environment.apiHost + "/file", file, options);
  }

  getFilesByUser(): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.get<any>(environment.apiHost + "/file/user", options);
  }

  getSharedWith(fileId: number): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.get<any>(environment.apiHost + "/acl/shared-with/" + fileId, options);
  }

  shareWithOther(acl: any): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.post<any>(environment.apiHost + "/acl", acl, options);
  }

  updateShareWith(acl: any): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.put<any>(environment.apiHost + "/file/share", acl, options);
  }

  deleteShareWith(acl: any): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    return this.http.delete<any>(environment.apiHost + "/file/share", acl);
  }

}

export interface FileDTO {
  size: number;
  createdAt: Date;
  id: number;
  lastModified: Date;
  ownerId: number;
  description: string;
  name: string;
}

export interface ACLDTO {
  user: string,
  relation: string,
  fileId: number
}

export interface SharedWithDTO {
  user: string,
  relation: string
}