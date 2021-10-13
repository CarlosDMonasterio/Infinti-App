import {Component, OnInit} from '@angular/core';
import {AdminService} from '../admin.service';
import {HttpService} from '../../../http.service';
import {Result} from '../../../models/Result';
import {Config} from '../Config';

@Component({
    selector: 'app-admin-settings',
    templateUrl: './admin-settings.component.html',
    styleUrls: ['./admin-settings.component.css']
})

export class AdminSettingsComponent implements OnInit {

    generalSettings: Config[];
    settings;

    constructor(private adminService: AdminService, private http: HttpService) {
    }

    ngOnInit(): void {
        this.http.get('settings').subscribe((response: Result<any>) => {
            this.settings = response.requested;
            this.generalSettings = [];

            response.requested.forEach(setting => {
                if (this.adminService.getGeneralSettingKeys().indexOf(setting.key) === -1) {
                    return;
                }

                const config: Config = {
                    originalKey: setting.key,
                    key: (setting.key.replace(/_/g, ' ')).toLowerCase(),
                    value: setting.value,
                    isBoolean: this.adminService.getBooleanKeys().indexOf(setting.key) !== -1,
                    canAutoInstall: this.adminService.canAutoInstall(setting.key),
                    className: '',
                    edit: false,
                };

                if (config.isBoolean) {
                    const value = setting.value;
                    if (value === undefined || (value.toUpperCase() !== 'TRUE' && value.toUpperCase() !== 'YES')) {
                        config.className = 'fa-toggle-off';
                    } else {
                        config.className = 'fa-toggle-on green';
                    }
                }

                if (setting.key === 'AUTHENTICATION_METHOD') {
                    config.options = ['LDAP', 'OPEN', 'DEFAULT'];
                }

                this.generalSettings.push(config);
            });

            this.generalSettings.sort((a, b) => {
                return a.key.toLowerCase().localeCompare(b.key.toLowerCase());
            });
        });
    }

    change(event, setting): void {
        console.log('change', event, setting);
        this.updateSetting(setting);
    }

    updateSetting(setting, index?): void {
        console.log(setting);
        const settingObject = setting.originalKey ? {key: setting.originalKey, value: setting.value} : setting;
        this.http.put('settings', settingObject).subscribe(result => {
            if (!result) {
                return;
            }

            setting.edit = false;
            setting.value = result.value;
            if (index && settingObject.key === this.settings[index].key) {
                this.settings[index].value = setting.value;
            }
        });
    }

    updateBooleanSetting(setting): void {
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
}
