import { Component, OnInit, ViewChild } from '@angular/core';

import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {

  @ViewChild('sidenav') burgerMenu: MatSidenav; 

  constructor(private router: Router) { }

  ngOnInit() {
  }

  goToQuerist() {
    this.router.navigate(['querist']);
  }

  goToMining() {
    this.router.navigate(['home']);
  }

  handleNavigation() {
    this.burgerMenu.close();
  }

}
