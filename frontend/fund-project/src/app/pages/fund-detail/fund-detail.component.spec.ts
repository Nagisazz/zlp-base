import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundDetailComponent } from './fund-detail.component';

describe('FundDetailComponent', () => {
  let component: FundDetailComponent;
  let fixture: ComponentFixture<FundDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FundDetailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FundDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
