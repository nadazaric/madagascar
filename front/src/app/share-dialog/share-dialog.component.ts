import { Component, Inject, OnInit } from '@angular/core';
import { FileMetaData, LambdaService } from './../services/lambda.service';
import { ACLDTO, FileService, SharedUserDTO, SharedWithDTO } from './../services/file.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import * as saveAs from 'file-saver';
import { ShareWithOthersFormComponent } from '../share-with-others-form/share-with-others-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FileDTO } from '../services/file.service';

@Component({
  selector: 'app-share-dialog',
  templateUrl: './share-dialog.component.html',
  styleUrls: ['./share-dialog.component.css']
})
export class ShareDialogComponent implements OnInit {

  file: FileDTO = {} as FileDTO;
  selectedAddPrivilege: string = "viewer";
  selectedUpdatePrivilege: string[] = [];
  sharedWith: SharedUserDTO[] = [];


  constructor(public dialogRef: MatDialogRef<ShareDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fileService: FileService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.file = this.data.file;
    this.getSharedWith();
  }

  inviteForm = new FormGroup({
    username: new FormControl('', [Validators.required])
  });

  addUserFromForm(){
    if (!this.inviteForm.valid) {
      this.snackBar.open("Input fields are required", "", {
        duration: 2700, panelClass: ['snack-bar-back-error']
      });
      return
    }
    let acl : ACLDTO = {
      user: this.inviteForm.value.username!,
      fileId: this.file.id,
      relation: this.selectedAddPrivilege
    }
    this.fileService.shareWithOther(acl).subscribe({
      next: (value: any)  => {
        this.snackBar.open("Successfully shared file!", "", {
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

  getSharedWith(){
    this.fileService.getSharedWith(this.file.id).subscribe(
      (data: SharedUserDTO[]) => {
        this.sharedWith = data;
        console.log(data);
        for (let share of data){
          this.selectedUpdatePrivilege.push(share.relation);
        }
        console.log(this.selectedUpdatePrivilege)
      },
      (error) => {
        console.error('Error fetching files', error);
      }
    );
  }

  updateSharedWith(index: any){
    // let acl : ACLDTO = {
    //   user: this.sharedWith[index].user,
    //   fileId: this.file.id,
    //   relation: this.selectedUpdatePrivilege[index] //proveriti dal treba lowercase ili uppercase
    // }

    // this.fileService.updateShareWith(acl).subscribe({
    //   next: (value: any)  => {
    //     console.log(value);
    //     this.snackBar.open("Successfully updated the share!", "", {
    //       duration: 2700, panelClass: ['snack-bar-success']
    //     });
    //   },
    //   error: (err) => {
    //     console.log(err);
    //     this.snackBar.open(err.error, "", {
    //       duration: 2700, panelClass: ['snack-bar-back-error']
    //     });
    //   },
    // })
  }

  deleteSharedWith(index: any){

    let acl : ACLDTO = {
      user: this.sharedWith[index].username,
      fileId: this.file.id,
      relation: this.selectedUpdatePrivilege[index]
    }

    this.fileService.deleteShareWith(acl).subscribe({
      next: (value: any)  => {
        this.sharedWith.splice(index, 1)
        this.selectedUpdatePrivilege.splice(index, 1)
        this.snackBar.open("Successfully deleted the share!", "", {
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
}