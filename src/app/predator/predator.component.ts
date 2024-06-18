import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService, AlertService } from '../services/index_services';
import { User } from '../models/index_models';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';



// import {SanitizeHtmlPipe} from '../pipes/SanitizeHtmlPipe';


@Component({
  selector: 'app-predator',
  templateUrl: './predator.component.html',
  styleUrls: ['./predator.component.css']
})
export class PredatorComponent implements OnInit {

  headers = new HttpHeaders({'Content-Type': 'multipart/form-data;boundary=gc0p4Jq0M2Yt08jU534c0p',
                             'Access-Control-Allow-Origin': '*',
                             'Access-Control-Allow-Headers': 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method',
                             'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, DELETE',
                             });
  options = { headers: this.headers};
  host: any;
  currentUser: User;

  // properties for dangen URL
  dangerousUrlPredictOK!: string;
  urlPredictOk!: SafeResourceUrl;

  dangerousUrlSimilarityVariance!: string;
  urlSimilarityVariance!: SafeResourceUrl;

  dangerousUrlSimilarityModelEncounterModel!: string;
  urlSimilarityModelEncounterModel!: SafeResourceUrl;

  dangerousUrlVarianceStanDesviationMeanSemilarity!: string;
  urlVarianceStanDesviationMeanSemilarity!: SafeResourceUrl;

  dangerousUrlPredicton!: string;
  urlPredicton!: SafeResourceUrl;

  dangerousUrlAngularVelocity!: string;
  urlAngularVelocity!: SafeResourceUrl;

  dangerousUrlForce!: string;
  urlForce!: SafeResourceUrl;

  dangerousUrlDisplacement!: string;
  urlDisplacement!: SafeResourceUrl;


  dateRefresh = new Date();
  dateRefreshFinal: any;

  serverPredator = 'localhost';
  serverPredatorKafka;
  // myFormLeft = new FormGroup({
  //   left: new FormControl('', [Validators.required]),
  //   fileSourceLeft: new FormControl('', [Validators.required]),
  // });
  // myFormRight = new FormGroup({
  //   right: new FormControl('', [Validators.required]),
  //   fileSourceRight: new FormControl('', [Validators.required]),
  // });
  // myFormGps = new FormGroup({
  //   gps: new FormControl('', [Validators.required]),
  //   fileSourceGPS: new FormControl('', [Validators.required]),
  // });

  myFormDataset = new FormGroup({
    server: new FormControl('', [Validators.required]),
    dataset: new FormControl('', [Validators.required]),
    fileSourceDataset: new FormControl('', [Validators.required]),
    machine: new FormControl('', [Validators.required]),
    numberRows: new FormControl('', [Validators.required]),
  });

  myFormServer = new FormGroup({});
  sanitizeHtml;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;


  constructor(private http: HttpClient,
              private userService: UserService,
              private sanitizer: DomSanitizer,
              private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
        this.host = self.location.host.split(':')[0];

        this.refreshDate();




  }

    // tslint:disable-next-line:typedef
async ngOnInit() {

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
        this.userProfile = await this.keycloak.loadUserProfile();
        this.currentUser.id = this.userProfile.id;
        this.currentUser.username = this.userProfile.username;
        this.currentUser.firstName = this.userProfile.firstName;
        this.currentUser.lastName = this.userProfile.lastName;

    }

