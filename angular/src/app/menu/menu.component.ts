import { Component, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements AfterViewInit {

  ngAfterViewInit() {
    const toggleBtn = document.querySelector('.toggle_btn') as HTMLElement;
    const toggleBtnIcon = document.querySelector('.toggle_btn i') as HTMLElement;
    const dropDownMenu = document.querySelector('.dropdown_menu') as HTMLElement;

    toggleBtn.addEventListener('click', () => {
      dropDownMenu.classList.toggle('open');
      const isOpen = dropDownMenu.classList.contains('open');

      toggleBtnIcon.className = isOpen
        ? 'fa-solid fa-xmark'
        : 'fa-solid fa-bars';
    });
  }
}
