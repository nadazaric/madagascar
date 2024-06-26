import { LambdaService, FileMetaData } from './../services/lambda.service';
import { CognitoService } from './../services/cognito.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { UtilService } from '../services/util.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ShareDialogComponent } from '../share-dialog/share-dialog.component';
import { FileDTO, FileService } from '../services/file.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  files: FileDTO[] = [];
  folders: String[] = [];
  path: string = '';
  navItems: String[] = [];
  loaded = false;
  currentFolderFullPath:String = "";

  constructor(private router: Router,
    private cognitoService: CognitoService,
    private lambdaService: LambdaService,
    private fileService: FileService,
    private utilService: UtilService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.loaded = false;

    this.fileService.getFilesByUser().subscribe(
      (data: FileDTO[]) => {
        this.files = data;
      },
      (error) => {}
    );
    // this.utilService.recieveCurrentPath().subscribe((value) => {
    //   this.setPath(value);
    //   this.readContent();
    // })
  }

  openShareDialog(index: any){
    this.dialog.open(ShareDialogComponent, {
      data: {file: this.files[index]
      }
    });
  }

  setPath(value: string) {
    this.path = value;
    this.navItems = this.path.split("/").slice(0, this.path.split("/").length-1);
  }

  navToFolder(token: String, index: number) {
    if (token == "Shared-root"){
      this.folders = [];
      this.files = [];
      this.utilService.setCurrentPath("");
      this.sharedWithMeClicked();
      return
    }
    
    if (this.path.split("/")[this.path.split("/").length-2] == token)
      return

    let folderName = "";
    this.folders = [];
    this.files = [];
    if (token != "Root" && token != "Shared-root") {
      for (let i = 0; i < index; i++)
      folderName += this.navItems[i] + "/";
      folderName += token;
    }
    
    if (!folderName.endsWith("/") && folderName != "")
      folderName += "/";
    if (token == "Root")
      this.currentFolderFullPath = "";
    this.utilService.setCurrentPath(folderName);
  }

  logout() {
    localStorage.removeItem('user');
    this.cognitoService.setLoggedIn(false);
    this.cognitoService.signOut();
    this.router.navigate(['login']);
  }

  openCreateFolderDialog() {}

  openFolder(folderName: String) {
    this.currentFolderFullPath = folderName;
    if (folderName == "Root")
      this.currentFolderFullPath = "";
    this.folders = [];
    this.files = [];
    this.utilService.setCurrentPath(this.path + folderName.split('/')[folderName.split('/').length-2] + "/");
  }

  deleteFolder(folderName: String) {
    this.lambdaService.deleteFolder(folderName).subscribe({
      next: (value) => {
        this.snackBar.open("Successfully deleted folder!", "", {
          duration: 2700, panelClass: ['snack-bar-success']
        });
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        });
      },
    })
  }

  deleteFile(fileName: String) {
    this.lambdaService.deleteFile(fileName).subscribe({
      next: (value) => {
        this.snackBar.open("Successfully deleted file!", "", {
          duration: 2700, panelClass: ['snack-bar-success']
        });
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-front-error']
        });
      },
    })
  }

  readContent() {
    this.lambdaService.readCurrentFolderContent(this.currentFolderFullPath).subscribe({
      next: (value: String[])  => {
        value.forEach(element=> {
          if (element.endsWith("/"))
            this.folders.push(element);
          else
            this.files;
        });
        this.loaded = true;
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        })
      },
    })
  }

  openFileDetails(file: String) {}

  editFile(file: String) {
    this.utilService.setClickedFile(file);
    this.router.navigate(['/update'])
  }

  isSharedWithMeClicked: boolean = false;

  sharedWithMeClicked(){
    this.isSharedWithMeClicked = true;
    this.readSharedFiles();
    this.readSharedFolders();
  }

  readSharedFiles(){
    this.files = [];
    this.lambdaService.getSharedFilesByUsername().subscribe({
      next: (value: String[])  => {
        for (let str of value){
          continue;
          // if (!this.files.includes(str)) {
          //   this.files.push(str);
          // }
        }
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        })
      },
    })
  }

  readSharedFolders(){
    this.folders = [];
    this.lambdaService.getSharedFoldersByUsername().subscribe({
      next: (value: String[])  => {
        value.forEach(element=> {
          // if (element.endsWith("/"))
          //   this.folders.push(element);
          // else
            this.folders.push(element);
        });
      },
      error: (err) => {
        this.snackBar.open(err.error, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        })
      },
    })
  }


  manageSharing(name: String){ }

  reloadPage(){
    window.location.reload();
  }

}
