import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { Router } from '@angular/router';

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.css']
})
export class SideMenuComponent implements OnInit {

  @Output()
  navigatedFromMenu = new EventEmitter<boolean>();

  constructor(private router: Router) { }

  ngOnInit() { }

  goToSettings() {
    this.router.navigate(['settings']);
    this.navigatedFromMenu.emit(true);
  }

  goToHelp() {
    this.router.navigate(['help']);
    this.navigatedFromMenu.emit(true);
  }

}
