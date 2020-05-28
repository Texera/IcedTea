import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDictionarySectionComponent } from './user-dictionary-section.component';

import { UserDictionaryService } from '../../../../common/service/user/user-dictionary/user-dictionary.service';

import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

import { HttpClientModule, HttpClient } from '@angular/common/http';
import { CustomNgMaterialModule } from '../../../../common/custom-ng-material.module';

describe('UserDictionarySectionComponent', () => {
  let component: UserDictionarySectionComponent;
  let fixture: ComponentFixture<UserDictionarySectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserDictionarySectionComponent],
      providers: [
        UserDictionaryService,
        NgbActiveModal
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
    fixture = TestBed.createComponent(UserDictionarySectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

});
