import { Component, OnInit } from "@angular/core";
import * as Papa from "papaparse";

let obj: AppComponent;

@Component({
  selector: "my-app",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {
  dataList: any[];
  filterCount: string;
  dataListFiltered: any[];
  dataListGlobal: any[];
  headearName: any;

  ngOnInit() {
    obj = this;
    this.filterCount = "";
    this.dataList = [];
    this.headearName = "RABO CSV READER"
  }

  filterBasedonColumn(event,filterColumn) {
    let filterValue = event.target.value;
    if (filterValue == "") {
      this.dataList = this.dataListGlobal;
    } else {

      let filteredListValue = this.dataListGlobal.filter(function (e) {
        if (e[filterColumn] == filterValue) {
          return e;
        }
      });

      this.dataListFiltered = filteredListValue;
      this.dataList = this.dataListFiltered;
    }

  }

  onChange(files: File[]) {
    if (files[0]) {
      console.log(files[0]);
      Papa.parse(files[0], {
        header: true,
        skipEmptyLines: true,
        complete: (result, file) => {
          console.log(result);
          this.dataList = result.data;
          this.dataListGlobal = result.data;

          let renamekeyObjects = result.data.map(function (obj) {
            obj['first_name'] = obj['First name'];
            obj['sur_name'] = obj['Sur name'];
            obj['issue_Count'] = obj['Issue Count'];
            obj['dob'] = obj['Date of Birth'];
            delete obj['First name'];
            delete obj['Sur name'];
            delete obj['Issue Count'];
            delete obj['Date of Birth'];
            return obj;
          });
          this.dataList = renamekeyObjects;
          this.dataListGlobal = renamekeyObjects;

        }
      });
    }
  }


}
