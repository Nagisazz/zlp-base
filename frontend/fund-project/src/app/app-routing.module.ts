import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FundDetailComponent } from './pages/fund-detail/fund-detail.component';
import { FundSearchComponent } from './pages/fund-search/fund-search.component';
import { APP_BASE_HREF } from '@angular/common';

export const routes: Routes = [
  { 
    path: '', 
    component: FundSearchComponent,
  },
  { 
    path: 'fund-search',
    component: FundSearchComponent,
  },
  { 
    path: 'fund-detail',
    component: FundDetailComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{ provide: APP_BASE_HREF, useValue: (window as any).__POWERED_BY_QIANKUN__ ? '/platform/fund' : '/' }]
})
export class AppRoutingModule { }
