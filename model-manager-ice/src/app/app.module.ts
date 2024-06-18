import * as process from 'process';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // <-- NgModel lives here
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { NgtUniversalModule } from '@ng-toolkit/universal';
import { CommonModule } from '@angular/common';
import { TransferHttpCacheModule } from '@nguniversal/common';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AlertService, AuthenticationService, UserService, TranslationService, FileService } from './services/index_services';
import { AlertComponent } from './directives/index_directives';
import { AuthGuard } from './guards/index.guard';
import { AuthGuardKey } from './guards/index.guard';

import { HomeComponent } from './home/index_home';
// import { LoginComponent } from './login/index_login';
// import { RegisterComponent } from './register/index_register';
import { JwtInterceptor } from './helpers/index_helpers';
import { NavBarComponent } from './nav/index_nav';
// used to create fake backend
// import { fakeBackendProvider } from './helpers/index_helpers';
import { NeuronalnetworkcolumnsComponent } from './neuronalnetworkcolumns/neuronalnetworkcolumns.component';
// import { FileUploaderComponent } from './fileuploader/fileuploader.component';
// import { FileListComponent } from './filelist/filelist.component';

import { ReactiveFormsModule } from '@angular/forms';

import { UploadFileComponent } from './neuronalnetworkcolumns/uploadfile/uploadfile.component';

import { DataTablesModule } from 'angular-datatables';
import { NeuronalnetworkcolumsCorreComponent } from './neuronalnetworkcolumns/neuronalnetworkcolums-corre/neuronalnetworkcolums-corre.component';
import { MatTabsModule } from '@angular/material/tabs';

import { NgApexchartsModule } from 'ng-apexcharts';
import { NeuronalnetworkcolumsAnomaliesComponent } from './neuronalnetworkcolumns/neuronalnetworkcolums-corre/neuronalnetworkcolums-anomalies/neuronalnetworkcolums-anomalies.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { NzTableModule } from 'ng-zorro-antd/table';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library, dom  } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import { UnsupervisedComponent } from './unsupervised/unsupervised.component';
import { SupervisedComponent } from './supervised/supervised.component';
import { MymodelsComponent } from './mymodels/mymodels.component';
import { MyaccountComponent } from './myaccount/myaccount.component';
import { SettingsComponent } from './settings/settings.component';
import { DatePipe } from '@angular/common';
import { ReadmodelComponent } from './readmodel/readmodel.component';
import { NeuronalnetworkidComponent } from './neuronalnetworkid/neuronalnetworkid.component';
import { UploadfilesidComponent } from './neuronalnetworkid/uploadfilesid/uploadfilesid.component';
import { NeuronalnetworkingcolumnsidCorreComponent } from './neuronalnetworkid/neuronalnetworkingcolumnsid-corre/neuronalnetworkingcolumnsid-corre.component';
import { NeuronalnetworkingcolumnsidAnomaliesComponent } from './neuronalnetworkid/neuronalnetworkingcolumnsid-corre/neuronalnetworkingcolumnsid-anomalies/neuronalnetworkingcolumnsid-anomalies.component';
import { ReadmodelidComponent } from './readmodelid/readmodelid.component';
import { InitkmeansComponent } from './clusteringinitkmeans/initkmeans.component';
import { UploadfileskmeansComponent } from './clusteringinitkmeans/uploadfileskmeans/uploadfileskmeans.component';
// import { ImportDatasetsIDColumnssKmeansComponent } from './clusteringinitkmeans/kmeans/import-datasets-idcolumnss-kmeans.component';
// tslint:disable-next-line: max-line-length
// import { ImportValuesPerIDColumnsKmeansARComponent } from './clusteringinitkmeans/kmeans/kmeans-ar/import-values-per-idcolumns-kmeans-ar.component';
// tslint:disable-next-line: max-line-length
// import { ImportValuesPerIDColumnsKmeansAREndComponent } from './clusteringinitkmeans/kmeans/kmeans-ar/kmeans-ar-end/import-values-per-idcolumns-kmeans-ar-end.component';
// tslint:disable-next-line:max-line-length
// import { ImportDatasetsKmeansEndRegressionComponent } from './clusteringinitkmeans/kmeans/kmeans-ar/kmeans-ar-end/kmeans-end-regression/import-datasets-kmeans-end-regression.component';
// tslint:disable-next-line:max-line-length
// import { ImportDatasetsKmeansEndComponent } from './clusteringinitkmeans/kmeans/kmeans-ar/kmeans-ar-end/kmeans-end-regression/kmeans-end/import-datasets-kmeans-end.component';
import { KmeansModelComponent } from './clusteringinitkmeans/kmeans-model/kmeans-model.component';
import { KmeansGraphicsComponent } from '././clusteringinitkmeans/kmeans-model/kmeans-graphics/kmeans-graphics.component';
// Translation
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MaterialModule } from './material/material.module';
import { InitGLMComponent } from './init-glm/init-glm.component';
import { UploadfilesGLMComponent } from './init-glm/uploadfiles-glm/uploadfiles-glm.component';
// import { ImportDatasetsIdcolumnssGlmComponent } from './init-glm/start-idcolumns-glm/import-datasets-idcolumnss-glm.component';
// tslint:disable-next-line:max-line-length
// import { ImportValuesPerIdcolumnssGlmComponent } from './init-glm/start-idcolumns-glm/idcolumns-glm/import-values-per-idcolumnss-glm.component';
// tslint:disable-next-line:max-line-length
// import { ImportValuesPerIdcolumnssGlmEndComponent } from './init-glm/start-idcolumns-glm/idcolumns-glm/id-glm-end/import-values-per-idcolumnss-glm-end.component';
// tslint:disable-next-line:max-line-length
// import { ImportValuesPerIdcolumnssGlmEndRegressionComponent } from './init-glm/start-idcolumns-glm/idcolumns-glm/id-glm-end/glm-end-regression/import-values-per-idcolumnss-glm-end-regression.component';
// tslint:disable-next-line:max-line-length
// import { ImportDatasetsGlmEndComponent } from './init-glm/start-idcolumns-glm/idcolumns-glm/id-glm-end/glm-end-regression/glm-end/import-datasets-glm-end.component';
import { GlmModelComponent } from './init-glm/glm-model/glm-model.component';
import { GlmGraphicsComponent } from './init-glm/glm-model/glm-graphics/glm-graphics.component';
import { MainComponent } from './main/main.component';
import { PredatorComponent } from './predator/predator.component';
import { ProgressbarComponent } from './progressbar/progressbar.component';
import { TimeLoadingService} from './services/timeloading.service';
import { AgGridModule } from '@ag-grid-community/angular';
import { ImagerecognitionComponent } from './imagerecognition/imagerecognition.component';
import { CnnComponent } from './cnn/cnn-uploadfiles/cnn.component';
import { RednnComponent } from './rnn/rednn.component';
import { RedbnComponent } from './rbn/redbn.component';
import { AutoencoderComponent } from './autoencoder/autoencoder.component';
import { CnnModelComponent } from './cnn/cnn-model/cnn-model.component';
import { DndDirective } from './cnn/cnn-model/dnd.directive';
import { ProgressComponent } from './cnn/cnn-model/progress/progress.component';
import { FilterPipe } from './mymodels/filter.pipe';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ClusteringComponent } from './clustering/clustering.component';
import { ClusteringwardComponent } from './clusteringward/wardFiles/clusteringward.component';
import { ClusteringbirchComponent } from './clusteringbirch/birchFiles/clusteringbirch.component';
import { BirchmodelComponent } from './clusteringbirch/birchmodel/birchmodel.component';
import { BirchdeployComponent } from './clusteringbirch/birchDeploy/birchdeploy.component';
import { ClusteringtensorflowComponent } from './clusteringtensorflow/clusteringtensorflow.component';
import { WardmodelComponent } from './clusteringward/wardmodel/wardmodel.component';
import { SklearnmodelComponent } from './clusteringsklearncluster/sklearnmodel/sklearnmodel.component';
import { SklearnfilesComponent } from './clusteringsklearncluster/sklearnfiles/sklearnfiles.component';
import { SklearndeployComponent } from './clusteringsklearncluster/sklearndeploy/sklearndeploy.component';

