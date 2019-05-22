import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualQueristComponent } from './visual-querist.component';

describe('VisualQueristComponent', () => {
  let component: VisualQueristComponent;
  let fixture: ComponentFixture<VisualQueristComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualQueristComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualQueristComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
