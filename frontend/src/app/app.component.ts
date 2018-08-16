import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';

export interface PendingRequest {
  pendingRequest: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  fileSelected$: BehaviorSubject<boolean> = new BehaviorSubject(false);
  convertingBase64$: BehaviorSubject<boolean> = new BehaviorSubject(false);

  private _selectedFile: string;
  private _selectedFilename: string;
  private _selectedMimeType: string;
  private _pendingRequest: PendingRequest;

  // private _selectedFiles:

  constructor(private _httpClient: HttpClient) {

  }

  get pendingRequest() {
    return this._pendingRequest;
  }

  signDocument() {
    const location = window.location;
    const completionUrl = `${location.protocol}//${location.host}/${location.pathname.split('/')[1]}`;
    this._httpClient.post<PendingRequest>('http://localhost:9000/econtract-backend/api/v1/sign-document', {
      completionUrl: completionUrl,
      fileBase64: this._selectedFile,
      filename: this._selectedFilename,
      mimeType: this._selectedMimeType
    }).subscribe(pendingRequest => {
      this._pendingRequest = pendingRequest;
      setTimeout(() => document.forms['BrowserPostForm'].submit(), 100);
    });
  }

  handleFileInput(files: FileList) {
    if (files.length > 0) {
      console.log(files);
      this.convertingBase64$.next(true);
      this.fileSelected$.next(false);
      const myReader: FileReader = new FileReader();
      myReader.onloadend = () => {
        this._selectedFile = myReader.result.split('base64,')[1];
        this._selectedMimeType = files[0].type;
        this._selectedFilename = files[0].name;
        this.convertingBase64$.next(false);
        this.fileSelected$.next(true);
      };
      myReader.readAsDataURL(files[0]);
    } else {
      this.fileSelected$.next(false);
      this.convertingBase64$.next(false);
    }
  }
}