import {BackendscriptsService} from './services/backendscripts.service';

import { ScrollingModule } from '@angular/cdk/scrolling';

import {TableModule} from 'primeng/table';
import {CalendarModule} from 'primeng/calendar';
import {SliderModule} from 'primeng/slider';
import {DialogModule} from 'primeng/dialog';
import {MultiSelectModule} from 'primeng/multiselect';
import {ContextMenuModule} from 'primeng/contextmenu';
import {ButtonModule} from 'primeng/button';
import {ToastModule} from 'primeng/toast';
import {InputTextModule} from 'primeng/inputtext';
import {ProgressBarModule} from 'primeng/progressbar';
import {DropdownModule} from 'primeng/dropdown';
import {IvyCarouselModule} from 'angular-responsive-carousel';
import {SanitizeHtmlPipe} from './pipes/SanitizeHtmlPipe';
import { WarddeployComponent } from './clusteringward/wardDeploy/warddeploy.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { KeycloakSecurityService } from './services/keycloak-security.service';
import { TokenInterceptor } from './services/token-interceptor';
import { GanUploadfilesComponent } from './gan/gan-uploadfiles/gan-uploadfiles.component';
import { GanModelComponent } from './gan/gan-model/gan-model.component';
import { Observable } from 'rxjs/internal/Observable';
import { map, tap } from 'rxjs/operators';
// import { Envi } from './envi/envi.component';
import { environment } from '../environments/environment'

import { EnvServiceProvider } from './services/Env.service.provider';
import { EnvService } from './services/EnvService.service';

// import { AuthGuardKeyGAN } from './guards/AuthGuardKeyGAN.guard';
// import { initializeKeycloak } from './init/keycloak-init.factory';
// import { ConfigInitService } from './init/config-init.service';
// import { Config } from '../../config.js';

let ipTemp;


