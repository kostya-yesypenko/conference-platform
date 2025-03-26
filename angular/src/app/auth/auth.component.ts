import { Component, ElementRef, AfterViewInit, ViewChild } from "@angular/core";

@Component({
  selector: "app-auth",
  templateUrl: "./auth.component.html",
  styleUrls: ["./auth.component.scss"]
})
export class AuthComponent implements AfterViewInit {
  @ViewChild('container', {static: false}) container!: ElementRef;

  constructor() {}

  ngAfterViewInit(): void {}

  activateContainer(): void {
    this.container?.nativeElement.classList.add("active");
  }

  deactivateContainer(): void {
    this.container?.nativeElement.classList.remove("active");
  }
}
