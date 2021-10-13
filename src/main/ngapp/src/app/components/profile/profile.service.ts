import {Injectable} from '@angular/core';
import {MenuItem} from "../admin/MenuItem";

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    constructor() {
    }

    generalSettingKeys = [
        'PROJECT_NAME',
        'SCRIPTS_DIR',
        'URI_PREFIX',
        'BY_PASS_DIVA_REVIEW',
        'REGISTRATION_ALLOWED',
        'ENABLE_IP_QUESTIONS',
        'ENABLE_BIO_SECURITY_QUESTION',
        'BLAST_CMD_PATH',
        'DESIGN_COMPLETE_THRESHOLD'
    ];

    emailSettingKeys = [
        'SMTP_HOST',
        'SEND_EMAIL_NOTIFICATION',
        'ADMIN_EMAIL',
        'SEND_EMAIL_ON_ERRORS',
        'ERROR_EMAIL_EXCEPTION_PREFIX'
    ];

    booleanKeys = [
        'SEND_EMAIL_ON_ERRORS',
        'BY_PASS_DIVA_REVIEW',
        'SEND_EMAIL_NOTIFICATION',
        'REGISTRATION_ALLOWED',
        'ENABLE_IP_QUESTIONS',
        'ENABLE_BIO_SECURITY_QUESTION'
    ];

    menuOptions: MenuItem[] = [
        {
            id: 'profile',
            display: 'Profile',
            icon: 'fa-user'
        },
        {
            id: 'groups',
            display: 'Private Groups',
            icon: 'fa-group'
        }
    ];

    getGeneralSettingKeys(): any {
        return this.generalSettingKeys;
    }

    getEmailKeys(): any {
        return this.emailSettingKeys;
    }

    getBooleanKeys(): any {
        return this.booleanKeys;
    }

    canAutoInstall(key: string): boolean {
        return key == 'BLAST_CMD_PATH'
    }

    getMenuOptions(): MenuItem[] {
        return this.menuOptions;
    }
}
