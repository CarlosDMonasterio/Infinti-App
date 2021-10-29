import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {AdminComponent} from './components/admin/admin.component';
import {AdminSettingsComponent} from './components/admin/admin-settings/admin-settings.component';
import {AdminUsersComponent} from './components/admin/admin-users/admin-users.component';
import {AdminEmailSettingsComponent} from './components/admin/admin-email-settings/admin-email-settings.component';
import {ProfileComponent} from './components/profile/profile.component';
import {UserInfoComponent} from './components/profile/user-info/user-info.component';
import {ProfileDetailsResolver} from './components/profile/profile.details.resolver';
import {UserGroupsComponent} from './components/profile/user-groups/user-groups.component';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {ConfigComponent} from './components/config/config.component';
import {ResetPasswordComponent} from './components/reset-password/reset-password.component';
import {AdminGuardGuard} from './components/admin/admin-guard.guard';
import {SurveyComponent} from "./components/nurse/survey/survey.component";
import {DistrictsComponent} from "./components/admin/sites/districts.component";
import {IncidentComponent} from "./components/nurse/incident/incident.component";
import {AdminSchoolsComponent} from "./components/admin/admin-schools/admin-schools.component";
import {ReportsComponent} from "./components/reports/reports.component";
import {HygieneComponent} from "./components/nurse/hygiene/hygiene.component";
import {GlovesComponent} from "./components/nurse/gloves/gloves.component";
import {HygieneReportsComponent} from "./components/reports/hygiene-reports/hygiene-reports.component";
import {IncidentReportsComponent} from "./components/reports/incident-reports/incident-reports.component";
import {GloveReportsComponent} from "./components/reports/glove-reports/glove-reports.component";
import {SurveyReportsComponent} from "./components/reports/survey-reports/survey-reports.component";
import {TracingComponent} from "./components/nurse/tracing/tracing.component";
import {TestsComponent} from "./components/reports/tests/tests.component";
import {PatientsComponent} from "./components/reports/patients/patients.component";
import {ScheduleComponent} from "./components/reports/schedule/schedule.component";
import {RegisterComponent} from "./components/register/register.component";

const routes: Routes = [
    {path: '', component: DashboardComponent},
    {path: 'login', component: LoginComponent},
    {path: 'config', component: ConfigComponent},
    {path: 'survey', component: SurveyComponent},
    {path: 'incidents', component: IncidentComponent},
    {path: 'hygiene', component: HygieneComponent},
    {path: 'gloves', component: GlovesComponent},
    {path: 'tracing', component: TracingComponent},
    {path: 'forgotPassword', component: ForgotPasswordComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'resetPassword', component: ResetPasswordComponent},
    {
        path: 'admin', component: AdminComponent, canActivate: [AdminGuardGuard],
        children: [{
            path: '', redirectTo: 'users', pathMatch: 'full'
        }, {
            path: 'districts',
            component: DistrictsComponent
        }, {
            path: 'settings',
            component: AdminSettingsComponent
        }, {
            path: 'users',
            component: AdminUsersComponent
        }, {
            path: 'email',
            component: AdminEmailSettingsComponent
        }, {
            path: 'schools',
            component: AdminSchoolsComponent
        },]
    },
    {
        path: 'user/:id', component: ProfileComponent, resolve: {user: ProfileDetailsResolver},
        children: [{
            path: '', redirectTo: 'profile', pathMatch: 'full'
        }, {
            path: 'profile', component: UserInfoComponent
        }, {
            path: 'groups', component: UserGroupsComponent
        }]
    },
    {
        path: 'reports', component: ReportsComponent, canActivate: [AdminGuardGuard],
        children: [
            //     {
            //     path: '', redirectTo: 'profile', pathMatch: 'full'
            // },

            {
                path: 'surveys', component: SurveyReportsComponent
            },
            {
                path: 'hygiene', component: HygieneReportsComponent
            },
            {
                path: 'incidents', component: IncidentReportsComponent
            },
            {
                path: 'gloves', component: GloveReportsComponent
            },
            {
                path: 'tests', component: TestsComponent
            },
            {
                path: 'patients', component: PatientsComponent, children: [
                    {path: ':patientId', component: PatientsComponent}
                ]
            },
            {
                path: 'schedules', component: ScheduleComponent
            }
        ]
    },

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