// function initializeAppFactory(httpClient: HttpClient): () => any {
//   return () =>  httpClient.get("/users/files/envBackEnd/",{responseType: 'text'})
//     .subscribe(data => { 
//       console.log('Env: ',data);
//       ipTemp = data;

//     })

//  }



@NgModule({
  declarations: [
    AppComponent,
    AlertComponent,
    HomeComponent,
    // LoginComponent,
    // RegisterComponent,
    NavBarComponent,
    NeuronalnetworkcolumnsComponent,
    // FileUploaderComponent, // secure version now no working
    // FileListComponent, // secure version now no working
    UploadFileComponent,
    NeuronalnetworkcolumsCorreComponent,
    NeuronalnetworkcolumsAnomaliesComponent,
    UnsupervisedComponent,
    SupervisedComponent,
    MymodelsComponent,
    MyaccountComponent,
    SettingsComponent,
    ReadmodelComponent,
    NeuronalnetworkidComponent,
    UploadfilesidComponent,
    NeuronalnetworkingcolumnsidCorreComponent,
    NeuronalnetworkingcolumnsidAnomaliesComponent,
    ReadmodelidComponent,
    InitkmeansComponent,
    UploadfileskmeansComponent,
    // ImportDatasetsIDColumnssKmeansComponent,
    // ImportValuesPerIDColumnsKmeansARComponent,
    // ImportValuesPerIDColumnsKmeansAREndComponent,
    // ImportDatasetsKmeansEndRegressionComponent,
    // ImportDatasetsKmeansEndComponent,
    KmeansModelComponent,
    KmeansGraphicsComponent,
    InitGLMComponent,
    UploadfilesGLMComponent,
    // ImportDatasetsIdcolumnssGlmComponent,
    // ImportValuesPerIdcolumnssGlmComponent,
    // ImportValuesPerIdcolumnssGlmEndComponent,
    // ImportValuesPerIdcolumnssGlmEndRegressionComponent,
    // ImportDatasetsGlmEndComponent,
    GlmModelComponent,
    GlmGraphicsComponent,
    MainComponent,
    PredatorComponent,
    ProgressbarComponent,
    ImagerecognitionComponent,
    CnnComponent,
    RednnComponent,
    RedbnComponent,
    AutoencoderComponent,
    CnnModelComponent,
    DndDirective,
    ProgressComponent,
    FilterPipe,
    ClusteringComponent,
    ClusteringwardComponent,
    ClusteringbirchComponent,
    ClusteringtensorflowComponent,
    WardmodelComponent,
    SklearnmodelComponent,
    SklearnfilesComponent,
    SklearndeployComponent,
    BirchmodelComponent,
    SanitizeHtmlPipe,
    BirchdeployComponent,
    WarddeployComponent,
    GanUploadfilesComponent,
    GanModelComponent,

  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    IvyCarouselModule,
    NgtUniversalModule,
    TransferHttpCacheModule,
    MDBBootstrapModule.forRoot(),
    DataTablesModule,
    MatTabsModule,
    NgApexchartsModule,
    BrowserModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NzTableModule,
    FontAwesomeModule,
    MaterialModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    TableModule,
    CalendarModule,
    SliderModule,
    DialogModule,
    MultiSelectModule,
    ContextMenuModule,
    DropdownModule,
    ButtonModule,
    ToastModule,
    InputTextModule,
    ProgressBarModule,
    HttpClientModule,
    FormsModule,
    ScrollingModule,
    KeycloakAngularModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (http: HttpClient) => {
          return new TranslateHttpLoader(http);
        },
        deps: [ HttpClient ]
      }
    }),
    AgGridModule.withComponents([]
        )
    ],
    providers: [
      // {
      //   provide: APP_INITIALIZER,
      //   useFactory: initializeAppFactory,
      //   deps: [HttpClient,KeycloakService],
      //   multi: true
      // },
     {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    AuthGuard,
    AuthGuardKey,
    AlertService,
    FileService,
    AuthenticationService,
    BackendscriptsService,
    UserService,
    DatePipe,
    TranslationService,
    TimeLoadingService,
    SettingsComponent,
     {// user old authorisation
        provide: HTTP_INTERCEPTORS,
        useClass: JwtInterceptor,
        multi: true
    },
    EnvServiceProvider,

  ],
  bootstrap: [AppComponent],
})
export class AppModule {
  ipTemp: any= '';

  host: any;
  constructor(private translate: TranslateService,
              private userService: UserService,
              private http: HttpClient,
              public envService: EnvService
             ){
        ipTemp = envService.KEYCLOAK_SERVER
        console.log('Keycloak Server Frond End develop: ' , ipTemp);
        library.add(fas, far);
        dom.watch();

  }
 

}
export function initializeKeycloak(keycloak: KeycloakService): any {
  
  // let ipTemp = environment.KEYCLOAK_SERVER;

    

      return () =>
      // setTimeout(() => {
        keycloak.init({
          config: {
            realm: 'analyticsproduct', //'ihelp',
            url: ipTemp, 
            clientId: 'analytics'// 'ihelp',
          },
          initOptions: {
            onLoad: 'check-sso',
           
          },
        
        });
}

