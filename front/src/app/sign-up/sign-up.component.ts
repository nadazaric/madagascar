import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { hasLetterAndDigitValidator, nameRegexValidator, surnameRegexValidator, usernameRegexValidator } from '../validators/user/userValidator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../services/user.service';

export interface Account{
  name: string,
  surname: string,
  username: string,
  email: string,
  password: string,
}

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css'],
})

export class SignUpComponent {

  loading: boolean;
  isConfirm: boolean;
  isVisible: boolean = false;

  constructor(private router: Router,
              private userService: UserService,
              private snackBar: MatSnackBar){
    this.loading = false;
    this.isConfirm = false;
  }

  registerForm = new FormGroup({
    name: new FormControl('', [Validators.required, nameRegexValidator]),
    surname: new FormControl('', [Validators.required, surnameRegexValidator]),
    username: new FormControl('', [Validators.required, usernameRegexValidator]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, hasLetterAndDigitValidator()]),
  }, [])

  public signUp(): void {
    if (this.registerForm.valid) {
      this.loading = true;

      this.singUp();
    } else{
      this.snackBar.open("Check your inputs again!", "", {
        duration: 2700, panelClass: ['snack-bar-front-error']
      });
    }
  }

  singUp() {
    let creds = {
      name: this.registerForm.value.name!,
      surname: this.registerForm.value.surname!,
      username: this.registerForm.value.username!,
      email: this.registerForm.value.email!,
      password: this.registerForm.value.password!,
    }

    this.userService.register(creds).subscribe({
      next: () => {
        this.snackBar.open("Success", "", {
          duration: 2700, panelClass: ['snack-bar-success']
        });
        this.router.navigate(['login'])
      }, error: (err) => {
        this.snackBar.open(err.message, "", {
          duration: 2700, panelClass: ['snack-bar-back-error']
        });
        this.loading = false;
      }
    })
  }
}