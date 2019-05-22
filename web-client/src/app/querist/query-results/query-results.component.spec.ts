import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QueryResultsComponent } from './query-results.component';

describe('QueryResultsComponent', () => {
  let component: QueryResultsComponent;
  let fixture: ComponentFixture<QueryResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
