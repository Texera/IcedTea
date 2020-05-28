import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { NgbdModalResourceViewComponent } from './ngbd-modal-resource-view.component';
import { CustomNgMaterialModule } from '../../../../../common/custom-ng-material.module';
import { UserService } from '../../../../../common/service/user/user.service';
import { UserDictionaryService } from '../../../../../common/service/user/user-dictionary/user-dictionary.service';

describe('NgbdModalResourceViewComponent', () => {
  let component: NgbdModalResourceViewComponent;
  let fixture: ComponentFixture<NgbdModalResourceViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgbdModalResourceViewComponent ],
      providers: [
        NgbActiveModal,
        UserService,
        UserDictionaryService
      ],
      imports: [
        CustomNgMaterialModule,
        NgbModule,
        FormsModule,
        HttpClientModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgbdModalResourceViewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
