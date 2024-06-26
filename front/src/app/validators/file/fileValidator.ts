import { AbstractControl, FormGroup, FormControl, FormGroupDirective, NgForm, ValidatorFn } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core'

  export function fileNameRegexValidator( control: AbstractControl): { [key: string]: boolean } | null {
    const regex = /^([a-zA-Zčćđžš0-9 ._-]*)$/;
    if (control.value !== undefined && !regex.test(control.value)) {
        return { fileNameRegexError: true };
    }
    return null;
  }

  export function fileDescriptionRegexValidator( control: AbstractControl): { [key: string]: boolean } | null {
    const regex = /^([a-zA-Zčćđžš0-9 ._]*)$/;
    if (control.value !== undefined && !regex.test(control.value)) {
        return { fileDescriptionRegexError: true };
    }
    return null;
  }