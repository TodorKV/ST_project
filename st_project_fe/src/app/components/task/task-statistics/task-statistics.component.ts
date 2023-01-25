import { Component, OnInit} from '@angular/core';
import { User } from '../../../common/user';
import { TasksService } from '../../../services/tasks.service';
import { UserService } from '../../../services/user.service';
import { MatSelectChange } from '@angular/material/select';
import { Observable, of, tap } from 'rxjs';

@Component({
  selector: 'app-task-statistics',
  templateUrl: './task-statistics.component.html',
  styleUrls: ['./task-statistics.component.css']
})
export class TaskStatisticsComponent implements OnInit {
  private _users: User[] = [];
  
  currentlySelectedUserId: string | undefined;
  currentlySelectedUser: string | undefined;

  users$: Observable<any> = of();
  userStatistic$: Observable<any> = of();

  pageNumber: number = 0;
  pageSize: number = 10;

  constructor(
    private tasksService: TasksService,
    private userService: UserService) { }

  ngOnInit() {
    this.users$ = this.userService
      .getUserListPaginate(this.pageNumber, this.pageSize)
      .pipe(tap(data => this._users = data.content));
  }

  onUserSelection(selection: MatSelectChange): void {
    const tenantId: string | undefined = this._users
      .find(u => u.id === selection.value)?.tenant?.id;

    this.currentlySelectedUserId = selection.value;
    this.currentlySelectedUser = this._users
      .find(u => u.id === this.currentlySelectedUserId)?.username;

    this.userStatistic$ = this.tasksService.getUserStatistics(tenantId);
  }
}
