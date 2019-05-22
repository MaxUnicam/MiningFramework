import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './analysis/home/home.component';
import { ResultsComponent } from './analysis/results/results.component';

import { SettingsModule } from './settings/settings.module';
import { SettingsComponent } from './settings/settings/settings.component';
import { VisualQueristComponent } from './querist/visual-querist/visual-querist.component';
import { QueristModule } from './querist/querist.module';
import { QueryResultsComponent } from './querist/query-results/query-results.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'results/analysis',
    component: ResultsComponent
  },
  {
    path: 'results/query',
    component: QueryResultsComponent
  },
  {
    path: 'querist',
    component: VisualQueristComponent
  },
  {
    path: 'settings',
    component: SettingsComponent
  },
  {
    path: '**',
    component: HomeComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    SettingsModule,
    QueristModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
