import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
registerLocaleData(zh);

import { FundSearchComponent } from './pages/fund-search/fund-search.component';
import { FundDetailComponent } from './pages/fund-detail/fund-detail.component';

import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { HttpserviceService } from './services/httpservice.service';
import { InfoService } from './services/info.service';
import { HttpIndexService } from './services/index.service';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { zh_CN } from 'ng-zorro-antd/i18n';

@NgModule({
  declarations: [
    AppComponent,
    FundSearchComponent,
    FundDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    NzButtonModule,
    NzInputModule,
    NzSelectModule,
    NzTableModule,
    NzDividerModule,
    NzModalModule,
    NzInputNumberModule,
    NzDatePickerModule,
    NzMessageModule
  ],
  bootstrap: [AppComponent],
  providers: [
    HttpserviceService,
    InfoService,
    HttpIndexService,
    { provide: NZ_I18N, useValue: zh_CN }
  ]
})
export class AppModule { }
