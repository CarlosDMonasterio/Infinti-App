import {User} from "./User";

export class AuditLog {
    id: number;
    created: number;
    account: User;
    action: string;
}
