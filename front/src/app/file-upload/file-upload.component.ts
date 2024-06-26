import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UtilService } from '../services/util.service';
import { LambdaService } from '../services/lambda.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FileService } from '../services/file.service';
import { Router } from '@angular/router';
import { fileDescriptionRegexValidator, fileNameRegexValidator } from '../validators/file/fileValidator';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  form = new FormGroup({
    name: new FormControl('', [Validators.required, fileNameRegexValidator]),
    description: new FormControl('', [Validators.required, fileDescriptionRegexValidator]),
  }, [])

  tags: String[] = []
  size: String = '0kb'

  right_part_visible: boolean = false;

  constructor(private http: HttpClient, private utilService: UtilService,
    private fileService: FileService,
    private router: Router,
    private snackBar: MatSnackBar) { }

  profileImgPath: string = "";
  file: File = {} as File;
  path: string = '';

  ngOnInit(): void {
    this.utilService.recieveCurrentPath().subscribe((value) => {
      this.path = value;
    })
  }

  onImageSelect(event: any){
    if (event.target.files){
      var reader = new FileReader();
      this.file = event.target.files[0];
      if (this.file.size/1024 > 5000){
        this.snackBar.open("File is too big! Try again with smaller one!", "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        });
      } else {
        reader.readAsDataURL(this.file);
        reader.onload=(e: any)=>{
          this.profileImgPath = reader.result as string;
        }
        this.form.controls['name'].setValue(this.file.name);
        this.size = Math.round(this.file.size/1024).toString() + 'kB';
        this.right_part_visible = true;
      }
    }
  }


  save(): any{
    this.add().subscribe((res: any) => {
    });
    this.edit().subscribe((res: any) => {
    }) 
  }

  add(): Observable<any> {
    const options: any = {
      responseType: 'json',
    };
    
    return this.http.post<any>(environment.apiGateway + "/file?filename=" + this.path + this.form.value.name, this.profileImgPath, options);
  }

  edit(): Observable<any>{
    const options: any = {
      responseType: 'json',
    };
    let o = {
      id: this.path + this.form.value.name,
      name: this.form.value.name,
      lastModified:  new Date().toISOString().split('T')[0],
      type: this.file.type, 
      size: this.file.size,
      createdAt: new Date().toISOString().split('T')[0],
      description: this.form.value.description,
      tags: this.tags
    }
    return this.http.post<any>(environment.apiGateway + '/metadata', o, options);
  }

  createFile() {
    if (!this.form.valid)
      return;
    let o = {
      name: this.form.value.name,
      lastModified:  new Date().toISOString().split('T')[0],
      // type: this.file.type, 
      size: this.file.size,
      createdAt: new Date().toISOString().split('T')[0],
      description: this.form.value.description,
      // tags: this.tags,
      content: this.profileImgPath
    }
    this.fileService.createFile(o).subscribe({
      next: (value: any)  => {
        this.snackBar.open("Successfully created file!", "", {
          duration: 2700, panelClass: ['snack-bar-success']
        });
        this.router.navigate(['/homepage']);
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        });
      },
    })
  }
}