import { Injectable, EventEmitter, Output } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';



@Injectable()
export class AuthenticationService {
    @Output() fireIsLoggedIn: EventEmitter<any> = new EventEmitter<any>();
    @Output() fireIsLoggedOut: EventEmitter<any> = new EventEmitter<any>();
    host: any;
    constructor(private http: HttpClient) {
        this.host = self.location.host.split(':')[0];
     }

    login(username: string, password: string): any {
        return this.http.post<any>('http://' + this.host + ':3001/users/authenticate', { username, password })
            .map(user => {
                // login successful if there's a jwt token in the response
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    this.fireIsLoggedIn.emit(user);
                }

                return user;
            });
    }

    logout(): any {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.fireIsLoggedIn.emit(localStorage.removeItem('currentUser'));
    }

    getEmitter(): any {
        return this.fireIsLoggedIn;
    }

    getEmitterOut(): any{
        return this.fireIsLoggedOut;

    }
}
