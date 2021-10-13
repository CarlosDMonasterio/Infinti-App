import {User} from './User';
import {Group} from './Group';

export class Permission {
    public id: number;
    public account: User;
    public group?: Group;
    public canWrite: boolean;
    public article: string;
    public articleId: number;
    public isPrincipalInvestigator: boolean;
    public canEdit: boolean;
    remote: boolean;
}
