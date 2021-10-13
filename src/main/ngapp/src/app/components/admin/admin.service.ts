import {Injectable} from '@angular/core';
import {MenuItem} from './MenuItem';

@Injectable({
    providedIn: 'root'
})

export class AdminService {

    constructor() {
    }

    private generalSettingKeys = [
        'PROJECT_NAME',
        'DATA_DIR',
        'SCRIPTS_DIR',
        'URI_PREFIX',
        'BY_PASS_DIVA_REVIEW',
        'REGISTRATION_ALLOWED',
        'ENABLE_IP_QUESTIONS',
        'ENABLE_BIO_SECURITY_QUESTION',
        'BLAST_CMD_PATH',
        'DESIGN_COMPLETE_THRESHOLD',
        'ENABLE_ACCOUNT_VETTING',
        'ENABLE_DEFAULT_DIVA_TEAM',
        'AUTHENTICATION_METHOD'
    ];

    private emailSettingKeys = [
        'SMTP_HOST',
        'SEND_EMAIL_NOTIFICATION',
        'ADMIN_EMAIL',
        'SEND_EMAIL_ON_ERRORS',
        'ERROR_EMAIL_EXCEPTION_PREFIX'
    ];

    private booleanKeys = [
        'SEND_EMAIL_ON_ERRORS',
        'BY_PASS_DIVA_REVIEW',
        'SEND_EMAIL_NOTIFICATION',
        'REGISTRATION_ALLOWED',
        'ENABLE_IP_QUESTIONS',
        'ENABLE_BIO_SECURITY_QUESTION',
        'ENABLE_ACCOUNT_VETTING',
        'ENABLE_DEFAULT_DIVA_TEAM'
    ];

    private menuOptions: MenuItem[] = [
        {
            id: 'settings',
            display: 'General Settings',
            icon: 'fa-cogs'
        },
        {
            id: 'users',
            display: 'User Accounts',
            icon: 'fa-user'
        },
        // {
        //     id: 'groups',
        //     display: 'User Groups',
        //     icon: 'fa-group'
        // },
        {
            id: 'email',
            display: 'Email Settings',
            icon: 'fa-envelope'
        },
        // {
        //     id: 'tasks',
        //     display: 'Tasks',
        //     icon: 'fa-tasks'
        // },
        {
            id: 'api',
            display: 'Third Party Applications',
            icon: 'fa-link'
        },
        {
            id: 'instruments',
            display: 'Resources',
            icon: 'fa-industry'
        }
    ];

    getGeneralSettingKeys(): any {
        return this.generalSettingKeys;
    }

    getEmailKeys(): string[] {
        return this.emailSettingKeys;
    }

    getBooleanKeys(): any {
        return this.booleanKeys;
    }

    canAutoInstall(key): any {
        return key === 'BLAST_CMD_PATH';
    }

    getMenuOptions(): MenuItem[] {
        return this.menuOptions;
    }
}
