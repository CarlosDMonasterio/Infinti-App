export class User {
    id?: number;
    firstName: string;
    lastName: string;
    email: string;
    password?: string;
    newPassword?: string;
    sessionId?: string;
    public creationTime: string;
    public lastUpdateTime: string;
    public lastLoginTime: string;
    public disabled: boolean;
    public description: string;
    updatingActiveStatus?: boolean;
    updatingType?: boolean;
    allowedToChangePassword: boolean;
    usingTemporaryPassword: boolean;
    type?: string;
    isAdministrator: boolean;
    address: string;
}
