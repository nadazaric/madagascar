import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Credentials} from '../services/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { usernameRegexValidator } from '../validators/user/userValidator';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent {

  isVisible = false;
  submited: boolean = false;
  credentials: Credentials;

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  constructor(private router: Router,
              private snackBar: MatSnackBar,
              private authService: AuthService) {
    this.credentials = {} as Credentials;
  }

  public signIn(): void {
    this.submited = true;

    const credentials = {
      email: this.loginForm.value.username,
      password: this.loginForm.value.password
    };

    if (this.loginForm.valid) {
      this.authService.login(credentials).subscribe({
        next: (result) => {
          this.processLogin(result);
        },
         error: (error) => {
          console.log(error);
          console.log("tu")
          console.log(error.error);
          this.snackBar.open("Bad credentials. Please try again!", "", {
              duration: 2700, panelClass: ['snack-bar-server-error']
          });
        },
      });
    }
  }

  processLogin(result: any) {
    localStorage.setItem('user', JSON.stringify(result.accessToken));
    // localStorage.setItem('refreshToken', JSON.stringify(result.refreshToken));
    this.authService.setUser();
    let user = this.authService.getUser();
    console.log(this.authService.getUser());
    this.authService.setLoggedIn(true);
    if (this.authService.getRole() == "ROLE_ADMIN")
      this.router.navigate(['users']);
    else
      this.router.navigate(['homepage']);
  }

}