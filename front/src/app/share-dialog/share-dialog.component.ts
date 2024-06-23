import { Component, Inject, OnInit } from '@angular/core';
import { FileMetaData, LambdaService } from './../services/lambda.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import * as saveAs from 'file-saver';
import { ShareWithOthersFormComponent } from '../share-with-others-form/share-with-others-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-share-dialog',
  templateUrl: './share-dialog.component.html',
  styleUrls: ['./share-dialog.component.css']
})
export class ShareDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ShareDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private lambdaService: LambdaService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  inviteForm = new FormGroup({
    username: new FormControl('', [Validators.required])
  });

  addUserFromForm(){
    
  }

}
