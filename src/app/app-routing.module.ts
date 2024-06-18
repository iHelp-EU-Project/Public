import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
// import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/index_home';
// import { LoginComponent } from './login/index_login';
// import { RegisterComponent } from './register/index_register';
import { AuthGuard,
         AuthGuardKey,
         AuthGuardKeyBirch,
         AuthGuardKeyWard,
         AuthGuardKeyH2O,
       } from './guards/index.guard';
import { NeuronalnetworkcolumnsComponent } from './neuronalnetworkcolumns/neuronalnetworkcolumns.component';
import { UnsupervisedComponent } from './unsupervised/unsupervised.component';
import { SupervisedComponent } from './supervised/supervised.component';
import { MymodelsComponent } from './mymodels/mymodels.component';
import { SettingsComponent } from './settings/settings.component';
import { MyaccountComponent } from './myaccount/myaccount.component';
import { ReadmodelComponent } from './readmodel/readmodel.component';
import { NeuronalnetworkidComponent } from './neuronalnetworkid/neuronalnetworkid.component';
import { ReadmodelidComponent } from './readmodelid/readmodelid.component';
import { InitkmeansComponent } from './clusteringinitkmeans/initkmeans.component';
import { KmeansGraphicsComponent } from './clusteringinitkmeans/kmeans-model/kmeans-graphics/kmeans-graphics.component';
import { InitGLMComponent } from './init-glm/init-glm.component';
import { GlmGraphicsComponent } from './init-glm/glm-model/glm-graphics/glm-graphics.component';
import { MainComponent } from './main/main.component';
import { PredatorComponent } from './predator/predator.component';
import { ImagerecognitionComponent } from './imagerecognition/imagerecognition.component';
import { CnnComponent } from './cnn/cnn-uploadfiles/cnn.component';
import { CnnModelComponent } from './cnn/cnn-model/cnn-model.component';
import { RednnComponent } from './rnn/rednn.component';
import { RedbnComponent } from './rbn/redbn.component';
import { AutoencoderComponent } from './autoencoder/autoencoder.component';
import { ClusteringComponent } from './clustering/clustering.component';
import { ClusteringwardComponent } from './clusteringward/wardFiles/clusteringward.component';
import { WarddeployComponent } from './clusteringward/wardDeploy/warddeploy.component';
import { ClusteringbirchComponent } from './clusteringbirch/birchFiles/clusteringbirch.component';
import { BirchdeployComponent } from './clusteringbirch/birchDeploy/birchdeploy.component';
import { ClusteringtensorflowComponent } from './clusteringtensorflow/clusteringtensorflow.component';
import { SklearnfilesComponent } from './clusteringsklearncluster/sklearnfiles/sklearnfiles.component';
import { SklearndeployComponent } from './clusteringsklearncluster/sklearndeploy/sklearndeploy.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { GanUploadfilesComponent } from './gan/gan-uploadfiles/gan-uploadfiles.component';
import { GanModelComponent } from './gan/gan-model/gan-model.component'




// const routes: Routes = [
//   { path: 'dashboard', component: DashboardComponent }
// ];

const appRoutes: Routes = [
    { path: '', component: MainComponent,  },
    // { path: 'home', component: ClusteringComponent, canActivate: [AuthGuardKey]    },
    // { path: 'login', component: LoginComponent,    },
    // { path: 'register', component: RegisterComponent,  },
    // { path: 'neuronalnetworkcolumns' , component: NeuronalnetworkcolumnsComponent,  canActivate: [AuthGuardKeyNN]   },
    // { path: 'neuronalnetworkcolumnsid' , component: NeuronalnetworkidComponent, canActivate: [AuthGuardKeyNNid]    },
    { path: 'clustering' , component: ClusteringComponent, canActivate: [AuthGuardKey]    },
    { path: 'clusteringinitkmeans' , component: InitkmeansComponent, canActivate: [AuthGuardKey]    },
    { path: 'clusteringward' , component: ClusteringwardComponent, canActivate: [AuthGuardKey]    },
    { path: 'clusteringbirch' , component: ClusteringbirchComponent, canActivate: [AuthGuardKey]    },
    { path: 'clusteringtensorflow' , component: ClusteringtensorflowComponent, canActivate: [AuthGuardKey]    },
    { path: 'clusteringsklearn' , component: SklearnfilesComponent, canActivate: [AuthGuardKey]    },
    // { path: 'initkglm' , component: InitGLMComponent, canActivate: [AuthGuardKeyGLMRegression]    },
    // { path: 'cnn' , component: CnnComponent, canActivate: [AuthGuardKeyCNN]    },
    // { path: 'rnn' , component: RednnComponent, canActivate: [AuthGuardKeyRNN]    },
    // { path: 'rbn' , component: RedbnComponent, canActivate: [AuthGuardKeyRBN]    },
    // { path: 'gan' , component: GanUploadfilesComponent, canActivate: [AuthGuardKeyGAN]    },
    // { path: 'autoencoder' , component: AutoencoderComponent, canActivate: [AuthGuardKey]    },
    // { path: 'predator' , component: PredatorComponent, canActivate: [AuthGuardKeyPredator]    },
    // { path: 'unsupervised' , component: UnsupervisedComponent, canActivate: [AuthGuardKey]    },
    // { path: 'supervised' , component: SupervisedComponent, canActivate: [AuthGuardKey]    },
    // { path: 'imagesrecognition' , component: ImagerecognitionComponent, canActivate: [AuthGuardKey]    },
    { path: 'mymodels' , component: MymodelsComponent, canActivate: [AuthGuardKey]    },
    { path: 'myaccount' , component: MyaccountComponent, canActivate: [AuthGuardKey]    },
    { path: 'settings' , component: SettingsComponent,  canActivate: [AuthGuardKey]   },
    // { path: 'readmodels/:namemodels' , component: ReadmodelComponent, canActivate: [AuthGuardKey]    },
    // { path: 'readmodelsid/:namemodels' , component: ReadmodelidComponent, canActivate: [AuthGuardKey]    },
    { path: 'clustering/:namemodels' , component: KmeansGraphicsComponent, canActivate: [AuthGuardKey]    },
    // { path: 'glm/:namemodels' , component: GlmGraphicsComponent,  canActivate: [AuthGuardKey],   },
    // { path: 'cnn/:namemodels' , component: CnnModelComponent,  canActivate: [AuthGuardKey],   },
    { path: 'clusteringsklearn/:namemodels' , component: SklearndeployComponent, canActivate: [AuthGuardKey]    },
    { path: 'birchdeploy/:namemodels' , component: BirchdeployComponent, canActivate: [AuthGuardKey]    },
    { path: 'warddeploy/:namemodels' , component: WarddeployComponent, canActivate: [AuthGuardKey]    },
    // { path: 'gandeploy/:namemodels' , component: GanModelComponent, canActivate: [AuthGuardKey]    },




    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];
export const routing = RouterModule.forRoot(appRoutes);

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(appRoutes, {
    initialNavigation: 'enabled'
})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
