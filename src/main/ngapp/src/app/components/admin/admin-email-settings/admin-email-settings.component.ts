import {Component, OnInit} from '@angular/core';
import {AdminService} from '../admin.service';
import {Setting} from '../Setting';
import {Config} from '../Config';
import {HttpService} from '../../../http.service';
import {Result} from '../../../models/Result';

@Component({
    selector: 'app-admin-email-settings',
    templateUrl: './admin-email-settings.component.html',
    styleUrls: ['./admin-email-settings.component.css']
})
export class AdminEmailSettingsComponent implements OnInit {

    emailSettings: Config[] = [];
    settings: Setting[];

    constructor(private adminService: AdminService, private http: HttpService) {
    }

    ngOnInit(): void {
        this.http.get('settings').subscribe((response: Result<Setting>) => {
            this.settings = response.requested;
            this.setEmailSettings();
        });
    }

    updateBooleanSetting(setting): void {
        console.log(setting);
        if (!setting.edit) {
            return;
        }

        let newValue;
        let className;

        if (setting.value && (setting.value === 'true' || setting.value === 'yes')) {
            newValue = 'false';
            className = 'fa-toggle-off';
        } else {
            newValue = 'true';
            className = 'fa-toggle-on green';
        }

        setting.className = className;
        setting.value = newValue;

        // this.settings[i].value = newValue;
        this.updateSetting(setting);
    }

    // update a setting
    updateSetting(setting): void {
        const settingObject = setting.originalKey ? {key: setting.originalKey, value: setting.value} : setting;
        this.http.put('settings', settingObject).subscribe((result) => {
            if (!result) {
                return;
            }

            setting.edit = false;
            setting.value = result.value;
            // if (index && settingObject.key === $scope.settings[index].key) {
            //     $scope.settings[index].value = setting.value;
            // }
        });
    }

    setEmailSettings(): void {
        this.settings.forEach((setting) => {
            if (this.adminService.getEmailKeys().indexOf(setting.key) !== -1) {
                const config: Config = {
                    originalKey: setting.key,
                    key: (setting.key.replace(/_/g, ' ')).toLowerCase(),
                    value: setting.value,
                    isBoolean: this.adminService.getBooleanKeys().indexOf(setting.key) !== -1,
                    canAutoInstall: this.adminService.canAutoInstall(setting.key),
                    edit: false
                };

                if (config.isBoolean) {
                    const value = setting.value;
                    if (!value || (value.toUpperCase() !== 'TRUE' && value.toUpperCase() !== 'YES')) {
                        config.className = 'fa-toggle-off';
                    } else {
                        config.className = 'fa-toggle-on green';
                    }
                }

                this.emailSettings.push(config);
            }
        });
    }
}