    setInterval(this.reloadCurrentPage, 300000);

  }

  refreshDate(): void {

    this.dateRefresh = new Date();
    console.log(this.dateRefresh.valueOf());
    this.dateRefreshFinal = this.dateRefresh.getTime() + (15 * 60000);
    this.updateInsegureUrl(this.host);


  }



  reloadCurrentPage(): void {
    // window.location.reload();
    // this.refreshDate();
    // const iframeDome = document.getElementsByTagName('iframe');
    // iframeDome[0].contentWindow.location.reload();
    const date = new Date();
    console.log(date.valueOf());
    const dateR = date.getTime() + (15 * 60000);
    const iframe1 = document.getElementById('gra1') as HTMLFrameElement;
    iframe1.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=2&refresh=5s';
    const iframe3 = document.getElementById('gra3') as HTMLFrameElement;
    iframe3.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=29&refresh=5s';
    const iframe4 = document.getElementById('gra4') as HTMLFrameElement;
    iframe4.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=30&refresh=5s';
    const iframe5 = document.getElementById('gra5') as HTMLFrameElement;
    iframe5.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=31&refresh=5s';
    const iframe2 = document.getElementById('gra2') as HTMLFrameElement;
    iframe2.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=28&refresh=5s';
    const iframe6 = document.getElementById('gra6') as HTMLFrameElement;
    iframe6.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=35&refresh=5s';
    const iframe7 = document.getElementById('gra7') as HTMLFrameElement;
    iframe7.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=36&refresh=5s';
    const iframe8 = document.getElementById('gra8') as HTMLFrameElement;
    iframe8.src = 'http://' + self.location.host.split(':')[0] + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + dateR.valueOf() + '&orgId=1&theme=light&panelId=37&refresh=5s';
   }

   // Build URL secure for graphics grafana
   updateInsegureUrl(id: string): void {
    // Appending an host to a  URL is safe.
    // Always make sure to construct SafeValue objects as
    // close as possible to the input data so
    // that it's easier to check if the value is safe.
    this.dangerousUrlPredictOK =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=2&refresh=5s';
    this.urlPredictOk = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlPredictOK);

    this.dangerousUrlSimilarityVariance =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=29&refresh=5s';
    this.urlSimilarityVariance =
    this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlSimilarityVariance);

    this.dangerousUrlSimilarityModelEncounterModel =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=30&refresh=5s';
    this.urlSimilarityModelEncounterModel = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlSimilarityModelEncounterModel);

    this.dangerousUrlVarianceStanDesviationMeanSemilarity =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-15m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=31refresh=5s';
    this.urlVarianceStanDesviationMeanSemilarity =
    this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlVarianceStanDesviationMeanSemilarity);

    this.dangerousUrlPredicton =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-30m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=28&refresh=5s';
    this.urlPredicton = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlPredicton);

    this.dangerousUrlAngularVelocity =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-30m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=35&refresh=5s';
    this.urlAngularVelocity = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlAngularVelocity);

    this.dangerousUrlForce =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-30m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=36&refresh=5s';
    this.urlForce = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlForce);

    this.dangerousUrlDisplacement =
    'http://' + id + ':3000/d-solo/ad4iwzRMk/data?from=now-30m&to=' + this.dateRefreshFinal.valueOf() + '&orgId=1&theme=light&panelId=37&refresh=5s';
    this.urlDisplacement = this.sanitizer.bypassSecurityTrustResourceUrl(this.dangerousUrlDisplacement);
  }

  // url(s: string): any{
  //   return this.sanitizeHtml.transform(s);
  // }


  // get fleft(): any {
  //   return this.myFormLeft.controls;
  // }

  // get fright(): any {
  //   return this.myFormLeft.controls;
  // }
  // get fgps(): any {
  //   return this.myFormGps.controls;
  // }

  get fdataset(): any {
    return this.myFormDataset.controls;
  }

  onServerChange(event): void{

    this.serverPredator = event.target.value;


  }

  // onFileChangeLeft(event): void {
  //   if (event.target.files.length > 0) {
  //     const file = event.target.files[0];

  //     this.myFormLeft.patchValue({
  //       fileSourceLeft: file,
  //     });
  //   }
  // }

  // onFileChangeRight(event): void {
  //   if (event.target.files.length > 0) {
  //     const file = event.target.files[0];

  //     this.myFormRight.patchValue({
  //       fileSourceRight: file,
  //     });
  //   }
  // }

  // onFileChangeGPS(event): void {
  //   if (event.target.files.length > 0) {
  //     const file = event.target.files[0];

  //     this.myFormGps.patchValue({
  //       fileSourceGPS: file,
  //     });
  //   }
  // }

  onFileChangeDatasest(event): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];

      this.myFormDataset.patchValue({
        fileSourceDataset: file,
      });
    }
  }

  // submitLeft(): void {
  //   const formData = new FormData();


  //   formData.append('fileprofileLeft', this.myFormLeft.get('fileSourceLeft').value);


  //   this.http.post('http://' + this.serverPredator + ':8090/fileprofileLeft', formData)

  //     .subscribe((res) => {
  //       console.log(res);

  //       alert('Uploaded Successfully.');
  //     });
  // }


  // submitRight(): void {
  //   const formData = new FormData();


  //   formData.append('fileprofileRight', this.myFormRight.get('fileSourceRight').value);


  //   this.http.post('http://' + this.serverPredator + ':8090/fileprofileRight', formData)

  //     .subscribe((res) => {
  //       console.log(res);

  //       alert('Uploaded Successfully.');
  //     });
  // }

  // submitGPS(): void {
  //   const formData = new FormData();


  //   formData.append('filegps', this.myFormGps.get('fileSourceGPS').value);


  //   this.http.post('http://' + this.serverPredator + ':8090/filegps', formData)

  //     .subscribe((res) => {
  //       console.log(res);

  //       alert('Uploaded Successfully.');
  //     });
  // }

  submitDataset(): void {


    const formData = new FormData();

    console.log(this.myFormDataset.get('fileSourceDataset').value);
    console.log(this.myFormDataset.get('dataset').value);
    console.log(this.myFormDataset.get('machine').value);
    console.log(this.myFormDataset.get('numberRows').value);


    formData.append('fileSourceDataset', this.myFormDataset.get('fileSourceDataset').value);
    formData.append('machine', this.myFormDataset.get('machine').value);
    formData.append('numberRows', this.myFormDataset.get('numberRows').value);

    // const requestOptions = {
    //   method: 'POST',
    //   body: formData
    // };

    // fetch('http://localhost:8090/importDatasetCSV', requestOptions)
    //   .then(response => response.text())
    //   .then(result => console.log(result))
    //   .catch(error => console.log('error', error));

    // const  file = this.myFormDataset.value;
    // console.log(file);

    // formData.append('', file);

    console.log(formData);
    this.http.post('http://' + this.serverPredator + ':8090/importDatasetCSV', formData)

      .subscribe((res) => {
        console.log(res);

        // alert('Uploaded Successfully.');
      },
      error => {
          console.log(error);
      });
  }


  submitServerKafka(): void {


    const formData = new FormData();

    console.log(this.myFormDataset.get('server').value);
    console.log(this.myFormDataset.get('machine').value);
    console.log(this.myFormDataset.get('numberRows').value);


    formData.append('server', this.myFormDataset.get('server').value);
    formData.append('machine', this.myFormDataset.get('machine').value);
    formData.append('numberRows', this.myFormDataset.get('numberRows').value);


    console.log(formData);
    this.http.post('http://' + this.serverPredator + ':8090/serverKafka', formData)

      .subscribe((res) => {
        console.log(res);

        // alert('Uploaded Successfully.');
      },
      error => {
          console.log(error);
      });
  }

  startDocker(): void{

     try {

        this.userService.execDockerCompose(this.currentUser,  this.myFormDataset.get('machine').value, 'uploads')
              .subscribe(
                data => {

                            console.log(data);

                  },
              error => {
                });

    }catch (error){

    }


  }

}
