import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundSearchComponent } from './fund-search.component';

describe('FundSearchComponent', () => {
  let component: FundSearchComponent;
  let fixture: ComponentFixture<FundSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FundSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FundSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
