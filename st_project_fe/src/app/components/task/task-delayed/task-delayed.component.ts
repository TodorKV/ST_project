import { Component, Input, OnInit } from '@angular/core';
import { GetUserResponse, UserService } from '../../../services/user.service';
import { MatTableDataSource } from '@angular/material/table';
import { Jwtobj } from '../../../common/jwtobj';
import { TasksService } from '../../../services/tasks.service';
import { MatDialog } from '@angular/material/dialog';
import jwt_decode from 'jwt-decode';
import { PageEvent } from '@angular/material/paginator';
import { switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-task-delayed',
  templateUrl: './task-delayed.component.html',
  styleUrls: ['./task-delayed.component.css']
})
export class TaskDelayedComponent implements OnInit {
  private tenantId: string = '';

  mouseOverIndex = -1;
  displayedColumns: string[] = ['description', 'user', 'options'];
  tasks: Task[] = [];
  user: GetUserResponse | undefined;
  pageNumber: number = 0;
  pageSize: number = 10;
  totalElements: number = 0;
  dataSource = new MatTableDataSource(this.tasks);

  constructor(
    private tasksService: TasksService,
    private userService: UserService,
    public dialog: MatDialog) { }

  ngOnInit() {
    const token = localStorage.getItem("token");
    const decoded: Jwtobj = (jwt_decode(token!));

    this.tasksService.refresh
      .subscribe((data: any) => {
        if (data) {
          this.getPage();
        }
      });

    this.userService
      .getUserInfo(decoded.sub)
      .pipe(
        tap((response) => {
          this.tenantId = response.tenant.id;
        }),
        switchMap((response) => this.tasksService.getDelayedTaskListPaginate(this.pageNumber, this.pageSize, response.tenant.id)),
      )
      .subscribe((data: any) => {
        this.tasks = data.content;
        this.pageNumber = data.number + 1;
        this.pageSize = data.numberOfElements;
        this.totalElements = data.totalElements;
        this.dataSource = new MatTableDataSource(this.tasks);
      })
  }

  applyFilter(event: any) {
    if (event.key === 'Enter') {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
      this.getPage();
    }
  }

  public onMouseOver(index: any) {
    this.mouseOverIndex = index;
  }

  deleteOrder(id: string) {
    this.tasksService
      .removeContent(id)
      .subscribe(_ => {
        this.getPage();
      });
  }

  finishTask(id: string) {
    this.tasksService
      .finishTask(id)
      .subscribe(_ => {
        this.getPage();
      });
  }

  handlePage(event: PageEvent) {
    this.pageNumber = event.pageIndex;
    this.pageSize = event.pageSize;
    if (this.dataSource.filter) {
      this.tasksService
        .searchTaskPaginate(
          this.pageNumber,
          this.pageSize,
          this.dataSource.filter.toString())
        .subscribe(
          (data: any) => {
            this.tasks = data.content;
            this.pageNumber = data.number + 1;
            this.pageSize = data.numberOfElements;
            this.totalElements = data.totalElements;
            this.dataSource = new MatTableDataSource(this.tasks);
          }
        )
    }
    else {
      this.tasksService
        .getDelayedTaskListPaginate(
          this.pageNumber,
          this.pageSize,
          this.tenantId)
        .subscribe((data: any) => {
          this.tasks = data.content;
          console.log(this.dataSource);
          console.log(this.tasks);
          
          this.pageNumber = data.number + 1;
          this.pageSize = data.numberOfElements;
          this.totalElements = data.totalElements;
          this.dataSource = new MatTableDataSource(this.tasks);
        })
    }
  }

  getPage() {
    this.pageNumber = 0;
    if (this.pageSize < 1) this.pageSize = 10;
    if (this.dataSource.filter) {
      this.tasksService
        .searchTaskPaginate(
          this.pageNumber,
          this.pageSize,
          this.dataSource.filter.toString())
        .subscribe((data: any) => {
          this.tasks = data.content;
          this.pageNumber = data.number + 1;
          this.pageSize = data.numberOfElements;
          this.totalElements = data.totalElements;
          this.dataSource = new MatTableDataSource(this.tasks);
          this.tasksService.refreshTrigger = false;
        });
    }
    else {
      this.pageSize = 10;
      this.tasksService
        .getDelayedTaskListPaginate(this.pageNumber,
          this.pageSize,
          this.tenantId)
        .subscribe((data: any) => {
          this.tasks = data.content;
          this.pageNumber = data.number + 1;
          this.pageSize = data.numberOfElements;
          this.totalElements = data.totalElements;
          this.dataSource = new MatTableDataSource(this.tasks);
          this.tasksService.refreshTrigger = false;
        });
    }
  }


}
